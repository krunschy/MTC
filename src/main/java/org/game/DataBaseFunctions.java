package org.game;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DataBaseFunctions {
    public Connection connect_to_db(){
        String url = "jdbc:postgresql://localhost:5432/MTCdatabase";
        String user = "postgres";
        String password = "admin";
        Connection connection =null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            // Perform database operations here
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return connection;
    }

    public void CreateUser(Connection conn, String username, String pass){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(pass.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                stringBuilder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }
            String hashedpw = stringBuilder.toString();
            Statement statement;
            try{
                String query=String.format("insert into users(username, password, money, elo, wins, losses) values('%s', '%s', 20, 100, 0, 0)", username, hashedpw);
                statement=conn.createStatement();
                statement.executeUpdate(query);
                System.out.println("User angelegt");
            }catch (Exception e){
                System.out.println(e);
            }
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception (e.g., log it, throw a runtime exception, etc.)
            System.out.println(e);
        }
    }

    public void LoginUser(Connection conn, String username, String pass){
        Statement statement;
        ResultSet rs =null;
        String DBpass = null;
        try{
            String query=String.format("select * from users where username = '%s'", username);
            statement=conn.createStatement();
            rs = statement.executeQuery(query);
            rs.next(); //sufficient cause there's only 1 such user
            DBpass = rs.getString("password");
            try{
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] hashedBytes = messageDigest.digest(pass.getBytes());
                StringBuilder stringBuilder = new StringBuilder();
                for (byte hashedByte : hashedBytes) {
                    stringBuilder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
                }
                String hashedpw = stringBuilder.toString();
                if(hashedpw.equals(DBpass)){
                    System.out.println("passwort richtig");
                }
                else{
                    System.out.println(DBpass);
                    System.out.println(hashedpw);
                }
            } catch (NoSuchAlgorithmException e) {
                // Handle the exception (e.g., log it, throw a runtime exception, etc.)
                System.out.println(e);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public String[] getUserData(Connection conn, String username){ //untested :3
        String query;
        Statement statement;
        ResultSet rs;
        String[] userinfo = new String[3];
        String tmpbio;
        String tmpimg;
        try {
            query = String.format("select * from users where username = '%s'", username);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()) {
                userinfo[0] = rs.getString("username");
                tmpbio = rs.getString("bio");
                userinfo[1] = (tmpbio == null) ? "none" : tmpbio; //Just wanted to avoid working with null
                tmpimg = rs.getString("image");
                userinfo[2] = (tmpimg == null) ? "none" : tmpimg;
            }
            else{
                throw new RuntimeException("User does not exist");
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());

        }catch (Exception e){
            System.out.println(e);
        }
        return userinfo;
    }

    public String[] getUserStats(Connection conn, String username){ //untested :3
        String query;
        Statement statement;
        ResultSet rs;
        String[] userinfo = new String[5];
        try {
            query = String.format("select * from users where username = '%s'", username);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()) { //hier sind die meisten sachen in der db eigentlich ints, aber is egal, weil wir müssen damit nicht rechnen und getString typecasted es auf strings
                userinfo[0] = rs.getString("username");
                userinfo[1] = rs.getString("elo");
                userinfo[2] = rs.getString("wins");
                userinfo[3] = rs.getString("losses");
                userinfo[4] = rs.getString("money");
            }
            else{
                throw new RuntimeException("User does not exist");
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());
        }catch (Exception e){
            System.out.println(e);
        }
        return userinfo;
    }

    public void UpdateUserData(Connection conn, String username, String bio, String image){ // untested :3
        String query;
        Statement statement;
        try {
            query = String.format("UPDATE users SET image = '%s', bio ='%s' WHERE username = '%s'", image, bio, username);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("User Data updated");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void CreateCard(Connection conn, String CardID, String Name, int Dmg){
        //zuerst checken ob die Karte schon da is
        Statement statement;
        String query;
        ResultSet rs;
        try {
            query = String.format("select * from cards where cardid = '%s'", CardID);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()) { //case: karte gibts schon
                throw new RuntimeException("Card exists already");
            }
            //dann mal den namen parsen
            String creaturetypeOrSpell;
            String Element = null;
            String CardType = null;
            String CreatureType = null;
            int firstUpperCaseIndex = -1;
            for (int i = 1; i < Name.length(); i++) { //der erste buchstabe is fix groß, drum schauen wir obs dannach noch einen gibt und falls ja, wissen wir wo der name gesplitet gehoert
                char currentChar = Name.charAt(i);
                if (Character.isUpperCase(currentChar)) {
                    firstUpperCaseIndex = i;
                    break;
                }
            }
            if(firstUpperCaseIndex == -1){
                creaturetypeOrSpell = Name;
            }
            else{ //also wenn wir nen großbuchstaben gefunden haben, wird aufgeteilt
                Element = Name.substring(0, firstUpperCaseIndex);
                creaturetypeOrSpell = Name.substring(firstUpperCaseIndex);
            }
            if(creaturetypeOrSpell.equals("Spell")){
                CardType = "Spell";
            }
            else{
                CardType = "Monster";
                CreatureType = creaturetypeOrSpell;
            }
            //alle 4 Fälle, je nachdem welche felder null sind
            if (CreatureType == null) {
                if (Element == null) {
                    query = String.format("INSERT INTO cards(cardid, element, cardtype, name, damage, creatureType) VALUES('%s', NULL, '%s', '%s', %d, NULL)", CardID, CardType, Name, Dmg);
                } else {
                    query = String.format("INSERT INTO cards(cardid, element, cardtype, name, damage, creatureType) VALUES('%s', '%s', '%s', '%s', %d, NULL)", CardID, Element, CardType, Name, Dmg);
                }
            } else {
                if (Element == null) {
                    query = String.format("INSERT INTO cards(cardid, element, cardtype, name, damage, creatureType) VALUES('%s', NULL, '%s', '%s', %d, '%s')", CardID, CardType, Name, Dmg, CreatureType);
                } else {
                    query = String.format("INSERT INTO cards(cardid, element, cardtype, name, damage, creatureType) VALUES('%s', '%s', '%s', '%s', %d, '%s')", CardID, Element, CardType, Name, Dmg, CreatureType);
                }
            }
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Karte angelegt");
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void createPackage(Connection conn, String CardID1, String Name1, int Dmg1,
                              String CardID2, String Name2, int Dmg2,
                              String CardID3, String Name3, int Dmg3,
                              String CardID4, String Name4, int Dmg4){
        //wenn der admin ein pack erstellt, schickt er auch gleich alle infos zu den Karten selbst mit, also muessen sie angelegt werden
        CreateCard(conn, CardID1, Name1, Dmg1);
        CreateCard(conn, CardID2, Name2, Dmg2);
        CreateCard(conn, CardID3, Name3, Dmg3);
        CreateCard(conn, CardID4, Name4, Dmg4);
            Statement statement;
            try{
                String query=String.format("insert into packages(card1, card2, card3, card4) values('%s', '%s', '%s', '%s')", CardID1, CardID2, CardID3, CardID4);
                statement=conn.createStatement();
                statement.executeUpdate(query);
                System.out.println("Package angelegt");
            }catch (Exception e){
                System.out.println(e);
            }
    }
    public void buyPackage(Connection conn, String username){
        Statement statement;
        ResultSet rs;
        String query;
        int money;
        try {
            query = String.format("select * from users where username = '%s'", username);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            rs.next(); //sufficient cause there's only 1 such user
            money = rs.getInt("money");
        if(money<5){
            throw new RuntimeException("Insufficient funds to purchase any packages");
        }
            //nun kriegt man das Package, also wir holen uns die 4 CardIDs
            query = String.format("SELECT * FROM packages ORDER BY packageID LIMIT 1;");
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()) { //rsnext is exactly the row we need, but if it doesnt exist, there were no packages left in the shop
                String CardID1 = rs.getString("Card1");
                String CardID2 = rs.getString("Card2");
                String CardID3 = rs.getString("Card3");
                String CardID4 = rs.getString("Card4");
                //nun loeschen wir das Package
                query = String.format("DELETE FROM packages WHERE packageID = (SELECT packageID FROM packages ORDER BY packageID LIMIT 1);");
                statement = conn.createStatement();
                statement.executeUpdate(query);
                //nun müssen wir die geöffneten Karten in unseren Stack hauen
                query = String.format("insert into stack(username, cardid, isintradeid, isindeck) values('%s', '%s', NULL, FALSE)", username, CardID1);
                statement = conn.createStatement();
                statement.executeUpdate(query);
                System.out.println("card1 ist in der sammlung");

                query = String.format("insert into stack(username, cardid, isintradeid, isindeck) values('%s', '%s', NULL, FALSE)", username, CardID2);
                statement = conn.createStatement();
                statement.executeUpdate(query);
                System.out.println("card2 ist in der sammlung");

                query = String.format("insert into stack(username, cardid, isintradeid, isindeck) values('%s', '%s', NULL, FALSE)", username, CardID3);
                statement = conn.createStatement();
                statement.executeUpdate(query);
                System.out.println("card3 ist in der sammlung");

                query = String.format("insert into stack(username, cardid, isintradeid, isindeck) values('%s', '%s', NULL, FALSE)", username, CardID4);
                statement = conn.createStatement();
                statement.executeUpdate(query);
                System.out.println("card4 ist in der sammlung");

                //zuletzt wird das geld abgezogen
                int MoneyNew = money - 5;
                query = String.format("UPDATE users SET money = '%d' WHERE username = '%s'", MoneyNew, username);
                statement = conn.createStatement();
                statement.executeUpdate(query);
            }
            else
                throw new RuntimeException("Insufficient packages in the shop");

        }
        catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());

        }catch (Exception e){
        System.out.println(e);
        }
    }

    public List<String> getStack(Connection conn, String username) { //untested :3
        String query;
        Statement statement;
        ResultSet rs;
        List<String> cardIDs = new ArrayList<>();
        try {
            query = String.format("select * from stack where username = '%s'", username);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                String cardID = rs.getString("cardid");
                cardIDs.add(cardID);
            }
            }catch (Exception e){
        System.out.println(e);
        }
        return cardIDs;
    }

    public void createDeck(Connection conn, String username, String CardID1, String CardID2, String CardID3, String CardID4){ //untested :3
        String query;
        Statement statement;
        ResultSet rs;
        //zuerst schauen wir, ob die karten getauscht werden duerfen, also sie weder part von decks noch bestehenden trades sind und wir sie überhaupt besitzen
        ArrayList<String> cardList = new ArrayList<>(Arrays.asList(CardID1, CardID2, CardID3, CardID4));
        for(String cardid : cardList) {
            try {
                query = String.format("select * from stack where username = '%s' and cardid = '%s", username, cardid);
                statement = conn.createStatement();
                rs = statement.executeQuery(query);
                if (!rs.next()) { //must be exactly one result because cardid is the keyvalue of stack
                    throw new RuntimeException("A Card can't be put in the deck, because you don't own it.");
                }
                if (rs.getString("isintrade") != null) {
                    throw new RuntimeException("A Card can't be put in the deck, because it's part of another Trade already!");
                }
            } catch (RuntimeException e) {
                System.out.println("Caught exception: " + e.getMessage());
                return;
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
        }
        //Und dann updaten wir den stack und setzn bei alle diesen Karten isindeck auf true
        for(String cardid : cardList) {
            try {
                query = String.format("update stack set isindeck = TRUE where username = '%s' and cardid = '%s", username, cardid);
                statement = conn.createStatement();
                rs = statement.executeQuery(query);
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
        }
    }

    public List<String> getDeck(Connection conn, String username) { //untested :3, should return empty list in case of no deck
        String query;
        Statement statement;
        ResultSet rs;
        List<String> cardIDs = new ArrayList<>();
        try {
            query = String.format("select * from stack where username = '%s' AND isindeck = TRUE", username);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                String cardID = rs.getString("cardid");
                cardIDs.add(cardID);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return cardIDs;
    }

    public String[] getCardInfo(Connection conn, String cardID){ //untested :3
        String query;
        Statement statement;
        ResultSet rs;
        String[] cardInfo = new String[5];
        String tmpelement;
        String tmpcreaturetype;
        try {
            query = String.format("select * from cards where cardid = '%s'", cardID);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(rs.next()) {
                cardInfo[0] = rs.getString("name");
                cardInfo[1] = rs.getString("cardtype");
                cardInfo[2] = rs.getString("damage");
                tmpelement = rs.getString("element");
                cardInfo[3] = (tmpelement == null) ? "none" : tmpelement; //this could be any string that isnt Normal, Fire or Water as these are the only ones that do anything. Just wanted to avoid working with null
                tmpcreaturetype = rs.getString("creaturetype");
                cardInfo[4] = (tmpcreaturetype == null) ? "none" : tmpcreaturetype;
            }
            else{
                throw new RuntimeException("Card does not exist");
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());

        }catch (Exception e){
            System.out.println(e);
        }
        return cardInfo;
    }

    public void DatabaseBattle(Connection conn, String username1, String username2){ //untested :3
        String query;
        Statement statement;
        ResultSet rs;
        //hol von beiden die karten aus den decks
        List<String> deckUser1 = getDeck(conn, username1);
        List<String> deckUser2 = getDeck(conn, username2);
        //hol für jede karte die daten aus der db
        List<Card> ClassDeckUser1 = new ArrayList<>();
        List<Card> ClassDeckUser2 = new ArrayList<>();
        MonsterCard tmpM;
        SpellCard tmpS;
        //Die Karten von user1 in die Karten Liste einlesen
        for(int i=0; i<4; i++){
            String[] tmpcard = getCardInfo(conn, deckUser1.get(i)); //gets us a string array of the data
            if(tmpcard[1].equals("Monster")){
                tmpM = new MonsterCard(Integer.parseInt(tmpcard[2]), tmpcard[3], tmpcard[4]);
                ClassDeckUser1.set(i, tmpM);
            }
            else{
                tmpS = new SpellCard(Integer.parseInt(tmpcard[2]), tmpcard[3]);
                ClassDeckUser1.set(i, tmpS);
            }
        }
        //und nochmal für user 2
        for(int j=0; j<4; j++){
            String[] tmpcard = getCardInfo(conn, deckUser2.get(j)); //gets us a string array of the data
            if(tmpcard[1].equals("Monster")){
                tmpM = new MonsterCard(Integer.parseInt(tmpcard[2]), tmpcard[3], tmpcard[4]);
                ClassDeckUser2.set(j, tmpM);
            }
            else{
                tmpS = new SpellCard(Integer.parseInt(tmpcard[2]), tmpcard[3]);
                ClassDeckUser2.set(j, tmpS);
            }
        }
        //daraus decks bauen
        Deck Deck1 = new Deck(username1, ClassDeckUser1.get(0) , ClassDeckUser1.get(1), ClassDeckUser1.get(2), ClassDeckUser1.get(3));
        Deck Deck2 = new Deck(username2, ClassDeckUser2.get(0) , ClassDeckUser2.get(1), ClassDeckUser2.get(2), ClassDeckUser2.get(3));
        //battle
        Battle battle = new Battle(Deck1, Deck2);
        String winner = battle.determineWinner(); //contains a string with the winners name
        String loser;
        if(username1.equals(winner)){
            loser = username2;
        }
        else{
            loser = username1;
        }

        System.out.println(battle.GetBattlelog());
        try {
            int Winnermoney;
            int Winnerelo;
            int Winnerwins;
            int Loserlosses;
            int Loserelo;
            //get winner stats and update locally
            query = String.format("select * from users where username = '%s'", winner);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            rs.next(); //sufficient cause there's only 1 such user
            Winnermoney = rs.getInt("money");
            Winnerelo = rs.getInt("elo");
            Winnerwins = rs.getInt("wins");
            Winnermoney = Winnermoney + 5;
            Winnerelo = Winnerelo + 3;
            ++Winnerwins;
            //get loser stats and update locally
            query = String.format("select * from users where username = '%s'", loser);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            rs.next(); //sufficient cause there's only 1 such user
            Loserelo = rs.getInt("elo");
            Loserlosses = rs.getInt("losses");
            Loserelo = Loserelo -5;
            ++Loserlosses;
            //update the db for the winner
            query = String.format("UPDATE users SET money = '%d', elo = '%d', wins = '%d' WHERE username = '%s'", Winnermoney, Winnerelo, Winnerwins, winner);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            //and the loser
            query = String.format("UPDATE users SET elo = '%d', losses = '%d' WHERE username = '%s'", Loserelo, Loserlosses, loser);
            statement = conn.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
        System.out.println(e);
        }
    }


    public void MakeTradeOffer(Connection conn, String username, String tradeid, String cardid,  String reqType, int reqDMG, String reqEle){ //untested :3
        String query;
        Statement statement;
        ResultSet rs;
        //reqType existiert immer, von reqEle oder reqDmg darf es nur genau einen geben.
        //java hat zwar keine default parameter, aber in dieser methode wird einfach davon ausgegangen, dass man reqDMG = 0 und reqEle = "none" kriegt,
        // wenn man die requirements nicht braucht       accept trade wird einfach damit verträglich geschrieben und gut is

        //zuerst schauen wir, ob die karte getauscht werden darf, also sie weder part eines decks noch bestehenden trades ist und wir sie überhaupt besitzen
        try {
            query = String.format("select * from stack where username = '%s' and cardid = '%s", username, cardid);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            if(!rs.next()){
                throw new RuntimeException("Card can't be put up for Trade, because you don't own it.");
            }
            if(rs.getBoolean("isindeck")){
                throw new RuntimeException("Card can't be put up for Trade, because it's part of your deck!");
            }
            if(rs.getString("isintrade") != null){
                throw new RuntimeException("Card can't be put up for Trade, because it's part of another Trade already!");
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());
            return;
        }catch (Exception e){
            System.out.println(e);
            return;
        }
        //erstelle einen trade mit card id als trade id und den passenden feldern
        try {
            query = String.format("insert into trades (tradeid, cardid, reqdamage, reqelement, reqtyp) values('%s', '%s','%d', '%s', '%s')", tradeid, cardid, reqDMG, reqEle, reqType);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            query = String.format("UPDATE stack SET isintradeid = '%s' WHERE cardid = '%s'", tradeid, cardid);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Trade is in DB");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /*public List<String[]> browseTrades(Connection conn, String username){
        //select from stack where trade is nicht null und username ist nicht deiner
        //für alle diese tradeIDs geh in den trade table und lies alle felder aus und speicher sie in einem string array aus der liste
    }
    public void chooseTrade(Connection conn, String username, String TradeID, String cardid){
        //check ob die karte in einem deck ist
        //check ob die karte den anforderungen entspricht
        //falls alles passt: trade löschen, karten username im stack überschreiben und trade id auf null im stack setzen
    }
    public void deleteTrade(Connection conn, String TradeID){}
    public void getScoreboard(Connection conn){}*/
}
