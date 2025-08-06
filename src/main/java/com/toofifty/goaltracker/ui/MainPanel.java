package com.toofifty.goaltracker.ui;

import com.toofifty.goaltracker.GoalTrackerPlugin;
import com.toofifty.goaltracker.models.Profile;
import com.toofifty.goaltracker.ui.ProfileManager.ProfileManager;
import com.toofifty.goaltracker.ui.ProfileManager.ProfileManagerPanel;
import net.runelite.client.ui.PluginPanel;

import java.awt.CardLayout;

public class MainPanel extends PluginPanel
{
    private final CardLayout layout = new CardLayout();
    private final GoalTrackerPanel goalTrackerPanel;
    private final ProfileManagerPanel profileManagerPanel;

    public MainPanel(GoalTrackerPlugin plugin)
    {
        setLayout(layout);
        setPreferredSize(new java.awt.Dimension(PluginPanel.PANEL_WIDTH, 400));

        this.goalTrackerPanel = new GoalTrackerPanel(plugin, plugin.getGoalManager());
        this.profileManagerPanel = new ProfileManagerPanel(ProfileManager.getInstance().getProfiles());

        profileManagerPanel.onProfileSelected(profile -> {
            ProfileManager.getInstance().setActiveProfile(profile);
            goalTrackerPanel.setGoals(profile.getGoals());
            showGoals();
        });

        add(profileManagerPanel, "profile");
        add(goalTrackerPanel, "goals");

        showProfileSelector();
    }

    public void showGoals()
    {
        layout.show(this, "goals");
    }

    public void showProfileSelector()
    {
        profileManagerPanel.refreshProfiles();
        layout.show(this, "profile");
    }
}
