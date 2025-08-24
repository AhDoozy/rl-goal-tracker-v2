package com.ahdoozy.goaltrackerv2;

import com.ahdoozy.goaltrackerv2.models.task.*;
import com.google.inject.Provides;
import com.ahdoozy.goaltrackerv2.models.enums.TaskType;
import com.ahdoozy.goaltrackerv2.services.TaskIconService;
import com.ahdoozy.goaltrackerv2.services.TaskUpdateService;
import com.ahdoozy.goaltrackerv2.ui.GoalTrackerPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.chatbox.ChatboxItemSearch;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@PluginDescriptor(name = "Goal Tracker", description = "Keep track of your goals and complete them automatically")
public class GoalTrackerV2Plugin extends Plugin
{
    public static final int[] PLAYER_INVENTORIES = {
        InventoryID.INVENTORY.getId(),
        InventoryID.EQUIPMENT.getId(),
        InventoryID.BANK.getId(),
        InventoryID.SEED_VAULT.getId(),
        InventoryID.GROUP_STORAGE.getId()
    };

    @Getter
    @Inject
    private Client client;

    @Getter
    @Inject
    private SkillIconManager skillIconManager;

    @Getter
    @Inject
    private ItemManager itemManager;

    @Getter
    @Inject
    private ChatboxItemSearch itemSearch;

    @Getter
    @Inject
    private ChatboxPanelManager chatboxPanelManager;

    @Inject
    private ClientToolbar clientToolbar;

    @Getter
    @Inject
    private ClientThread clientThread;

    @Getter
    @Inject
    private ItemCache itemCache;

    @Inject
    private ChatMessageManager chatMessageManager;

    @Getter
    @Inject
    private GoalTrackerV2Config config;

    @Getter
    @Inject
    private TaskUpdateService taskUpdateService;

    @Getter
    @Inject
    private TaskIconService taskIconService;

    @Getter
    @Inject
    private TaskUIStatusManager uiStatusManager;

    @Getter
    @Inject
    private GoalManager goalManager;

    @Inject
    private GoalTrackerPanel goalTrackerPanel;

    private NavigationButton uiNavigationButton;

    @Setter
    private boolean validateAll = true;

    private boolean warmedIcons = false;

    @Override
    protected void startUp()
    {
        goalManager.load();
        itemCache.load();
        goalTrackerPanel.home();

        final AsyncBufferedImage icon = itemManager.getImage(ItemID.TODO_LIST);

        icon.onLoaded(() -> {
            uiNavigationButton = NavigationButton.builder()
                .tooltip("Goal Tracker")
                .icon(icon)
                .priority(7)
                .panel(goalTrackerPanel)
                .build();

            clientToolbar.addNavigation(uiNavigationButton);
        });

        goalTrackerPanel.onGoalUpdated((goal) -> goalManager.save());
        goalTrackerPanel.onTaskAdded((task) -> {
            if (taskUpdateService.update(task)) {
                if (task.getStatus().isCompleted()) {
                    notifyTask(task);
                }

                uiStatusManager.refresh(task);
            }

            goalManager.save();
        });
        goalTrackerPanel.onTaskUpdated((task) -> goalManager.save());

        // Preload item icons at plugin startup so they are visible immediately
        warmItemIcons();
        warmedIcons = true; // avoid re-warming on first login tick
    }

    @Override
    protected void shutDown()
    {
        clientToolbar.removeNavigation(uiNavigationButton);
    }

    @Subscribe
    public void onSessionOpen(SessionOpen event)
    {
        goalManager.load();
        goalTrackerPanel.refresh();
    }

    @Subscribe
    public void onStatChanged(StatChanged event)
    {
        List<SkillLevelTask> skillLevelTasks = goalManager.getIncompleteTasksByType(TaskType.SKILL_LEVEL);
        for (SkillLevelTask task : skillLevelTasks) {
            if (!taskUpdateService.update(task, event)) continue;

            if (task.getStatus().isCompleted()) {
                notifyTask(task);
            }

            uiStatusManager.refresh(task);
            this.goalManager.save();
        }

        List<SkillXpTask> skillXpTasks = goalManager.getIncompleteTasksByType(TaskType.SKILL_XP);
        for (SkillXpTask task : skillXpTasks) {
            if (!taskUpdateService.update(task, event)) continue;

            if (task.getStatus().isCompleted()) {
                notifyTask(task);
            }

            uiStatusManager.refresh(task);
            this.goalManager.save();
        }
    }

