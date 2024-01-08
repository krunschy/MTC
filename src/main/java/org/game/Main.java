package org.game;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DataBaseFunctions db = new DataBaseFunctions();
        Connection conn=db.connect_to_db();
        //db.CreateUser(conn, "hubert1", "123");
        //db.LoginUser(conn, "hubert1","123");
        //db.LoginUser(conn, "hubert1","1223");
        /*db.createPackage(conn,
                "CardID1", "FireDragon", 50,
                "CardID2",  "WaterSpell",  20,
                "CardID3",  "Elf",  25,
                "CardID4",  "RegularKraken",  40);*/
        db.buyPackage(conn, "hubert1");
        /*MonsterCard fireelf = new MonsterCard( 20, "Fire", "Elf");
        MonsterCard waterdragon = new MonsterCard( 50, "Water", "Dragon");
        SpellCard normalspell = new SpellCard(50, "Normal");
        SpellCard Firespell = new SpellCard(70, "Fire");

        MonsterCard Wfireelf = new MonsterCard( 12, "Fire", "Elf");
        MonsterCard Wwaterdragon = new MonsterCard( 40, "Water", "Dragon");
        SpellCard Wnormalspell = new SpellCard(30, "Normal");
        SpellCard WFirespell = new SpellCard(35, "Fire");

        Deck firstdeck = new Deck("Huso", fireelf, waterdragon, normalspell, Firespell);
        Deck seconddeck = new Deck("basedboi", Wfireelf, Wwaterdragon, Wnormalspell, WFirespell);
        Battle test = new Battle(firstdeck, seconddeck);
        System.out.println(test.determineWinner());
        System.out.println(test.GetBattlelog());*/
    }
}