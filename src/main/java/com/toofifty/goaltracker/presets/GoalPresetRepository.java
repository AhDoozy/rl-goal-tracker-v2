package com.toofifty.goaltracker.presets;

import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.enums.Status;
import com.toofifty.goaltracker.models.task.ManualTask;
import com.toofifty.goaltracker.models.task.QuestTask;
import net.runelite.api.Quest;
import com.toofifty.goaltracker.utils.ReorderableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Central repository for built-in goal presets.
 *
 * Keep this tiny and dependency‑free so it’s easy to expand.
 */
public class GoalPresetRepository {

    /** Simple container representing a named preset. */
    public static class Preset {
        private final String name;
        private final String description;
        private final List<Goal> goals;

        public Preset(String name, String description, List<Goal> goals) {
            this.name = name;
            this.description = description;
            this.goals = goals;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public List<Goal> getGoals() { return goals; }

        @Override public String toString() { return name; }
    }

    // Minimal prerequisite graph for the quests we reference
    private static final Map<Quest, List<Quest>> PREREQS = new HashMap<>();
    static {
        // Simple chains
        PREREQS.put(Quest.FAIRYTALE_II__CURE_A_QUEEN, Arrays.asList(Quest.FAIRYTALE_I__GROWING_PAINS));
        PREREQS.put(Quest.MOURNINGS_END_PART_II, Arrays.asList(Quest.MOURNINGS_END_PART_I));
        PREREQS.put(Quest.MOURNINGS_END_PART_I, Arrays.asList(Quest.ROVING_ELVES));
        PREREQS.put(Quest.ROVING_ELVES, Arrays.asList(Quest.REGICIDE, Quest.WATERFALL_QUEST));
        PREREQS.put(Quest.REGICIDE, Arrays.asList(Quest.UNDERGROUND_PASS));
        // Desert Treasure I chain (trimmed to common quest prereqs)
        PREREQS.put(Quest.DESERT_TREASURE_I, Arrays.asList(
                Quest.PRIEST_IN_PERIL,
                Quest.THE_DIG_SITE,
                Quest.THE_TOURIST_TRAP,
                Quest.TEMPLE_OF_IKOV,
                Quest.TROLL_STRONGHOLD
        ));
        PREREQS.put(Quest.TROLL_STRONGHOLD, Arrays.asList(Quest.DEATH_PLATEAU));
        // Animal Magnetism common prereqs
        PREREQS.put(Quest.ANIMAL_MAGNETISM, Arrays.asList(
                Quest.THE_RESTLESS_GHOST,
                Quest.ERNEST_THE_CHICKEN,
                Quest.PRIEST_IN_PERIL
        ));
        // Lunar Diplomacy common prereqs
        PREREQS.put(Quest.LUNAR_DIPLOMACY, Arrays.asList(
                Quest.THE_FREMENNIK_TRIALS
        ));
        // Song of the Elves depends on the elf line; ensure ME2 present via above mappings
        PREREQS.put(Quest.SONG_OF_THE_ELVES, Arrays.asList(Quest.MOURNINGS_END_PART_II));
        // Sins of the Father (trimmed main chain)
        PREREQS.put(Quest.SINS_OF_THE_FATHER, Arrays.asList(
                Quest.A_TASTE_OF_HOPE
        ));
    }

    private static void addQuestWithPrereqs(Quest quest, Set<Quest> seen, ReorderableList<com.toofifty.goaltracker.models.task.Task> out)
    {
        if (seen.contains(quest)) return;
        List<Quest> reqs = PREREQS.get(quest);
        if (reqs != null) {
            for (Quest q : reqs) {
                addQuestWithPrereqs(q, seen, out);
            }
        }
        // After ensuring prereqs are added, add the quest itself if missing
        if (!seen.contains(quest)) {
            out.add(QuestTask.builder().quest(quest).build());
            seen.add(quest);
        }
    }

    private static void expandWithPrereqs(ReorderableList<com.toofifty.goaltracker.models.task.Task> tasks)
    {
        Set<Quest> present = new HashSet<>();
        for (com.toofifty.goaltracker.models.task.Task t : tasks) {
            if (t instanceof QuestTask) {
                present.add(((QuestTask) t).getQuest());
            }
        }
        // Build a new ordered list with prereqs inserted before dependents
        ReorderableList<com.toofifty.goaltracker.models.task.Task> expanded = new ReorderableList<>();
        Set<Quest> added = new HashSet<>();
        for (com.toofifty.goaltracker.models.task.Task t : new ArrayList<>(tasks)) {
            if (t instanceof QuestTask) {
                addQuestWithPrereqs(((QuestTask) t).getQuest(), added, expanded);
            } else {
                expanded.add(t);
            }
        }
        tasks.clear();
        tasks.addAll(expanded);
    }

    /** Return all available presets. */
    public static List<Preset> getAll() {
        List<Preset> list = new ArrayList<>();
        list.add(buildQuestCapeCore());
        list.add(buildEarlyIronman());
        return list;
    }

    private static Preset buildQuestCapeCore() {
        ReorderableList<com.toofifty.goaltracker.models.task.Task> tasks = ReorderableList.from(
                QuestTask.builder().quest(Quest.ANIMAL_MAGNETISM).build(),
                QuestTask.builder().quest(Quest.FAIRYTALE_I__GROWING_PAINS).build(),
                QuestTask.builder().quest(Quest.FAIRYTALE_II__CURE_A_QUEEN).build(),
                QuestTask.builder().quest(Quest.DESERT_TREASURE_I).build(),
                QuestTask.builder().quest(Quest.DRAGON_SLAYER_II).build(),
                QuestTask.builder().quest(Quest.LUNAR_DIPLOMACY).build(),
                QuestTask.builder().quest(Quest.PRIEST_IN_PERIL).build(),
                QuestTask.builder().quest(Quest.UNDERGROUND_PASS).build(),
                QuestTask.builder().quest(Quest.REGICIDE).build(),
                QuestTask.builder().quest(Quest.ROVING_ELVES).build(),
                QuestTask.builder().quest(Quest.MOURNINGS_END_PART_I).build(),
                QuestTask.builder().quest(Quest.MOURNINGS_END_PART_II).build(),
                QuestTask.builder().quest(Quest.SONG_OF_THE_ELVES).build(),
                QuestTask.builder().quest(Quest.SINS_OF_THE_FATHER).build()
        );
        expandWithPrereqs(tasks);
        Goal goal = Goal.builder()
                .description("Quest Cape — Core unlocks")
                .tasks(tasks)
                .build();
        return new Preset(
                "Quest Cape (Core)",
                "Essential quests and unlocks toward Quest Cape.",
                Arrays.asList(goal)
        );
    }

    private static Preset buildEarlyIronman() {
        Goal early = Goal.builder()
                .description("Early Game Ironman — Foundation")
                .tasks(ReorderableList.from(
                        ManualTask.builder().description("Unlock Ardougne cloak 1 (monastery teleport)").status(Status.NOT_STARTED).build(),
                        ManualTask.builder().description("Do Wintertodt to ~70 Firemaking").status(Status.NOT_STARTED).build(),
                        ManualTask.builder().description("Get graceful set").status(Status.NOT_STARTED).build(),
                        ManualTask.builder().description("Obtain Dramen staff (Fairy rings)").status(Status.NOT_STARTED).build(),
                        ManualTask.builder().description("Start Barrows for early gear and runes").status(Status.NOT_STARTED).build()
                ))
                .build();
        return new Preset(
                "Early Game Ironman",
                "Starter progression path for fresh iron accounts.",
                Arrays.asList(early)
        );
    }
}
