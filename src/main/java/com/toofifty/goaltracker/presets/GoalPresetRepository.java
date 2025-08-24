package com.toofifty.goaltracker.presets;

import com.toofifty.goaltracker.models.Goal;
import com.toofifty.goaltracker.models.task.ItemTask;
import com.toofifty.goaltracker.models.task.QuestTask;
import com.toofifty.goaltracker.models.task.SkillLevelTask;
import com.toofifty.goaltracker.utils.ReorderableList;
import lombok.Getter;
import net.runelite.api.Quest;
import net.runelite.api.Skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.runelite.api.ItemID.*;

public class GoalPresetRepository {

    @Getter
    public static final class Preset {
        private final String name;
        private final String description;
        private final List<Goal> goals;
        public Preset(String name, String description, List<Goal> goals) {
            this.name = name;
            this.description = description;
            this.goals = goals;
        }

        @Override public String toString() { return name; }
    }

    public static List<Preset> getAll() {
        List<Preset> list = new ArrayList<>();
        // list.add(buildLadlorIronman());
        // list.add(buildLadlorMelee());
        // list.add(buildLadlorRanged());
        // list.add(buildLadlorMagic());
        // list.add(buildLadlorUtility());
        // list.add(buildLadlorVoidSet());
        // list.add(buildLadlorGodCapes());
        // list.add(buildLadlorCrystalBowfa());
        // list.add(buildLadlorJewelryBoots());
        // list.add(buildLadlorSlayerUndead());
        // list.add(buildLadlorImbuedRings());
        // list.add(buildLadlorGwdArmor());
        // list.add(buildLadlorEliteVoid());
        // list.add(buildLadlorPrayerScrolls());
        // list.add(buildLadlorRaidsUniques());
        // list.add(buildLadlorMilestones());
        list.add(buildEarlyIronman());
        list.add(buildMidIronman());
        list.add(buildLateIronman());
        return list;
    }

