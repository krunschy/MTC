package org.game;
import org.game.httpserver.http.HttpStatus;
import org.game.DataBaseStringReturn;
import org.game.DataBaseListString;
import org.game.DataBaseListListString;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DataBaseFunctions {
    private final Connection conn;
    private static final DataBaseFunctions INSTANCE = new DataBaseFunctions();
    //wir managen die database über nen singleton
    public static DataBaseFunctions getInstance() {
        return INSTANCE;
    }

    public DataBaseFunctions(){
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
        conn = connection;
    }
    // veraltete CONNECTION
//    public Connection connect_to_db(){
//        String url = "jdbc:postgresql://localhost:5432/MTCdatabase";
//        String user = "postgres";
//        String password = "admin";
//        Connection connection =null;
//        try {
//            connection = DriverManager.getConnection(url, user, password);
//            System.out.println("Connected to the PostgreSQL server successfully.");
//            // Perform database operations here
//        } catch (SQLException e) {
//            System.err.println("Connection error: " + e.getMessage());
//        }
//        return connection;
//    }

    public HttpStatus CreateUser(String username, String pass){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(pass.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                stringBuilder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }
            String hashedpw = stringBuilder.toString();
            PreparedStatement statement;
            try{
                String query= "insert into users(username, password, money, elo, wins, losses) values(?, ? , 20, 100, 0, 0)";
                statement=conn.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, hashedpw);
                statement.executeUpdate();
                System.out.println("User angelegt");
            }catch (Exception e){
                System.out.println(e);
                return HttpStatus.CONFLICT;
            }
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception (e.g., log it, throw a runtime exception, etc.)
            System.out.println(e);
        }
        return HttpStatus.CREATED;
    }

    public HttpStatus LoginUser(String username, String pass){
        PreparedStatement statement;
        ResultSet rs =null;
        String DBpass = null;
        try{
            String query= "select * from users where username = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
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
                    return HttpStatus.OK;
                }
                else{
                    return HttpStatus.UNAUTHORIZED;
                }
            } catch (NoSuchAlgorithmException e) {
                // Handle the exception (e.g., log it, throw a runtime exception, etc.)
                System.out.println(e);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return HttpStatus.UNAUTHORIZED;
    }

    public DataBaseStringReturn getUserData(String username){
        DataBaseStringReturn value = new DataBaseStringReturn();
        String query;
        PreparedStatement statement;
        ResultSet rs;
        String[] userinfo = new String[5];
        String tmpbio;
        String tmpimg;
        String tmpelo;
        String tmpmon;
        try {
            query = "select * from users where username = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
            if(rs.next()) {
                userinfo[0] = rs.getString("username");
                tmpbio = rs.getString("bio");
                userinfo[1] = (tmpbio == null) ? "none" : tmpbio; //Just wanted to avoid working with null
                tmpimg = rs.getString("image");
                userinfo[2] = (tmpimg == null) ? "none" : tmpimg;
                tmpmon = rs.getString("money");
                userinfo[3] = (tmpmon == null) ? "none" : tmpmon; //Just wanted to avoid working with null
                tmpelo = rs.getString("elo");
                userinfo[4] = (tmpelo == null) ? "none" : tmpelo;
                value.setValue(userinfo);
                value.setHttp(HttpStatus.OK);
            }
            else{
                value.setHttp(HttpStatus.NOT_FOUND);
                throw new RuntimeException("User does not exist");
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());

        }catch (Exception e){
            System.out.println(e);
        }
        return value;
    }

    public DataBaseStringReturn getUserStats(String username){
        DataBaseStringReturn response = new DataBaseStringReturn();
        String query;
        PreparedStatement statement;
        ResultSet rs;
        String[] userinfo = new String[5];
        try {
            query = "select * from users where username = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
            if(rs.next()) { //hier sind die meisten sachen in der db eigentlich ints, aber is egal, weil wir müssen damit nicht rechnen und getString typecasted es auf strings
                userinfo[0] = rs.getString("name");
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
        response.setHttp(HttpStatus.OK);
        response.setValue(userinfo);
        return response;
    }

    public HttpStatus UpdateUserData(String username, String name, String bio, String image){
        String query;
        PreparedStatement statement;
        System.out.println("name:"+bio);
        try {
            query = "UPDATE users SET image = ?, bio = ?, name = ? WHERE username = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, image);
            statement.setString(2, bio);
            statement.setString(3, name);
            statement.setString(4, username);
            statement.executeUpdate();
            System.out.println("User Data updated");
            return HttpStatus.OK;
        }catch (Exception e){
            System.out.println(e);
            return HttpStatus.NOT_FOUND;
        }
    }

    public int CreateCard(String CardID, String Name, int Dmg){
        //zuerst checken ob die Karte schon da is
        PreparedStatement statement;
        String query;
        ResultSet rs;
        try {
            query = "select * from cards where cardid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, CardID);
            rs = statement.executeQuery();
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
            query = "INSERT INTO cards(cardid, element, cardtype, name, damage, creatureType) VALUES(?, ?, ?, ?, ?, ?)";
            statement=conn.prepareStatement(query);
            statement.setString(1, CardID);
            if (Element == null) {
                statement.setNull(2, java.sql.Types.VARCHAR);
            } else {
                statement.setString(2, Element);
            }
            statement.setString(3, CardType);
            statement.setString(4, Name);
            statement.setInt(5, Dmg);
            if (CreatureType == null) {
                statement.setNull(6, java.sql.Types.VARCHAR);
            } else {
                statement.setString(6, CreatureType);
            }
            statement.executeUpdate();
            System.out.println("Karte angelegt");
            return 0; //this means all is fine
        }
        catch(RuntimeException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println(e);
        }
        return 1; //this means something went wrong
    }

    public HttpStatus createPackage(String CardID1, String Name1, int Dmg1,
                              String CardID2, String Name2, int Dmg2,
                              String CardID3, String Name3, int Dmg3,
                              String CardID4, String Name4, int Dmg4,
                              String CardID5, String Name5, int Dmg5){
        //wenn der admin ein pack erstellt, schickt er auch gleich alle infos zu den Karten selbst mit, also muessen sie angelegt werden
        //CreateCard returned 0 wenn alles klappt und 1 falls was schief geht, zb es die karte schon gibt
        if(CreateCard(CardID1, Name1, Dmg1) +
        CreateCard(CardID2, Name2, Dmg2) +
        CreateCard(CardID3, Name3, Dmg3) +
        CreateCard(CardID4, Name4, Dmg4) +
        CreateCard(CardID5, Name5, Dmg5) > 0){ //also hier ist mind eine Karte fehlgeschlagen
            return HttpStatus.CONFLICT;
        }
        PreparedStatement statement;
            try{
                String query="insert into packages(card1, card2, card3, card4, card5) values(?, ?, ?, ?, ?)";
                statement=conn.prepareStatement(query);
                statement.setString(1, CardID1);
                statement.setString(2, CardID2);
                statement.setString(3, CardID3);
                statement.setString(4, CardID4);
                statement.setString(5, CardID5);
                statement.executeUpdate();
                System.out.println("Package angelegt");
                return HttpStatus.CREATED;
            }catch (Exception e){
                System.out.println(e);
            }
        return HttpStatus.CONFLICT;
    }
    public HttpStatus buyPackage(String username){
        PreparedStatement statement;
        ResultSet rs;
        String query;
        int money;
        HttpStatus response = HttpStatus.OK;
        try {
            query = "select * from users where username = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
            rs.next(); //sufficient cause there's only 1 such user
            money = rs.getInt("money");
        if(money<5){
            response = HttpStatus.FORBIDDEN;
            throw new RuntimeException("Insufficient funds to purchase any packages");
        }
            //nun kriegt man das Package, also wir holen uns die 5 CardIDs
            query = "SELECT * FROM packages ORDER BY packageID LIMIT 1;";
            statement=conn.prepareStatement(query);
            rs = statement.executeQuery();
            if(rs.next()) { //rsnext is exactly the row we need, but if it doesnt exist, there were no packages left in the shop

                String CardID1 = rs.getString("Card1");
                String CardID2 = rs.getString("Card2");
                String CardID3 = rs.getString("Card3");
                String CardID4 = rs.getString("Card4");
                String CardID5 = rs.getString("Card5");
                //nun loeschen wir das Package
                conn.setAutoCommit(false);
                query = "DELETE FROM packages WHERE packageID = (SELECT packageID FROM packages ORDER BY packageID LIMIT 1)";
                statement=conn.prepareStatement(query);
                statement.executeUpdate();
                //nun müssen wir die geöffneten Karten in unseren Stack hauen
                String[] cardIDs = {CardID1, CardID2, CardID3, CardID4, CardID5};
                query = "INSERT INTO stack (username, cardid, isintradeid, isindeck) VALUES (?, ?, NULL, FALSE)";
                for (int i = 0; i < cardIDs.length; i++) {
                    String cardID = cardIDs[i];
                    statement = conn.prepareStatement(query);
                    statement.setString(1, username);
                    statement.setString(2, cardID);
                    statement.executeUpdate();
                    System.out.println(cardID + " ist in der sammlung");
                }
                //zuletzt wird das geld abgezogen
                int MoneyNew = money - 5;
                query = "UPDATE users SET money = ? WHERE username = ?";
                statement = conn.prepareStatement(query);
                statement.setInt(1, MoneyNew);
                statement.setString(2, username);
                statement.executeUpdate();
                conn.commit();
            }
            else {
                response = HttpStatus.NOT_FOUND;
                throw new RuntimeException("No packages left in the shop");
            }
        }
        catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());

        }catch (Exception e){
        System.out.println(e);
        }
        return response;
    }

    public DataBaseListString getStack(String username) {
        DataBaseListString result = new DataBaseListString();
        String query;
        PreparedStatement statement;
        ResultSet rs;
        List<String> cardIDs = new ArrayList<>();
        try {
            query = "select * from stack join cards on stack.cardid = cards.cardid where username = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
            while (rs.next()) {
                String cardID = rs.getString("name");
                cardIDs.add(cardID);
            }
            }catch (Exception e){
        System.out.println(e);
        }
        if(cardIDs.isEmpty()){
            result.setHttp(HttpStatus.NO_CONTENT);
        }
        else{
            result.setHttp(HttpStatus.OK);
        }
        result.setValue(cardIDs);
        return result;
    }

    public HttpStatus createDeck(String username, String CardID1, String CardID2, String CardID3, String CardID4){
        String query;
        PreparedStatement statement;
        ResultSet rs;
        //zuerst schauen wir, ob die karten part von decks oder bestehenden trades sind und wir sie überhaupt besitzen
        ArrayList<String> cardList = new ArrayList<>(Arrays.asList(CardID1, CardID2, CardID3, CardID4));
        for(String cardid : cardList) {
            try {
                query = "select * from stack where username = ? and cardid = ?";
                statement=conn.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, cardid);
                rs = statement.executeQuery();
                if (!rs.next()) { //must be exactly one result because cardid is the keyvalue of stack
                    throw new RuntimeException("A Card can't be put in the deck, because you don't own it.");
                }
                if (rs.getString("isintradeid") != null) {
                    throw new RuntimeException("A Card can't be put in the deck, because it's part of a Trade!");
                }
            } catch (RuntimeException e) {
                System.out.println("Caught exception: " + e.getMessage());
                return HttpStatus.FORBIDDEN;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        //Zuerst updaten wir den stack und setzen alle karten auf "sind nicht in deck"
        try {
            query = "update stack set isindeck = FALSE where username = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        //Und dann updaten wir den stack und setzn bei alle diesen Karten isindeck auf true
        for(String cardid : cardList) {
            try {
                query = "update stack set isindeck = TRUE where username = ? and cardid = ?";
                statement=conn.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, cardid);
                statement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Deck angelegt");
        return HttpStatus.OK;
    }

    public DataBaseListString getDeck(String username) { //returns empty list in case of no deck
        DataBaseListString result = new DataBaseListString();
        String query;
        PreparedStatement statement;
        ResultSet rs;
        List<String> cardIDs = new ArrayList<>();
        try {
            query = "select * from stack join cards on stack.cardid = cards.cardid where username = ? AND isindeck = TRUE";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
            while (rs.next()) {
                String cardID = rs.getString("NAME");
                cardIDs.add(cardID);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        if(cardIDs.isEmpty()){
            result.setHttp(HttpStatus.NO_CONTENT);
        }
        else{
            result.setHttp(HttpStatus.OK);
        }
        result.setValue(cardIDs);
        return result;
    }

    public List<String> getDeckID(String username) { //returns empty list in case of no deck
        String query;
        PreparedStatement statement;
        ResultSet rs;
        List<String> cardIDs = new ArrayList<>();
        try {
            query = "select * from stack join cards on stack.cardid = cards.cardid where username = ? AND isindeck = TRUE";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
            while (rs.next()) {
                String cardID = rs.getString("cardid");
                cardIDs.add(cardID);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return cardIDs;
    }
    public List<String> getCardInfo(String cardID){
        String query;
        PreparedStatement statement;
        ResultSet rs;
        List<String> cardInfo = new ArrayList<>();
        String tmpelement;
        String tmpcreaturetype;
        try {
            query = "select * from cards where cardid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, cardID);
            rs = statement.executeQuery();
            if(rs.next()) {
                cardInfo.add(rs.getString("name"));
                cardInfo.add(rs.getString("cardtype"));
                cardInfo.add(rs.getString("damage"));
                tmpelement = rs.getString("element");
                cardInfo.add((tmpelement == null) ? "" :tmpelement); //any string that isnt Normal, Fire or Water  will do here, as these are the only ones that do anything. Just wanted to avoid working with null
                tmpcreaturetype = rs.getString("creaturetype");
                cardInfo.add((tmpcreaturetype == null) ? "" : tmpcreaturetype);
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

    public String DatabaseBattle(String username1, String username2){ //gibt keinen http zurück, weil lt api specification hier eh nur ok kommen kann
        String response ="";
        String query;
        PreparedStatement statement;
        ResultSet rs;
        //hol von beiden die karten aus den decks
        List<String> deckUser1 = getDeckID(username1);
        List<String> deckUser2 = getDeckID(username2);
        //hol für jede karte die daten aus der db
        List<Card> ClassDeckUser1 = new ArrayList<>();
        List<Card> ClassDeckUser2 = new ArrayList<>();
        MonsterCard tmpM;
        SpellCard tmpS;
        //Die Karten von user1 in die Karten Liste einlesen
        for(int i=0; i<4; i++){
            List<String> tmpcard = getCardInfo(deckUser1.get(i)); //gets us a string List of the data of a single card
            if(tmpcard.get(1).equals("Monster")){
                tmpM = new MonsterCard(Integer.parseInt(tmpcard.get(2)), tmpcard.get(3), tmpcard.get(4));
                ClassDeckUser1.add(tmpM);
            }
            else{
                tmpS = new SpellCard(Integer.parseInt(tmpcard.get(2)), tmpcard.get(3));
                ClassDeckUser1.add(tmpS);
            }
        }
        //und nochmal für user 2
        for(int j=0; j<4; j++){
            List<String> tmpcard = getCardInfo(deckUser2.get(j)); //gets us a string List of the data of a single card
            if(tmpcard.get(1).equals("Monster")){
                tmpM = new MonsterCard(Integer.parseInt(tmpcard.get(2)), tmpcard.get(3), tmpcard.get(4));
                ClassDeckUser2.add(tmpM);
            }
            else{
                tmpS = new SpellCard(Integer.parseInt(tmpcard.get(2)), tmpcard.get(3));
                ClassDeckUser2.add(tmpS);
            }
        }
        //daraus decks bauen
        Deck Deck1 = new Deck(username1, ClassDeckUser1.get(0) , ClassDeckUser1.get(1), ClassDeckUser1.get(2), ClassDeckUser1.get(3));
        Deck Deck2 = new Deck(username2, ClassDeckUser2.get(0) , ClassDeckUser2.get(1), ClassDeckUser2.get(2), ClassDeckUser2.get(3));
        //battle
        Battle battle = new Battle(Deck1, Deck2);
        String winner = battle.determineWinner(); //contains a string with the winners name
        String loser ="";
        String log = battle.GetBattlelog();
        Boolean draw = false;
        if(winner.equals("draw")) {
        draw = true;
        log += "\n The game ended in a draw as it exceeded 1000 rounds. The userstats won't change as a result.";
        }
        else {
            if (username1.equals(winner)) {
                loser = username2;
            } else {
                loser = username1;
            }
            log += "\n The winner is " + winner +"!";
        }
        response = log;
        System.out.println(log);
        if(!draw) {
            try {
                int Winnermoney;
                int Winnerelo;
                int Winnerwins;
                int Loserlosses;
                int Loserelo;
                //get winner stats and update locally
                query = "select * from users where username = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, winner);
                rs = statement.executeQuery();
                rs.next(); //sufficient cause there's only 1 such user
                Winnermoney = rs.getInt("money");
                Winnerelo = rs.getInt("elo");
                Winnerwins = rs.getInt("wins");
                Winnermoney = Winnermoney + 5;
                Winnerelo = Winnerelo + 5;
                ++Winnerwins;
                //get loser stats and update locally
                query = "select * from users where username = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, loser);
                rs = statement.executeQuery();
                rs.next(); //sufficient cause there's only 1 such user
                Loserelo = rs.getInt("elo");
                Loserlosses = rs.getInt("losses");
                Loserelo = Loserelo - 5;
                ++Loserlosses;
                //update the db for the winner
                query = "UPDATE users SET money = ?, elo = ?, wins = ? WHERE username = ?";
                statement = conn.prepareStatement(query);
                statement.setInt(1, Winnermoney);
                statement.setInt(2, Winnerelo);
                statement.setInt(3, Winnerwins);
                statement.setString(4, winner);
                statement.executeUpdate();
                //and the loser
                query = "UPDATE users SET elo = ?, wins = ? WHERE username = ?";
                statement = conn.prepareStatement(query);
                statement.setInt(1, Loserelo);
                statement.setInt(2, Loserlosses);
                statement.setString(3, loser);
                statement.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return response;
    }


    public HttpStatus createTradeOffer(String username, String TradeId , String cardid,  String reqType, int reqDMG, String reqEle){
        String query;
        PreparedStatement statement;
        ResultSet rs;
        //reqType existiert immer, von reqEle oder reqDmg darf es nur genau einen geben.
        //java hat zwar keine default parameter, aber in dieser methode wird einfach davon ausgegangen, dass man reqDMG = 0 und reqEle = "none" kriegt,
        // wenn man die requirements nicht braucht       accept trade wird einfach damit verträglich geschrieben und gut is

        //zuerst schauen wir, ob die karte getauscht werden darf, also sie weder part eines decks noch bestehenden trades ist und wir sie überhaupt besitzen
        try {
            query = "select * from stack where username = ? and cardid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, cardid);
            rs = statement.executeQuery();
            if(!rs.next()){
                throw new RuntimeException("Card can't be put up for Trade, because you don't own it.");
            }
            if(rs.getBoolean("isindeck")){
                throw new RuntimeException("Card can't be put up for Trade, because it's part of your deck!");
            }
            if(rs.getString("isintradeid") != null){
                throw new RuntimeException("Card can't be put up for Trade, because it's part of another Trade already!");
            }
            query = "select * from trades where tradeid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, TradeId);
            rs = statement.executeQuery();
            if(rs.next()){ //falls es die tradeID scho gibt
                return HttpStatus.CONFLICT;
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());
            return HttpStatus.FORBIDDEN;
        }catch (Exception e){
            System.out.println(e);
            return HttpStatus.FORBIDDEN; //abissl unsicher, welche der responses aus der api specification am meisten sinn macht, wenn die query failed, aber besser sowas als weitermachen
        }
        //erstelle einen trade mit card id als trade id und den passenden feldern
        try {
            conn.setAutoCommit(false);
            query = "insert into trades (tradeid, cardid, reqdamage, reqelement, reqtype) values(?, ?, ?, ?, ?)";
            statement=conn.prepareStatement(query);
            statement.setString(1, TradeId);
            statement.setString(2, cardid);
            statement.setInt(3, reqDMG);
            statement.setString(4, reqEle);
            statement.setString(5, reqType);
            statement.executeUpdate();
            query = "UPDATE stack SET isintradeid = ? WHERE cardid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, TradeId);
            statement.setString(2, cardid);
            statement.executeUpdate();
            conn.commit();
            System.out.println("Trade is in DB");
        }catch (Exception e){
            System.out.println(e);
        }
        return HttpStatus.CREATED;
    }

    public DataBaseListListString browseTrades(String username){
        String query;
        PreparedStatement statement;
        ResultSet rs;
        DataBaseListListString response = new DataBaseListListString();
        List<String> TradeIDs = new ArrayList<>();
        List<List<String>> AllTrades = new ArrayList<>();
        try {
            query = "select * from stack where username != ? and isintradeid IS NOT NULL"; //wählt alle trades von andern usern
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();
            while(rs.next()) {
                TradeIDs.add(rs.getString("isintradeid"));
            }
            for(String TradeID : TradeIDs) {
                List<String> DataSet = new ArrayList<>();
                query = "select reqdamage, reqelement, reqtype, element, cardtype, name, damage from trades join cards on trades.cardid = cards.cardid where tradeid= ?;";
                //die relevanten daten des trades
                statement=conn.prepareStatement(query);
                statement.setString(1, TradeID);
                rs = statement.executeQuery();
                rs.next();
                DataSet.add(rs.getString("reqdamage"));
                DataSet.add(rs.getString("reqelement"));
                DataSet.add(rs.getString("reqtype"));
                DataSet.add(rs.getString("element"));
                DataSet.add(rs.getString("cardtype"));
                DataSet.add(rs.getString("name"));
                DataSet.add(rs.getString("damage")); //wieder wird der int auf string gecasted, is aber wurscht
                AllTrades.add(DataSet);
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());

        }catch (Exception e){
            System.out.println(e);
        }
        response.setHttp(HttpStatus.OK);
        response.setValue(AllTrades);
        return response;
    }
    public HttpStatus commitToTrade(String username, String TradeID, String cardid){
        String query;
        PreparedStatement statement;
        ResultSet rs;
        //zuerst schauen wir, ob die karte getauscht werden darf, also sie weder part eines decks noch bestehenden trades ist und wir sie überhaupt besitzen
        //außerdem: wir dürfen sie auch nicht traden, wenns unser trade
        try {
            query = "select * from stack where username = ? and cardid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, cardid);
            rs = statement.executeQuery();
            if(!rs.next()){
                throw new RuntimeException("Card can't be put up for Trade, because you don't own it.");
            }
            if(rs.getBoolean("isindeck")){
                throw new RuntimeException("Card can't be traded, because it's part of your deck!");
            }
            if(rs.getString("isintradeid") != null){
                throw new RuntimeException("Card can't be traded, because it's part of a tradeoffer");
            }
            query = "select * from stack where username = ? and isintradeid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, TradeID);
            if(rs.next()){
                System.out.println("hier1");
                return HttpStatus.FORBIDDEN;
            }
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());
            return HttpStatus.FORBIDDEN;
        }catch (Exception e){
            System.out.println(e);
            return HttpStatus.FORBIDDEN;
        }
        //check ob die karte den anforderungen entspricht
        Integer reqDmg = 0;
        String reqelement = "";
        String reqtype = "";
        Integer Dmg = 0;
        String element = "";
        String type = "";
        String Trader = "";
        String TradeCardID = "";
        try {
            //die anforderungen des trades herausfinden
            query = "select reqdamage, reqelement, reqtype, cardid from trades where tradeid= ?;";
            statement = conn.prepareStatement(query);
            statement.setString(1, TradeID);
            rs = statement.executeQuery();
            if(!rs.next()){
                return HttpStatus.NOT_FOUND;
            }
            reqDmg = rs.getInt("reqdamage");
            reqelement = rs.getString("reqelement");
            reqtype = rs.getString("reqtype");
            TradeCardID = rs.getString("cardid");
            //die entsprechenden Daten der zu tauschenden Karte auslesen
            query = "select damage, element, cardtype from cards where cardid= ?;";
            statement = conn.prepareStatement(query);
            statement.setString(1, cardid);
            rs = statement.executeQuery();
            rs.next();
            Dmg = rs.getInt("damage");
            element = rs.getString("element");
            type = rs.getString("cardtype");
            //überprüfen ob der Trade legitim ist
                if(Dmg < reqDmg){
                    throw new RuntimeException("Illegal Trade: Your Card has too little Damage");
                }
                if(!reqelement.equals("none")){ //hier steht entweder das element drin oder "none" falls man keine Anforderungen hat
                    if(!reqelement.equalsIgnoreCase(element)) { //falls nicht none, wird geschaut ob man nen Element mismatch hat
                        throw new RuntimeException("Illegal Trade: Your Card has the wrong Element");
                    }
                }
                if(!reqtype.equalsIgnoreCase(type)){
                    throw new RuntimeException("Illegal Trade: Your Card has the wrong card type");
                }
            //tausch durchführen
            //zuerst müssen wir den namen herausfinden von dem user mit dem wir tauschen
            query = "select username from stack where isintradeid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, TradeID);
            rs = statement.executeQuery();
            rs.next();
            Trader = rs.getString("username");
            //zu tauschende Karte updaten (also die, die wir hergeben)
            query = "update stack set username = ? where cardid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, Trader); //neuer username ist vom dem Tauschanbieter
            statement.setString(2, cardid); //die card id ist von unserer karte
            statement.executeUpdate();
            //ertauschte Karte updaten (also die, die wir bekommen). Hier setzen wir isintradeid dabei gleich null, weil die Karten ja kein teil eines trades mehr sein soll
            query = "update stack set username = ?, isintradeid = NULL where cardid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, username); //neuer username ist von uns
            statement.setString(2, TradeCardID); //die card id ist die der ertauschten karten
            statement.executeUpdate();
            //eintrag aus trades table löschen
            query = "delete from trades where tradeid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, TradeID);
            statement.executeUpdate();
        }catch(RuntimeException e){
            System.out.println("Caught exception: " + e.getMessage());
            return HttpStatus.FORBIDDEN;
        }catch (Exception e){
            System.out.println(e);
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.OK;
    }
    public HttpStatus deleteTrade(String username, String TradeID){
        String query;
        PreparedStatement statement;
        ResultSet rs;
        //zuerst schauen wir, ob der trade überhaupt vom user ist
        try {
            query = "select * from stack where isintradeid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, TradeID);
            rs = statement.executeQuery();
            if(!rs.next()){
                return HttpStatus.NOT_FOUND;
            }
            String retrievedUsername = rs.getString("username");
            if (!retrievedUsername.equals(username)) {
                return HttpStatus.FORBIDDEN;
            }
        }catch (Exception e){
            System.out.println(e);
            return HttpStatus.FORBIDDEN;
        }
        //da der user so einen trade hat, werden wir ihn nun zuerst aus dem stack table und dann aus dem trades table löschen
        try {
            conn.setAutoCommit(false);
            query = "update stack set isintradeid = NULL where isintradeid = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, TradeID);
            statement.executeUpdate();
            //und nun löschen wir den trade aus dem trades table
            query = "delete from trades where tradeid = ?";
            statement=conn.prepareStatement(query);
            statement.setString(1, TradeID);
            statement.executeUpdate();
            conn.commit();
        }catch (Exception e){
            System.out.println(e);
        }
        return HttpStatus.OK;
    }

    public DataBaseListListString getScoreboard(){
    String query;
    PreparedStatement statement;
    ResultSet rs;
    DataBaseListListString response = new DataBaseListListString();
    List<List<String>> Scoreboard = new ArrayList<>();
        try {
        query = "select * from users order by elo desc"; //eine liste aller user, nach elo sortiert (das meiste zuerst)
        statement=conn.prepareStatement(query);
        rs = statement.executeQuery();
        while(rs.next()) {
            List<String> DataSet = new ArrayList<>();
            DataSet.add(rs.getString("username"));
            DataSet.add(rs.getString("elo"));
            DataSet.add(rs.getString("wins"));
            DataSet.add(rs.getString("losses"));
            Scoreboard.add(DataSet);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        response.setHttp(HttpStatus.OK);
        response.setValue(Scoreboard);
        return response;
}
    //mach eine query die alle user sortiert nach elo ausgibt und zwar die daten username/win/loss/elo
    //jede iteration einer while schleife auf result liest eine zeile als List<string> ein


}