    public void notifyTask(Task task)
    {
        if (client.getGameState() != GameState.LOGGED_IN || task.isNotified()) return;

        log.debug("Notify: [Goal Tracker] You have completed a task: " + task + "!");

        String message = "[Goal Tracker] You have completed a task: " + task + "!";
        String formattedMessage = ColorUtil.wrapWithColorTag(message, config.completionMessageColor());
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", formattedMessage, null);

        task.setNotified(true);
    }

    public void warmItemIcons()
    {
        // Iterate all tasks and ensure item icons are requested so they appear without opening search
        goalManager.getGoals().forEach(goal -> {
            if (goal.getTasks() == null) return;
            goal.getTasks().forEach(task -> {
                if (task instanceof ItemTask)
                {
                    try {
                        taskIconService.get((ItemTask) task);
                    } catch (Exception ignored) { }
                }
            });
        });
        // Refresh UI after warming to repaint icons
        javax.swing.SwingUtilities.invokeLater(goalTrackerPanel::refresh);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (client.getGameState() != GameState.LOGGED_IN) {
            warmedIcons = false;
            return;
        }

        // redo the login check on the next game tick
        validateAll = true;
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (!validateAll) {
            return;
        }
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        validateAll = false;
        // perform a full refresh just once on login
        // onGameStateChanged reports incorrect quest statuses,
        // so this need to be done in this subscriber
        goalTrackerPanel.refresh();

        if (!warmedIcons)
        {
            warmItemIcons();
            warmedIcons = true;
        }

        goalManager.getGoals().stream()
            .flatMap(goal -> goal.getTasks().stream())
            .filter(task -> !task.getStatus().isCompleted())
            .forEach(task -> {
                if (taskUpdateService.update(task)) {
                    if (task.getStatus().isCompleted()) {
                        notifyTask(task);
                    }
                    uiStatusManager.refresh(task);
                }
            });

        List<QuestTask> questTasks = goalManager.getIncompleteTasksByType(TaskType.QUEST);
        for (QuestTask task : questTasks) {
            if (!taskUpdateService.update(task)) continue;

            if (task.getStatus().isCompleted()) {
                notifyTask(task);
            }

            uiStatusManager.refresh(task);
        }

        goalManager.save();
        goalTrackerPanel.refresh();
    }

    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        if (event.getType() != ChatMessageType.GAMEMESSAGE || !event.getMessage().contains("Quest complete")) return;

        List<QuestTask> questTasks = goalManager.getIncompleteTasksByType(TaskType.QUEST);
        for (QuestTask task : questTasks) {
            if (!taskUpdateService.update(task)) continue;

            if (task.getStatus().isCompleted()) {
                notifyTask(task);
            }

            uiStatusManager.refresh(task);
            this.goalManager.save();
        }
        goalTrackerPanel.refresh();
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event)
    {
        if (IntStream.of(GoalTrackerV2Plugin.PLAYER_INVENTORIES).noneMatch((id) -> id == event.getContainerId())) return;

        itemCache.update(event.getContainerId(), event.getItemContainer().getItems());

        List<ItemTask> itemTasks = goalManager.getIncompleteTasksByType(TaskType.ITEM);
        for (ItemTask task : itemTasks) {
            if (!taskUpdateService.update(task)) continue;

            if (task.getStatus().isCompleted()) {
                notifyTask(task);
            }

            // always refresh item tasks, since the acquired
            // count could have changed
            uiStatusManager.refresh(task);
            this.goalManager.save();
        }
    }

    @Provides
    GoalTrackerV2Config getGoalTrackerConfig(ConfigManager configManager)
    {
        return configManager.getConfig(GoalTrackerV2Config.class);
    }
}