    /*
    private static Preset buildLadlorIronman() {
        // Melee lane
        Goal melee = Goal.builder()
                .description("Ladlor — Melee Progression")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(DRAGON_SCIMITAR).itemName("Dragon scimitar").quantity(1).build(),
                        ItemTask.builder().itemId(DRAGON_DEFENDER).itemName("Dragon defender").quantity(1).build(),
                        ItemTask.builder().itemId(FIGHTER_TORSO).itemName("Fighter torso").quantity(1).build(),
                        ItemTask.builder().itemId(DRAGON_BOOTS).itemName("Dragon boots").quantity(1).build(),
                        ItemTask.builder().itemId(ABYSSAL_WHIP).itemName("Abyssal whip").quantity(1).build(),
                        ItemTask.builder().itemId(BERSERKER_RING).itemName("Berserker ring").quantity(1).build(),
                        ItemTask.builder().itemId(FIRE_CAPE).itemName("Fire cape").quantity(1).build(),
                        ItemTask.builder().itemId(FEROCIOUS_GLOVES).itemName("Ferocious gloves").quantity(1).build(),
                        ItemTask.builder().itemId(ABYSSAL_TENTACLE).itemName("Abyssal tentacle").quantity(1).build()
                ))
                .build();

        // Ranged lane
        Goal ranged = Goal.builder()
                .description("Ladlor — Ranged Progression")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(MAGIC_SHORTBOW).itemName("Magic shortbow").quantity(1).build(),
                        ItemTask.builder().itemId(RUNE_CROSSBOW).itemName("Rune crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(BLACK_DHIDE_BODY).itemName("Black d'hide body").quantity(1).build(),
                        ItemTask.builder().itemId(AVAS_ACCUMULATOR).itemName("Ava's accumulator").quantity(1).build(),
                        ItemTask.builder().itemId(KARILS_LEATHERTOP).itemName("Karil's leathertop").quantity(1).build(),
                        ItemTask.builder().itemId(KARILS_LEATHERSKIRT).itemName("Karil's leatherskirt").quantity(1).build(),
                        ItemTask.builder().itemId(TOXIC_BLOWPIPE).itemName("Toxic blowpipe").quantity(1).build(),
                        ItemTask.builder().itemId(ARMADYL_CROSSBOW).itemName("Armadyl crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(AVAS_ASSEMBLER).itemName("Ava's assembler").quantity(1).build()
                ))
                .build();

        // Magic lane
        Goal magic = Goal.builder()
                .description("Ladlor — Magic Progression")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(MYSTIC_ROBE_TOP).itemName("Mystic robe top").quantity(1).build(),
                        ItemTask.builder().itemId(MYSTIC_ROBE_BOTTOM).itemName("Mystic robe bottom").quantity(1).build(),
                        ItemTask.builder().itemId(OCCULT_NECKLACE).itemName("Occult necklace").quantity(1).build(),
                        ItemTask.builder().itemId(MAGES_BOOK).itemName("Mage's book").quantity(1).build(),
                        ItemTask.builder().itemId(TRIDENT_OF_THE_SEAS).itemName("Trident of the seas").quantity(1).build(),
                        ItemTask.builder().itemId(UNCHARGED_TOXIC_TRIDENT).itemName("Trident of the swamp").quantity(1).build(),
                        ItemTask.builder().itemId(ANCESTRAL_ROBE_TOP).itemName("Ancestral robe top").quantity(1).build(),
                        ItemTask.builder().itemId(ANCESTRAL_ROBE_BOTTOM).itemName("Ancestral robe bottom").quantity(1).build(),
                        ItemTask.builder().itemId(ANCESTRAL_HAT).itemName("Ancestral hat").quantity(1).build()
                ))
                .build();

        // Utility / Unlocks lane
        Goal utility = Goal.builder()
                .description("Ladlor — Utility & Unlocks")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(GRACEFUL_HOOD).itemName("Graceful hood").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_TOP).itemName("Graceful top").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_LEGS).itemName("Graceful legs").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_GLOVES).itemName("Graceful gloves").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_BOOTS).itemName("Graceful boots").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_CAPE).itemName("Graceful cape").quantity(1).build(),
                        ItemTask.builder().itemId(BARROWS_GLOVES).itemName("Barrows gloves").quantity(1).build()
                ))
                .build();

        // Void set lane
        Goal voidSet = Goal.builder()
                .description("Ladlor — Void Set")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(VOID_KNIGHT_TOP).itemName("Void knight top").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_KNIGHT_ROBE).itemName("Void knight robe").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_KNIGHT_GLOVES).itemName("Void knight gloves").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_MAGE_HELM).itemName("Void mage helm").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_RANGER_HELM).itemName("Void ranger helm").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_MELEE_HELM).itemName("Void melee helm").quantity(1).build()
                ))
                .build();

        // God capes imbued lane
        Goal godCapes = Goal.builder()
                .description("Ladlor — Imbued God Capes")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(IMBUED_SARADOMIN_CAPE).itemName("Imbued Saradomin cape").quantity(1).build(),
                        ItemTask.builder().itemId(IMBUED_GUTHIX_CAPE).itemName("Imbued Guthix cape").quantity(1).build(),
                        ItemTask.builder().itemId(IMBUED_ZAMORAK_CAPE).itemName("Imbued Zamorak cape").quantity(1).build()
                ))
                .build();

        // Crystal / Bofa lane
        Goal crystal = Goal.builder()
                .description("Ladlor — Crystal & Bowfa")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(CRYSTAL_HELM).itemName("Crystal helm").quantity(1).build(),
                        ItemTask.builder().itemId(CRYSTAL_BODY).itemName("Crystal body").quantity(1).build(),
                        ItemTask.builder().itemId(CRYSTAL_LEGS).itemName("Crystal legs").quantity(1).build(),
                        ItemTask.builder().itemId(BOW_OF_FAERDHINEN).itemName("Bow of Faerdhinen").quantity(1).build()
                ))
                .build();

        // Jewelry / Boots upgrades lane
        Goal jewelry = Goal.builder()
                .description("Ladlor — Jewelry & Boots Upgrades")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(AMULET_OF_TORTURE).itemName("Amulet of torture").quantity(1).build(),
                        ItemTask.builder().itemId(NECKLACE_OF_ANGUISH).itemName("Necklace of anguish").quantity(1).build(),
                        ItemTask.builder().itemId(PEGASIAN_BOOTS).itemName("Pegasian boots").quantity(1).build(),
                        ItemTask.builder().itemId(RANGER_BOOTS).itemName("Ranger boots").quantity(1).build(),
                        ItemTask.builder().itemId(PRIMORDIAL_BOOTS).itemName("Primordial boots").quantity(1).build(),
                        ItemTask.builder().itemId(ETERNAL_BOOTS).itemName("Eternal boots").quantity(1).build()
                ))
                .build();

        // Slayer & Undead lane
        Goal slayerUndead = Goal.builder()
                .description("Ladlor — Slayer & Undead")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(BLACK_MASK).itemName("Black mask").quantity(1).build(),
                        ItemTask.builder().itemId(SLAYER_HELMET).itemName("Slayer helmet").quantity(1).build(),
                        ItemTask.builder().itemId(SLAYER_HELMET_I).itemName("Slayer helmet (i)").quantity(1).build(),
                        ItemTask.builder().itemId(SALVE_AMULET_E).itemName("Salve amulet (e)").quantity(1).build(),
                        ItemTask.builder().itemId(SALVE_AMULET_EI).itemName("Salve amulet (ei)").quantity(1).build(),
                        ItemTask.builder().itemId(IMBUED_HEART).itemName("Imbued heart").quantity(1).build(),
                        ItemTask.builder().itemId(RUNE_POUCH).itemName("Rune pouch").quantity(1).build(),
                        ItemTask.builder().itemId(RING_OF_SUFFERING).itemName("Ring of suffering").quantity(1).build(),
                        ItemTask.builder().itemId(RING_OF_SUFFERING_I).itemName("Ring of suffering (i)").quantity(1).build()
                ))
                .build();

        // Imbued Rings lane
        Goal imbuedRings = Goal.builder()
                .description("Ladlor — Imbued Rings")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(BERSERKER_RING_I).itemName("Berserker ring (i)").quantity(1).build(),
                        ItemTask.builder().itemId(ARCHERS_RING_I).itemName("Archer's ring (i)").quantity(1).build(),
                        ItemTask.builder().itemId(SEERS_RING_I).itemName("Seers ring (i)").quantity(1).build(),
                        ItemTask.builder().itemId(WARRIOR_RING_I).itemName("Warrior ring (i)").quantity(1).build()
                ))
                .build();

        // God Wars Dungeon Armor lane
        Goal gwdArmor = Goal.builder()
                .description("Ladlor — God Wars Armor")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(BANDOS_CHESTPLATE).itemName("Bandos chestplate").quantity(1).build(),
                        ItemTask.builder().itemId(BANDOS_TASSETS).itemName("Bandos tassets").quantity(1).build(),
                        ItemTask.builder().itemId(ARMADYL_CHESTPLATE).itemName("Armadyl chestplate").quantity(1).build(),
                        ItemTask.builder().itemId(ARMADYL_CHAINSKIRT).itemName("Armadyl chainskirt").quantity(1).build()
                ))
                .build();

        // Elite Void upgrades lane
        Goal eliteVoid = Goal.builder()
                .description("Ladlor — Elite Void")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(ELITE_VOID_TOP).itemName("Elite void top").quantity(1).build(),
                        ItemTask.builder().itemId(ELITE_VOID_ROBE).itemName("Elite void robe").quantity(1).build()
                ))
                .build();

        // Prayer scrolls lane (Rigour & Augury)
        Goal prayerScrolls = Goal.builder()
                .description("Ladlor — Prayer Scrolls")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(DEXTEROUS_PRAYER_SCROLL).itemName("Dexterous prayer scroll (Rigour)").quantity(1).build(),
                        ItemTask.builder().itemId(ARCANE_PRAYER_SCROLL).itemName("Arcane prayer scroll (Augury)").quantity(1).build()
                ))
                .build();

        // Raids uniques lane
        Goal raidsUniques = Goal.builder()
                .description("Ladlor — Raids Uniques")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(DRAGON_HUNTER_CROSSBOW).itemName("Dragon hunter crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(TWISTED_BOW).itemName("Twisted bow").quantity(1).build(),
                        ItemTask.builder().itemId(SCYTHE_OF_VITUR).itemName("Scythe of Vitur").quantity(1).build(),
                        ItemTask.builder().itemId(GHRAZI_RAPIER).itemName("Ghrazi rapier").quantity(1).build(),
                        ItemTask.builder().itemId(OSMUMTENS_FANG).itemName("Osmumten's fang").quantity(1).build(),
                        ItemTask.builder().itemId(MASORI_BODY).itemName("Masori body").quantity(1).build(),
                        ItemTask.builder().itemId(MASORI_CHAPS).itemName("Masori chaps").quantity(1).build(),
                        ItemTask.builder().itemId(TUMEKENS_SHADOW).itemName("Tumeken's shadow").quantity(1).build()
                ))
                .build();

        // Boss / cape milestones (quests mixed in)
        Goal milestones = Goal.builder()
                .description("Ladlor — Milestones & Capes")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(FIRE_CAPE).itemName("Fire cape").quantity(1).build(),
                        QuestTask.builder().quest(Quest.SONG_OF_THE_ELVES).build(),
                        QuestTask.builder().quest(Quest.SINS_OF_THE_FATHER).build()
                ))
                .build();

        List<Goal> goals = Arrays.asList(
                melee, ranged, magic, utility,
                voidSet, eliteVoid, godCapes, crystal, jewelry,
                slayerUndead, imbuedRings, gwdArmor, prayerScrolls, raidsUniques,
                milestones
        );
        return new Preset(
                "Ladlor Ironman Progression",
                "A multi-lane item-based progression inspired by Ladlor's Ironman chart.",
                goals
        );
    }
    private static Preset buildLadlorMelee() {
        Goal melee = Goal.builder()
                .description("Ladlor — Melee Progression")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(DRAGON_SCIMITAR).itemName("Dragon scimitar").quantity(1).build(),
                        ItemTask.builder().itemId(DRAGON_DEFENDER).itemName("Dragon defender").quantity(1).build(),
                        ItemTask.builder().itemId(FIGHTER_TORSO).itemName("Fighter torso").quantity(1).build(),
                        ItemTask.builder().itemId(DRAGON_BOOTS).itemName("Dragon boots").quantity(1).build(),
                        ItemTask.builder().itemId(ABYSSAL_WHIP).itemName("Abyssal whip").quantity(1).build(),
                        ItemTask.builder().itemId(BERSERKER_RING).itemName("Berserker ring").quantity(1).build(),
                        ItemTask.builder().itemId(FIRE_CAPE).itemName("Fire cape").quantity(1).build(),
                        ItemTask.builder().itemId(FEROCIOUS_GLOVES).itemName("Ferocious gloves").quantity(1).build(),
                        ItemTask.builder().itemId(ABYSSAL_TENTACLE).itemName("Abyssal tentacle").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Melee", "Melee progression lane from Ladlor chart.", Arrays.asList(melee));
    }

    private static Preset buildLadlorRanged() {
        Goal ranged = Goal.builder()
                .description("Ladlor — Ranged Progression")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(MAGIC_SHORTBOW).itemName("Magic shortbow").quantity(1).build(),
                        ItemTask.builder().itemId(RUNE_CROSSBOW).itemName("Rune crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(BLACK_DHIDE_BODY).itemName("Black d'hide body").quantity(1).build(),
                        ItemTask.builder().itemId(AVAS_ACCUMULATOR).itemName("Ava's accumulator").quantity(1).build(),
                        ItemTask.builder().itemId(KARILS_LEATHERTOP).itemName("Karil's leathertop").quantity(1).build(),
                        ItemTask.builder().itemId(KARILS_LEATHERSKIRT).itemName("Karil's leatherskirt").quantity(1).build(),
                        ItemTask.builder().itemId(TOXIC_BLOWPIPE).itemName("Toxic blowpipe").quantity(1).build(),
                        ItemTask.builder().itemId(ARMADYL_CROSSBOW).itemName("Armadyl crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(AVAS_ASSEMBLER).itemName("Ava's assembler").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Ranged", "Ranged progression lane from Ladlor chart.", Arrays.asList(ranged));
    }

    private static Preset buildLadlorMagic() {
        Goal magic = Goal.builder()
                .description("Ladlor — Magic Progression")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(MYSTIC_ROBE_TOP).itemName("Mystic robe top").quantity(1).build(),
                        ItemTask.builder().itemId(MYSTIC_ROBE_BOTTOM).itemName("Mystic robe bottom").quantity(1).build(),
                        ItemTask.builder().itemId(OCCULT_NECKLACE).itemName("Occult necklace").quantity(1).build(),
                        ItemTask.builder().itemId(MAGES_BOOK).itemName("Mage's book").quantity(1).build(),
                        ItemTask.builder().itemId(TRIDENT_OF_THE_SEAS).itemName("Trident of the seas").quantity(1).build(),
                        ItemTask.builder().itemId(UNCHARGED_TOXIC_TRIDENT).itemName("Trident of the swamp").quantity(1).build(),
                        ItemTask.builder().itemId(ANCESTRAL_ROBE_TOP).itemName("Ancestral robe top").quantity(1).build(),
                        ItemTask.builder().itemId(ANCESTRAL_ROBE_BOTTOM).itemName("Ancestral robe bottom").quantity(1).build(),
                        ItemTask.builder().itemId(ANCESTRAL_HAT).itemName("Ancestral hat").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Magic", "Magic progression lane from Ladlor chart.", Arrays.asList(magic));
    }

    private static Preset buildLadlorUtility() {
        Goal utility = Goal.builder()
                .description("Ladlor — Utility & Unlocks")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(GRACEFUL_HOOD).itemName("Graceful hood").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_TOP).itemName("Graceful top").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_LEGS).itemName("Graceful legs").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_GLOVES).itemName("Graceful gloves").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_BOOTS).itemName("Graceful boots").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_CAPE).itemName("Graceful cape").quantity(1).build(),
                        ItemTask.builder().itemId(BARROWS_GLOVES).itemName("Barrows gloves").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Utility & Unlocks", "Utility, movement and QoL lane from Ladlor chart.", Arrays.asList(utility));
    }

    private static Preset buildLadlorVoidSet() {
        Goal voidSet = Goal.builder()
                .description("Ladlor — Void Set")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(VOID_KNIGHT_TOP).itemName("Void knight top").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_KNIGHT_ROBE).itemName("Void knight robe").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_KNIGHT_GLOVES).itemName("Void knight gloves").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_MAGE_HELM).itemName("Void mage helm").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_RANGER_HELM).itemName("Void ranger helm").quantity(1).build(),
                        ItemTask.builder().itemId(VOID_MELEE_HELM).itemName("Void melee helm").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Void Set", "Standard Void set lane from Ladlor chart.", Arrays.asList(voidSet));
    }

    private static Preset buildLadlorGodCapes() {
        Goal godCapes = Goal.builder()
                .description("Ladlor — Imbued God Capes")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(IMBUED_SARADOMIN_CAPE).itemName("Imbued Saradomin cape").quantity(1).build(),
                        ItemTask.builder().itemId(IMBUED_GUTHIX_CAPE).itemName("Imbued Guthix cape").quantity(1).build(),
                        ItemTask.builder().itemId(IMBUED_ZAMORAK_CAPE).itemName("Imbued Zamorak cape").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Imbued God Capes", "Mage Arena II imbued god capes.", Arrays.asList(godCapes));
    }

    private static Preset buildLadlorCrystalBowfa() {
        Goal crystal = Goal.builder()
                .description("Ladlor — Crystal & Bowfa")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(CRYSTAL_HELM).itemName("Crystal helm").quantity(1).build(),
                        ItemTask.builder().itemId(CRYSTAL_BODY).itemName("Crystal body").quantity(1).build(),
                        ItemTask.builder().itemId(CRYSTAL_LEGS).itemName("Crystal legs").quantity(1).build(),
                        ItemTask.builder().itemId(BOW_OF_FAERDHINEN).itemName("Bow of Faerdhinen").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Crystal & Bowfa", "Crystal armour and Bow of Faerdhinen lane.", Arrays.asList(crystal));
    }

    private static Preset buildLadlorJewelryBoots() {
        Goal jewelry = Goal.builder()
                .description("Ladlor — Jewelry & Boots Upgrades")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(AMULET_OF_TORTURE).itemName("Amulet of torture").quantity(1).build(),
                        ItemTask.builder().itemId(NECKLACE_OF_ANGUISH).itemName("Necklace of anguish").quantity(1).build(),
                        ItemTask.builder().itemId(PEGASIAN_BOOTS).itemName("Pegasian boots").quantity(1).build(),
                        ItemTask.builder().itemId(RANGER_BOOTS).itemName("Ranger boots").quantity(1).build(),
                        ItemTask.builder().itemId(PRIMORDIAL_BOOTS).itemName("Primordial boots").quantity(1).build(),
                        ItemTask.builder().itemId(ETERNAL_BOOTS).itemName("Eternal boots").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Jewelry & Boots", "Jewelry and footwear upgrades lane.", Arrays.asList(jewelry));
    }

    private static Preset buildLadlorSlayerUndead() {
        Goal slayerUndead = Goal.builder()
                .description("Ladlor — Slayer & Undead")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(BLACK_MASK).itemName("Black mask").quantity(1).build(),
                        ItemTask.builder().itemId(SLAYER_HELMET).itemName("Slayer helmet").quantity(1).build(),
                        ItemTask.builder().itemId(SLAYER_HELMET_I).itemName("Slayer helmet (i)").quantity(1).build(),
                        ItemTask.builder().itemId(SALVE_AMULET_E).itemName("Salve amulet (e)").quantity(1).build(),
                        ItemTask.builder().itemId(SALVE_AMULET_EI).itemName("Salve amulet (ei)").quantity(1).build(),
                        ItemTask.builder().itemId(IMBUED_HEART).itemName("Imbued heart").quantity(1).build(),
                        ItemTask.builder().itemId(RUNE_POUCH).itemName("Rune pouch").quantity(1).build(),
                        ItemTask.builder().itemId(RING_OF_SUFFERING).itemName("Ring of suffering").quantity(1).build(),
                        ItemTask.builder().itemId(RING_OF_SUFFERING_I).itemName("Ring of suffering (i)").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Slayer & Undead", "Slayer and undead-focused unlocks.", Arrays.asList(slayerUndead));
    }

    private static Preset buildLadlorImbuedRings() {
        Goal imbuedRings = Goal.builder()
                .description("Ladlor — Imbued Rings")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(BERSERKER_RING_I).itemName("Berserker ring (i)").quantity(1).build(),
                        ItemTask.builder().itemId(ARCHERS_RING_I).itemName("Archer's ring (i)").quantity(1).build(),
                        ItemTask.builder().itemId(SEERS_RING_I).itemName("Seers ring (i)").quantity(1).build(),
                        ItemTask.builder().itemId(WARRIOR_RING_I).itemName("Warrior ring (i)").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Imbued Rings", "Imbued Dagannoth Kings rings.", Arrays.asList(imbuedRings));
    }

    private static Preset buildLadlorGwdArmor() {
        Goal gwdArmor = Goal.builder()
                .description("Ladlor — God Wars Armor")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(BANDOS_CHESTPLATE).itemName("Bandos chestplate").quantity(1).build(),
                        ItemTask.builder().itemId(BANDOS_TASSETS).itemName("Bandos tassets").quantity(1).build(),
                        ItemTask.builder().itemId(ARMADYL_CHESTPLATE).itemName("Armadyl chestplate").quantity(1).build(),
                        ItemTask.builder().itemId(ARMADYL_CHAINSKIRT).itemName("Armadyl chainskirt").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — God Wars Armor", "Bandos and Armadyl armour lane.", Arrays.asList(gwdArmor));
    }

    private static Preset buildLadlorEliteVoid() {
        Goal eliteVoid = Goal.builder()
                .description("Ladlor — Elite Void")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(ELITE_VOID_TOP).itemName("Elite void top").quantity(1).build(),
                        ItemTask.builder().itemId(ELITE_VOID_ROBE).itemName("Elite void robe").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Elite Void", "Elite void upgrade lane.", Arrays.asList(eliteVoid));
    }

    private static Preset buildLadlorPrayerScrolls() {
        Goal prayerScrolls = Goal.builder()
                .description("Ladlor — Prayer Scrolls")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(DEXTEROUS_PRAYER_SCROLL).itemName("Dexterous prayer scroll (Rigour)").quantity(1).build(),
                        ItemTask.builder().itemId(ARCANE_PRAYER_SCROLL).itemName("Arcane prayer scroll (Augury)").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Prayer Scrolls", "Rigour and Augury unlock scrolls.", Arrays.asList(prayerScrolls));
    }

    private static Preset buildLadlorRaidsUniques() {
        Goal raidsUniques = Goal.builder()
                .description("Ladlor — Raids Uniques")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(DRAGON_HUNTER_CROSSBOW).itemName("Dragon hunter crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(TWISTED_BOW).itemName("Twisted bow").quantity(1).build(),
                        ItemTask.builder().itemId(SCYTHE_OF_VITUR).itemName("Scythe of Vitur").quantity(1).build(),
                        ItemTask.builder().itemId(GHRAZI_RAPIER).itemName("Ghrazi rapier").quantity(1).build(),
                        ItemTask.builder().itemId(OSMUMTENS_FANG).itemName("Osmumten's fang").quantity(1).build(),
                        ItemTask.builder().itemId(MASORI_BODY).itemName("Masori body").quantity(1).build(),
                        ItemTask.builder().itemId(MASORI_CHAPS).itemName("Masori chaps").quantity(1).build(),
                        ItemTask.builder().itemId(TUMEKENS_SHADOW).itemName("Tumeken's shadow").quantity(1).build()
                ))
                .build();
        return new Preset("Ladlor — Raids Uniques", "Raids 1/2/3 uniques lane.", Arrays.asList(raidsUniques));
    }

    private static Preset buildLadlorMilestones() {
        Goal milestones = Goal.builder()
                .description("Ladlor — Milestones & Capes")
                .tasks(ReorderableList.from(
                        ItemTask.builder().itemId(FIRE_CAPE).itemName("Fire cape").quantity(1).build(),
                        QuestTask.builder().quest(Quest.SONG_OF_THE_ELVES).build(),
                        QuestTask.builder().quest(Quest.SINS_OF_THE_FATHER).build()
                ))
                .build();
        return new Preset("Ladlor — Milestones & Capes", "Key capes and late-game quest milestones.", Arrays.asList(milestones));
    }
    */

