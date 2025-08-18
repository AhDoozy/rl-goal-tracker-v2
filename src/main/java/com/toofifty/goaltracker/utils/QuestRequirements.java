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
                Quest.DRAGON_SLAYER_I,
                Arrays.asList(
                        SkillLevelTask.builder().skill(Skill.ATTACK).level(32).build(),
                        QuestTask.builder().quest(Quest.PRIEST_IN_PERIL).build()
                )
        );
        REQUIREMENT_MAP.put(
                Quest.HEROES_QUEST,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.DRAGON_SLAYER_I).build(),
                        QuestTask.builder().quest(Quest.LOST_CITY).build(),
                        QuestTask.builder().quest(Quest.PRIEST_IN_PERIL).build(),
                        SkillLevelTask.builder().skill(Skill.CRAFTING).level(48).build(),
                        SkillLevelTask.builder().skill(Skill.HERBLORE).level(53).build(),
                        SkillLevelTask.builder().skill(Skill.SLAYER).level(55).build()
                )
        );
        REQUIREMENT_MAP.put(
                Quest.THE_GRAND_TREE,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.TREE_GNOME_VILLAGE).build(),
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(25).build(),
                        SkillLevelTask.builder().skill(Skill.RANGED).level(25).build()
                )
        );
        REQUIREMENT_MAP.put(
                Quest.TREE_GNOME_VILLAGE,
                Arrays.asList(
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(8).build()
                )
        );

        // Additional quest requirements based on OSRS Wiki
        // Animal Magnetism requires completion of The Restless Ghost, Ernest the Chicken and Priest in Peril.
        // It also requires Slayer 18, Crafting 19, Ranged 30 and Woodcutting 35【816824159430957†L50-L60】.
        REQUIREMENT_MAP.put(
                Quest.ANIMAL_MAGNETISM,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.THE_RESTLESS_GHOST).build(),
                        QuestTask.builder().quest(Quest.ERNEST_THE_CHICKEN).build(),
                        QuestTask.builder().quest(Quest.PRIEST_IN_PERIL).build(),
                        SkillLevelTask.builder().skill(Skill.SLAYER).level(18).build(),
                        SkillLevelTask.builder().skill(Skill.CRAFTING).level(19).build(),
                        SkillLevelTask.builder().skill(Skill.RANGED).level(30).build(),
                        SkillLevelTask.builder().skill(Skill.WOODCUTTING).level(35).build()
                )
        );

        // Another Slice of H.A.M. requires Death to the Dorgeshuun, The Giant Dwarf and The Dig Site quests
        // plus Attack 15 and Prayer 25【497556247310731†L53-L64】.
        REQUIREMENT_MAP.put(
                Quest.ANOTHER_SLICE_OF_HAM,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.DEATH_TO_THE_DORGESHUUN).build(),
                        QuestTask.builder().quest(Quest.THE_GIANT_DWARF).build(),
                        QuestTask.builder().quest(Quest.THE_DIG_SITE).build(),
                        SkillLevelTask.builder().skill(Skill.ATTACK).level(15).build(),
                        SkillLevelTask.builder().skill(Skill.PRAYER).level(25).build()
                )
        );

        // The Giant Dwarf requires Crafting 12, Firemaking 16, Magic 33 and Thieving 14【870029561849796†L64-L70】.
        REQUIREMENT_MAP.put(
                Quest.THE_GIANT_DWARF,
                Arrays.asList(
                        SkillLevelTask.builder().skill(Skill.CRAFTING).level(12).build(),
                        SkillLevelTask.builder().skill(Skill.FIREMAKING).level(16).build(),
                        SkillLevelTask.builder().skill(Skill.MAGIC).level(33).build(),
                        SkillLevelTask.builder().skill(Skill.THIEVING).level(14).build()
                )
        );

        // The Lost Tribe requires Goblin Diplomacy and Rune Mysteries plus Agility 13, Thieving 13 and Mining 17【123371285222949†L58-L64】.
        REQUIREMENT_MAP.put(
                Quest.THE_LOST_TRIBE,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.GOBLIN_DIPLOMACY).build(),
                        QuestTask.builder().quest(Quest.RUNE_MYSTERIES).build(),
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(13).build(),
                        SkillLevelTask.builder().skill(Skill.THIEVING).level(13).build(),
                        SkillLevelTask.builder().skill(Skill.MINING).level(17).build()
                )
        );

        // Death to the Dorgeshuun requires The Lost Tribe along with Agility 23 and Thieving 23【61594254303929†L61-L67】.
        REQUIREMENT_MAP.put(
                Quest.DEATH_TO_THE_DORGESHUUN,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.THE_LOST_TRIBE).build(),
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(23).build(),
                        SkillLevelTask.builder().skill(Skill.THIEVING).level(23).build()
                )
        );


        // The Dig Site requires Agility 10, Herblore 10 and Thieving 25【916139208382379†L63-L68】.
        REQUIREMENT_MAP.put(
                Quest.THE_DIG_SITE,
                Arrays.asList(
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(10).build(),
                        SkillLevelTask.builder().skill(Skill.HERBLORE).level(10).build(),
                        SkillLevelTask.builder().skill(Skill.THIEVING).level(25).build()
                )
        );

        // Bone Voyage requires completion of The Dig Site and 100 Kudos; no skill levels【366447167732673†L46-L51】.
        REQUIREMENT_MAP.put(
                Quest.BONE_VOYAGE,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.THE_DIG_SITE).build()
                )
        );

        // Client of Kourend requires X Marks the Spot; no skill requirements【215435182682270†L42-L47】.
        REQUIREMENT_MAP.put(
                Quest.CLIENT_OF_KOUREND,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.X_MARKS_THE_SPOT).build()
                )
        );


        // Ghosts Ahoy requires Priest in Peril, The Restless Ghost, Agility 25 and Cooking 20【735691433885456†L54-L58】.
        REQUIREMENT_MAP.put(
                Quest.GHOSTS_AHOY,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.PRIEST_IN_PERIL).build(),
                        QuestTask.builder().quest(Quest.THE_RESTLESS_GHOST).build(),
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(25).build(),
                        SkillLevelTask.builder().skill(Skill.COOKING).level(20).build()
                )
        );

        // Dream Mentor requires completion of Lunar Diplomacy and Eadgar's Ruse【148132524076550†L48-L60】【282840601330941†L49-L56】.
        REQUIREMENT_MAP.put(
                Quest.DREAM_MENTOR,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.LUNAR_DIPLOMACY).build(),
                        QuestTask.builder().quest(Quest.EADGARS_RUSE).build()
                )
        );

        // Lunar Diplomacy requires The Fremennik Trials, Lost City, Rune Mysteries and Shilo Village.
        // It also requires multiple skills: Herblore 5, Crafting 61, Defence 40, Firemaking 49, Magic 65, Mining 60 and Woodcutting 55【209799958490204†L50-L67】.
        REQUIREMENT_MAP.put(
                Quest.LUNAR_DIPLOMACY,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.THE_FREMENNIK_TRIALS).build(),
                        QuestTask.builder().quest(Quest.LOST_CITY).build(),
                        QuestTask.builder().quest(Quest.RUNE_MYSTERIES).build(),
                        QuestTask.builder().quest(Quest.SHILO_VILLAGE).build(),
                        SkillLevelTask.builder().skill(Skill.HERBLORE).level(5).build(),
                        SkillLevelTask.builder().skill(Skill.CRAFTING).level(61).build(),
                        SkillLevelTask.builder().skill(Skill.DEFENCE).level(40).build(),
                        SkillLevelTask.builder().skill(Skill.FIREMAKING).level(49).build(),
                        SkillLevelTask.builder().skill(Skill.MAGIC).level(65).build(),
                        SkillLevelTask.builder().skill(Skill.MINING).level(60).build(),
                        SkillLevelTask.builder().skill(Skill.WOODCUTTING).level(55).build()
                )
        );

        // Eadgar's Ruse requires Druidic Ritual and Troll Stronghold, with Herblore 31【282840601330941†L49-L56】.
        REQUIREMENT_MAP.put(
                Quest.EADGARS_RUSE,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.DRUIDIC_RITUAL).build(),
                        QuestTask.builder().quest(Quest.TROLL_STRONGHOLD).build(),
                        SkillLevelTask.builder().skill(Skill.HERBLORE).level(31).build()
                )
        );

        // Troll Stronghold requires Death Plateau and Agility 15【881893560221627†L45-L49】.
        REQUIREMENT_MAP.put(
                Quest.TROLL_STRONGHOLD,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.DEATH_PLATEAU).build(),
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(15).build()
                )
        );

        // Shilo Village requires Jungle Potion (and thus Druidic Ritual) plus Crafting 20 and Agility 32【397040585224810†L50-L57】.
        REQUIREMENT_MAP.put(
                Quest.SHILO_VILLAGE,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.JUNGLE_POTION).build(),
                        SkillLevelTask.builder().skill(Skill.CRAFTING).level(20).build(),
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(32).build()
                )
        );

        // Jungle Potion requires Druidic Ritual and Herblore 3【532018769049742†L45-L49】.
        REQUIREMENT_MAP.put(
                Quest.JUNGLE_POTION,
                Arrays.asList(
                        QuestTask.builder().quest(Quest.DRUIDIC_RITUAL).build(),
                        SkillLevelTask.builder().skill(Skill.HERBLORE).level(3).build()
                )
        );


        // Dragon Slayer II requires numerous quests and high skill levels【916074840360349†L82-L92】【916074840360349†L93-L129】.
        REQUIREMENT_MAP.put(
                Quest.DRAGON_SLAYER_II,
                Arrays.asList(
                        // Quest prerequisites
                        QuestTask.builder().quest(Quest.LEGENDS_QUEST).build(),
                        QuestTask.builder().quest(Quest.DREAM_MENTOR).build(),
                        QuestTask.builder().quest(Quest.A_TAIL_OF_TWO_CATS).build(),
                        QuestTask.builder().quest(Quest.ANIMAL_MAGNETISM).build(),
                        QuestTask.builder().quest(Quest.GHOSTS_AHOY).build(),
                        QuestTask.builder().quest(Quest.BONE_VOYAGE).build(),
                        QuestTask.builder().quest(Quest.CLIENT_OF_KOUREND).build(),
                        // Skill requirements
                        SkillLevelTask.builder().skill(Skill.MAGIC).level(75).build(),
                        SkillLevelTask.builder().skill(Skill.SMITHING).level(70).build(),
                        SkillLevelTask.builder().skill(Skill.MINING).level(68).build(),
                        SkillLevelTask.builder().skill(Skill.CRAFTING).level(62).build(),
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(60).build(),
                        SkillLevelTask.builder().skill(Skill.THIEVING).level(60).build(),
                        SkillLevelTask.builder().skill(Skill.CONSTRUCTION).level(50).build(),
                        SkillLevelTask.builder().skill(Skill.HITPOINTS).level(50).build()
                )
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