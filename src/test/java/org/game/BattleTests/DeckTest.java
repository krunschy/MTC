package org.game.BattleTests;

import org.game.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTest {
    @Test
    void testMonsterCard() {
        Card Wizard = new MonsterCard(10, "Fire", "Wizard");
        assertEquals("Fire", Wizard.getElement());
        assertEquals(10, Wizard.getAttackValue());
        assertEquals("Monster", Wizard.getCardType());
        assertEquals("Wizard", Wizard.getCreatureType());
    }

    @Test
    void testSpellCard() {
        Card Water = new SpellCard(10, "Fire");
        assertEquals("Fire", Water.getElement());
        assertEquals(10, Water.getAttackValue());
        assertEquals("Spell", Water.getCardType());
    }
    //teste wie gut es geht das dekc zu zerlegen mit removefirstcard
    @Test
    void testRemoveCard() {
        Card Water = new SpellCard(10, "Fire");
        Card Fire = new SpellCard(10, "Water");
        Card Orc = new MonsterCard(50, "Fire", "Orc");
        Card Wizard = new MonsterCard(10, "", "Wizard");
        Deck deck1 = new Deck("username1", Water, Fire , Orc , Wizard);
        assertEquals(Water, deck1.getFirstCard());
        deck1.removeFirstCard();
        assertEquals(Fire, deck1.getFirstCard());
    }
}
