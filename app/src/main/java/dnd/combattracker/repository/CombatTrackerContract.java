package dnd.combattracker.repository;


import android.provider.BaseColumns;

public final class CombatTrackerContract {

    private CombatTrackerContract() {
    }

    public static class EncounterEntry implements BaseColumns {
        public static final String TABLE_NAME = "Encounter";
        public static final String NAME = "name";
    }

    public static class CreatureEntry implements BaseColumns {
        public static final String TABLE_NAME = "Creature";
        public static final String NAME = "name";
        public static final String SIZE = "size";
        public static final String TYPE = "type";
        public static final String SUBTYPE = "subtype";
        public static final String ALIGNMENT = "alignment";
        public static final String ARMOR_CLASS = "armorClass";
        public static final String STRENGTH = "strength";
        public static final String DEXTERITY = "dexterity";
        public static final String CONSTITUTION = "constitution";
        public static final String INTELLIGENCE = "intelligence";
        public static final String WISDOM = "wisdom";
        public static final String CHARISMA = "charisma";
        public static final String CHALLENGE_RATING = "challengeRating";
    }

    public static class SavingThrowEntry implements BaseColumns {
        public static final String TABLE_NAME = "SavingThrow";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
        public static final String MODIFIER = "modifier";
    }

    public static class SkillEntry implements BaseColumns {
        public static final String TABLE_NAME = "Skill";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
        public static final String MODIFIER = "modifier";
    }

    public static class DamageVulnerabilityEntry implements BaseColumns {
        public static final String TABLE_NAME = "DamageVulnerability";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
    }

    public static class DamageResistanceEntry implements BaseColumns {
        public static final String TABLE_NAME = "DamageResistance";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
    }

    public static class DamageImmunityEntry implements BaseColumns {
        public static final String TABLE_NAME = "DamageImmunity";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
    }

    public static class ConditionImmunityEntry implements BaseColumns {
        public static final String TABLE_NAME = "ConditionImmunity";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
    }

    public static class SpeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Speed";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
    }

    public static class ArmorClassEntry implements BaseColumns {
        public static final String TABLE_NAME = "ArmorClass";
        public static final String CREATURE_ID = "creatureId";
        public static final String VALUE = "value";
        public static final String TYPE = "type";
    }

    public static class HitPointEntry implements BaseColumns {
        public static final String TABLE_NAME = "HitPoint";
        public static final String CREATURE_ID = "creatureId";
        public static final String VALUE = "value";
        public static final String HIT_DICE = "hitDice";
    }

    public static class SenseEntry implements BaseColumns {
        public static final String TABLE_NAME = "Sense";
        public static final String CREATURE_ID = "creatureId";
        public static final String DESCRIPTION = "description";
    }

    public static class LanguageEntry implements BaseColumns {
        public static final String TABLE_NAME = "Language";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
    }

    public static class ActionEntry implements BaseColumns {
        public static final String TABLE_NAME = "Action";
        public static final String CREATURE_ID = "creatureId";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String ATTACK_BONUS = "attackBonus";
        public static final String DAMAGE_DICE = "damageDice";
        public static final String DAMAGE_BONUS = "damageBonus";
        public static final String ACTION_CATEGORY = "actionCategory";
    }

}
