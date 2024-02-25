package org.game;

public interface Card {
    String getName();
    int getAttackValue();
    String getElement();
    String getCardType();
    default String getCreatureType() {
        throw new UnsupportedOperationException("This card type doesn't have a creature type.");
    }
}
