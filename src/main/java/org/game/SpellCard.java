package org.game;

public class SpellCard implements Card {
    private String name;
    private int attackValue;
    private String element;
    private String cardType = "Spell";

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
}