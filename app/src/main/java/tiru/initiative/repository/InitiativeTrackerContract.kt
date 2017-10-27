package tiru.initiative.repository

import android.provider.BaseColumns

class InitiativeTrackerContract {

    class EncounterEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Encounter"
            val NAME = "name"
        }
    }

    class EncounterDraftEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "EncounterDraft"
            val NAME = "name"
            val ENCOUNTER_ID = "encounterId"
        }
    }

    class CreatureEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Creature"
            val NAME = "name"
            val SIZE = "size"
            val TYPE = "type"
            val SUBTYPE = "subtype"
            val ALIGNMENT = "alignment"
            val STRENGTH = "strength"
            val DEXTERITY = "dexterity"
            val CONSTITUTION = "constitution"
            val INTELLIGENCE = "intelligence"
            val WISDOM = "wisdom"
            val CHARISMA = "charisma"
            val CHALLENGE_RATING = "challengeRating"
            val CUSTOM = "custom"
        }
    }

    class EncounterCreatureEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "EncounterCreature"
            val CREATURE_ID = "creatureId"
            val ENCOUNTER_ID = "encounterId"
        }
    }

    class EncounterCreatureDraftEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "EncounterCreatureDraft"
            val CREATURE_ID = "creatureId"
            val ENCOUNTER_ID = "encounterId"
        }
    }

    class SavingThrowEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "SavingThrow"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
            val MODIFIER = "modifier"
        }
    }

    class SkillEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Skill"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
            val MODIFIER = "modifier"
        }
    }

    class DamageVulnerabilityEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "DamageVulnerability"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
        }
    }

    class DamageResistanceEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "DamageResistance"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
        }
    }

    class DamageImmunityEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "DamageImmunity"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
        }
    }

    class ConditionImmunityEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "ConditionImmunity"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
        }
    }

    class SpeedEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Speed"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
        }
    }

    class ArmorClassEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "ArmorClass"
            val CREATURE_ID = "creatureId"
            val VALUE = "value"
            val TYPE = "type"
        }
    }

    class HitPointEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "HitPoint"
            val CREATURE_ID = "creatureId"
            val VALUE = "value"
            val HIT_DICE = "hitDice"
        }
    }

    class SenseEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Sense"
            val CREATURE_ID = "creatureId"
            val DESCRIPTION = "description"
        }
    }

    class LanguageEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Language"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
        }
    }

    class ActionEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "Action"
            val CREATURE_ID = "creatureId"
            val NAME = "name"
            val DESCRIPTION = "description"
            val ATTACK_BONUS = "attackBonus"
            val DAMAGE_DICE = "damageDice"
            val DAMAGE_BONUS = "damageBonus"
            val ACTION_CATEGORY = "actionCategory"
        }
    }
}