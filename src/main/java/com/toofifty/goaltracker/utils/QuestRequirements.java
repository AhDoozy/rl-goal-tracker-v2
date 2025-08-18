package com.toofifty.goaltracker.utils;

import com.toofifty.goaltracker.models.task.QuestTask;
import com.toofifty.goaltracker.models.task.SkillLevelTask;
import com.toofifty.goaltracker.models.task.Task;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestRequirements
{
    // Map of quests to their requirements (quests and skills)
    private static final Map<Quest, List<Task>> REQUIREMENT_MAP;

    // Static initializer with minimal dependencies
    static {
        REQUIREMENT_MAP = new HashMap<>();
        REQUIREMENT_MAP.put(
            Quest.FAIRYTALE_I__GROWING_PAINS,
            Arrays.asList(
                QuestTask.builder().quest(Quest.LOST_CITY).build(),
                QuestTask.builder().quest(Quest.NATURE_SPIRIT).build(),
                SkillLevelTask.builder().skill(Skill.FARMING).level(18).build()
            )
        );
        REQUIREMENT_MAP.put(
            Quest.NATURE_SPIRIT,
            Arrays.asList(
                QuestTask.builder().quest(Quest.PRIEST_IN_PERIL).build(),
                QuestTask.builder().quest(Quest.THE_RESTLESS_GHOST).build(),
                SkillLevelTask.builder().skill(Skill.PRAYER).level(18).build()
            )
        );
        REQUIREMENT_MAP.put(
            Quest.LOST_CITY,
            Arrays.asList(
                SkillLevelTask.builder().skill(Skill.CRAFTING).level(31).build(),
                SkillLevelTask.builder().skill(Skill.WOODCUTTING).level(36).build()
            )
        );
        REQUIREMENT_MAP.put(
            Quest.PRIEST_IN_PERIL,
            Collections.emptyList() // No requirements
        );
        REQUIREMENT_MAP.put(
            Quest.THE_RESTLESS_GHOST,
            Collections.emptyList() // No requirements
        );
    }

    /**
     * Get all requirements for a quest, including recursive sub-quest requirements.
     * @param quest The quest to get requirements for.
     * @param indentLevel The indentation level for the tasks (0 for main quest).
     * @return List of tasks (quest and skill requirements) with appropriate indentation.
     */
    public static List<Task> getRequirements(Quest quest, int indentLevel)
    {
        List<Task> tasks = new ArrayList<>();
        if (indentLevel >= 3) {
            // Respect Task.java's max indent level of 3
            return tasks;
        }

        List<Task> directRequirements = REQUIREMENT_MAP.getOrDefault(quest, Collections.emptyList());
        for (Task task : directRequirements) {
            Task withIndent = copyWithIndent(task, indentLevel + 1);
            tasks.add(withIndent);

            if (withIndent instanceof QuestTask) {
                Quest subQuest = ((QuestTask) withIndent).getQuest();
                List<Task> subRequirements = getRequirements(subQuest, indentLevel + 1);
                tasks.addAll(subRequirements);
            }
        }

        return tasks;
    }

    private static Task copyWithIndent(Task task, int indentLevel)
    {
        if (task instanceof QuestTask) {
            QuestTask qt = (QuestTask) task;
            return QuestTask.builder()
                .quest(qt.getQuest())
                .indentLevel(indentLevel)
                .build();
        }
        if (task instanceof SkillLevelTask) {
            SkillLevelTask st = (SkillLevelTask) task;
            return SkillLevelTask.builder()
                .skill(st.getSkill())
                .level(st.getLevel())
                .indentLevel(indentLevel)
                .build();
        }
        // Fallback: set indent on the same instance (least preferable, but safe for unknown subclasses)
        task.setIndentLevel(indentLevel);
        return task;
    }
}