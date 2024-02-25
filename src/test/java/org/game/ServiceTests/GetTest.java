package org.game.ServiceTests;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetTest {
    //diese tests verlassen sich auf eine DB nach dem curl script, aber so is das halt iwie beim DB testen
    //test getscoreboard service
    @Test
    void testScoreboardEndpoint() throws Exception {
        URL url = new URL("http://localhost:10001/scoreboard");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer kienboec-mtcgToken");
        int responseCode = connection.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        } finally {
            connection.disconnect();
        }
        assertEquals(200, responseCode);
        assertEquals("Name: admin, Elo: 100Name: kienboec, Elo: 100Name: altenhof, Elo: 100", responseContent.toString());
    }
    //test getstats service
    @Test
    void testStatsEndpoint() throws Exception {
        URL url = new URL("http://localhost:10001/stats");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer kienboec-mtcgToken");
        int responseCode = connection.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        } finally {
            connection.disconnect();
        }
        assertEquals(200, responseCode);
        assertEquals("Name: Kienboeck, Elo: 100, Wins: 0, Losses: 0, Money: 0", responseContent.toString());
    }
    //test getcards service without token, should return unauthorized
    @Test
    void testCardsEndpoint() throws Exception {
        URL url = new URL("http://localhost:10001/cards");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        assertEquals(401, responseCode);
    }
    //test getuserdata service with nonexistent user
    @Test
    void testUsersEndpoint() throws Exception {
        URL url = new URL("http://localhost:10001/users/fakeuser");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer admin-mtcgToken");
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        assertEquals(404, responseCode);
    }
    //test get deck service
    @Test
    void testdeckEndpoint() throws Exception {
        URL url = new URL("http://localhost:10001/deck");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer kienboec-mtcgToken");
        int responseCode = connection.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        } finally {
            connection.disconnect();
        }
        assertEquals(200, responseCode);
        assertEquals("WaterGoblin Dragon WaterSpell RegularSpell ", responseContent.toString());
    }
}
