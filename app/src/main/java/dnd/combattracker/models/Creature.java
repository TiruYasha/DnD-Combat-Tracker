package dnd.combattracker.models;


import java.util.List;

public class Creature {
    private int id;
    private String name;
    private String source;
    private String type;
    private String description;
    private int initiative;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private String challenge;

    private String[] speed;
    private String[] damageVulnerabilities;
    private String[] damageResistances;
    private String[] damageImmunities;
    private String[] conditionImmunities;
    private String[] senses;
    private String[] languages;

    private List<Score> savingThrows;
    private List<Score> skills;
    private List<Action> specialAbilities;
    private List<Action> actions;
    private List<Action> legendaryActions;
    private List<Action> reactions;
}
