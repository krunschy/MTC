package org.game.BattleTests;

import org.game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DamageCalcTest {
    //teste ob water -> fire
    @Test
    void testElemental() {
        Card Water = new SpellCard(10, "Fire");
        Card Fire = new SpellCard(10, "Water");
        Fight testfight = new Fight(Water,Fire);
        testfight.Winner();
        assertEquals("%s: FireSpell (10 Damage) vs %s: WaterSpell (10 Damage) => 10 VS 10 -> 5.0 VS 20.0 => WaterSpell wins\n",testfight.GetFightLog());
    }
    //teste ob orks wizards schaden machen können
    @Test
    void testOrkDamageWizard() {
        Card Orc = new MonsterCard(50, "Fire", "Orc");
        Card Wizard = new MonsterCard(10, "", "Wizard");
        Fight testfight = new Fight(Orc,Wizard);
        testfight.Winner();
        assertEquals("%s: FireOrc (50 Damage) vs %s: Wizard (10 Damage) => Wizard wins\n",testfight.GetFightLog());
    }
    //teste ob kraken gegen spells immun ist
    @Test
    void testKrakenImmuneSpells() {
        Card Kraken = new MonsterCard(10, "Fire", "Kraken");
        Card Spell = new SpellCard(100, "Regular");
        Fight testfight = new Fight(Kraken,Spell);
        testfight.Winner();
        assertEquals("%s: FireKraken (10 Damage) vs %s: RegularSpell (100 Damage) => 10 VS 100 -> 20.0 VS 0.0 => FireKraken wins\n",testfight.GetFightLog());
    }
}