    private static Preset buildEarlyIronman() {
        Goal early = Goal.builder()
                .description("Early Ironman Progression")
                .tasks(ReorderableList.from(
                        // Core Early Skills (unlock movement & teleports)
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(30).build(),
                        SkillLevelTask.builder().skill(Skill.MAGIC).level(37).build(), // Falador/Camelot teles
                        SkillLevelTask.builder().skill(Skill.THIEVING).level(20).build(),
                        // Early set-up gear
                        ItemTask.builder().itemId(GRACEFUL_HOOD).itemName("Graceful hood").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_TOP).itemName("Graceful top").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_LEGS).itemName("Graceful legs").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_GLOVES).itemName("Graceful gloves").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_BOOTS).itemName("Graceful boots").quantity(1).build(),
                        ItemTask.builder().itemId(GRACEFUL_CAPE).itemName("Graceful cape").quantity(1).build(),
                        // Foundational quests for stats/unlocks
                        QuestTask.builder().quest(Quest.DRUIDIC_RITUAL).build(),
                        QuestTask.builder().quest(Quest.WATERFALL_QUEST).build(),
                        QuestTask.builder().quest(Quest.PRIEST_IN_PERIL).build(),
                        QuestTask.builder().quest(Quest.TREE_GNOME_VILLAGE).build(),
                        QuestTask.builder().quest(Quest.THE_GRAND_TREE).build(),
                        // Birdhouse runs (Fossil Island access)
                        QuestTask.builder().quest(Quest.THE_DIG_SITE).build(),
                        QuestTask.builder().quest(Quest.BONE_VOYAGE).build(),
                        SkillLevelTask.builder().skill(Skill.HUNTER).level(5).build(),
                        SkillLevelTask.builder().skill(Skill.CRAFTING).level(8).build(), // clockwork
                        SkillLevelTask.builder().skill(Skill.CONSTRUCTION).level(16).build(), // clockmaker's bench

                        // Seaweed runs (Giant seaweed patches on Fossil Island)
                        SkillLevelTask.builder().skill(Skill.FARMING).level(23).build()
                ))
                .build();
        return new Preset("Early Ironman Progression", "Stats, gear, and quest goals for early game Ironman.", Arrays.asList(early));
    }

    private static Preset buildMidIronman() {
        Goal mid = Goal.builder()
                .description("Mid Ironman Progression")
                .tasks(ReorderableList.from(
                        // Midgame Skill Targets
                        SkillLevelTask.builder().skill(Skill.ATTACK).level(60).build(),
                        SkillLevelTask.builder().skill(Skill.STRENGTH).level(60).build(),
                        SkillLevelTask.builder().skill(Skill.DEFENCE).level(60).build(),
                        SkillLevelTask.builder().skill(Skill.RANGED).level(60).build(),
                        SkillLevelTask.builder().skill(Skill.MAGIC).level(55).build(), // High Alch
                        SkillLevelTask.builder().skill(Skill.PRAYER).level(43).build(), // Protect prayers
                        SkillLevelTask.builder().skill(Skill.AGILITY).level(60).build(),
                        // Midgame Gear & Upgrades
                        ItemTask.builder().itemId(DRAGON_SCIMITAR).itemName("Dragon scimitar").quantity(1).build(),
                        ItemTask.builder().itemId(DRAGON_DEFENDER).itemName("Dragon defender").quantity(1).build(),
                        ItemTask.builder().itemId(FIGHTER_TORSO).itemName("Fighter torso").quantity(1).build(),
                        ItemTask.builder().itemId(RUNE_CROSSBOW).itemName("Rune crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(IBANS_STAFF_U).itemName("Iban's staff (u)").quantity(1).build(),
                        ItemTask.builder().itemId(BARROWS_GLOVES).itemName("Barrows gloves").quantity(1).build(),
                        ItemTask.builder().itemId(AVAS_ACCUMULATOR).itemName("Ava's accumulator").quantity(1).build(),
                        // Midgame Quests / Unlocks
                        QuestTask.builder().quest(Quest.RECIPE_FOR_DISASTER).build(),
                        QuestTask.builder().quest(Quest.FAIRYTALE_II__CURE_A_QUEEN).build(), // Fairy rings access (partial)
                        QuestTask.builder().quest(Quest.LUNAR_DIPLOMACY).build(),
                        QuestTask.builder().quest(Quest.ANIMAL_MAGNETISM).build(),
                        QuestTask.builder().quest(Quest.UNDERGROUND_PASS).build()
                ))
                .build();
        return new Preset("Mid Ironman Progression", "Stats, gear, and quest goals for mid game Ironman.", Arrays.asList(mid));
    }

    private static Preset buildLateIronman() {
        Goal late = Goal.builder()
                .description("Late Ironman Progression")
                .tasks(ReorderableList.from(
                        // Late Skill Targets
                        SkillLevelTask.builder().skill(Skill.ATTACK).level(85).build(),
                        SkillLevelTask.builder().skill(Skill.STRENGTH).level(85).build(),
                        SkillLevelTask.builder().skill(Skill.DEFENCE).level(85).build(),
                        SkillLevelTask.builder().skill(Skill.MAGIC).level(94).build(), // Vengeance/Ice Barrage
                        SkillLevelTask.builder().skill(Skill.PRAYER).level(77).build(), // Rigour/Augury
                        SkillLevelTask.builder().skill(Skill.RANGED).level(85).build(),
                        // Late Gear Goals
                        ItemTask.builder().itemId(ABYSSAL_TENTACLE).itemName("Abyssal tentacle").quantity(1).build(),
                        ItemTask.builder().itemId(TOXIC_BLOWPIPE).itemName("Toxic blowpipe").quantity(1).build(),
                        ItemTask.builder().itemId(ARMADYL_CROSSBOW).itemName("Armadyl crossbow").quantity(1).build(),
                        ItemTask.builder().itemId(BOW_OF_FAERDHINEN).itemName("Bow of Faerdhinen").quantity(1).build(),
                        ItemTask.builder().itemId(BANDOS_CHESTPLATE).itemName("Bandos chestplate").quantity(1).build(),
                        ItemTask.builder().itemId(BANDOS_TASSETS).itemName("Bandos tassets").quantity(1).build(),
                        ItemTask.builder().itemId(TRIDENT_OF_THE_SEAS).itemName("Trident of the seas").quantity(1).build(),
                        ItemTask.builder().itemId(RING_OF_SUFFERING).itemName("Ring of suffering").quantity(1).build(),
                        ItemTask.builder().itemId(RING_OF_SUFFERING_I).itemName("Ring of suffering (i)").quantity(1).build(),
                        ItemTask.builder().itemId(SALVE_AMULETEI).itemName("Salve amulet (ei)").quantity(1).build(),
                        ItemTask.builder().itemId(INFERNAL_CAPE).itemName("Infernal cape").quantity(1).build(),
                        // Late Quests / Diaries
                        QuestTask.builder().quest(Quest.SONG_OF_THE_ELVES).build(),
                        QuestTask.builder().quest(Quest.SINS_OF_THE_FATHER).build(),
                        QuestTask.builder().quest(Quest.MONKEY_MADNESS_II).build(),
                        QuestTask.builder().quest(Quest.DESERT_TREASURE_I).build()
                ))
                .build();
        return new Preset("Late Ironman Progression", "Stats, gear, and quest goals for late game Ironman.", Arrays.asList(late));
    }
}
