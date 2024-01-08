package org.game;

interface Card {
    String getName();
    int getAttackValue();
    String getElement();
    String getCardType();
    default String getCreatureType() {
        throw new UnsupportedOperationException("This card type doesn't have a creature type.");
    }
}

// Monster Card class implementing the Card interface
class MonsterCard implements Card {
    private String name;
    private int attackValue;
    private String element;
    private String creatureType;
    private String cardType = "creature";

    // Constructor for MonsterCard
    public MonsterCard(int attackValue, String element, String creatureType) {
        this.name = element + creatureType;
        this.attackValue = attackValue;
        this.element = element;
        this.creatureType = creatureType;
    }

    // Getter methods
    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getAttackValue() {
        return attackValue;
    }
    @Override
    public String getElement() {
        return element;
    }
    @Override
    public String getCardType() {
        return cardType;
    }
    public String getCreatureType() {
        return creatureType;
    }
}

// Spell Card class implementing the Card interface
class SpellCard implements Card {
    private String name;
    private int attackValue;
    private String element;
    private String cardType = "spell";

    public SpellCard(int attackValue, String element) {
        this.name = element + "Spell";
        this.attackValue = attackValue;
        this.element = element;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getAttackValue() {
        return attackValue;
    }
    @Override
    public String getElement() {
        return element;
    }
    @Override
    public String getCardType() {
        return cardType;
    }
    // Additional methods specific to SpellCard can be added here
}