package org.game;

public class MonsterCard implements Card {
    private String name;
    private int attackValue;
    private String element;
    private String creatureType;
    private String cardType = "Monster";

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
