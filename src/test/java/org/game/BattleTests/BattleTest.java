package org.game.BattleTests;

import org.game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {
    @Test
    void testTrivialDraw() {
            Card card = new MonsterCard(15, "Fire", "Kraken");
            Deck deck1 = new Deck("username1", card, card, card, card);
            Deck deck2 = new Deck("username2", card, card, card, card);
            Battle testbattle = new Battle(deck1, deck2);
            assertEquals("draw",testbattle.determineWinner());
    }

    @Test
    void testTrivialWin() {
        Card card = new MonsterCard(10, "Fire", "Kraken");
        Card badcard = new SpellCard(0, "Fire");
        Deck deck1 = new Deck("username1", card, card, card, card);
        Deck deck2 = new Deck("username2", badcard, badcard, badcard, badcard);
        Battle testbattle = new Battle(deck1, deck2);
        assertEquals("username1",testbattle.determineWinner());
    }
}
