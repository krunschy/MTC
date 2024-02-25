package org.game.ServiceTests;

import org.junit.jupiter.api.Test;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradingTest { //diese tests machen nur in reihenfolge sinn und der buttonlinks von der klasse beachte diese nicht. daher nur einzelnd durchführen
    //trade anlegen
    @Test
    void testCreateTrade() throws Exception {
        // Set up the URL and authorization header
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer kienboec-mtcgToken");
        connection.setRequestProperty("Content-Type", "application/json");

        // Set up the request body
        String requestBody = "{\"Id\": \"TestID\", \"CardToTrade\": \"8c20639d-6400-4534-bd0f-ae563f11f57a\", \"Type\": \"monster\", \"MinimumDamage\": 15}";

        // Enable output for the connection and write the request body
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();

        // Close the connection
        connection.disconnect();

        // Assert the response code
        assertEquals(201, responseCode);
    }
    //trade anschauen
    @Test
    void testCheckTrade() throws Exception {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer altenhof-mtcgToken");
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
        assertEquals("Name: WaterSpell, Element: Water, Cardtype: Spell, Damage: 25, Required Element: none, Required Type: monster, required Damage: 15", responseContent.toString());
    }
    //trade löschen
    @Test
    void testDeleteAndCheckTrade() throws Exception {
        URL url = new URL("http://localhost:10001/tradings/TestID");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Authorization", "Bearer kienboec-mtcgToken");
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        assertEquals(201, responseCode);
        //connect again to see if it worked
        URL url2 = new URL("http://localhost:10001/tradings");
        HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
        connection2.setRequestMethod("GET");
        connection2.setRequestProperty("Authorization", "Bearer altenhof-mtcgToken");
        responseCode = connection2.getResponseCode();
        StringBuilder responseContent = new StringBuilder();
        try (InputStream inputStream = connection2.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        } finally {
            connection2.disconnect();
        }
        assertEquals(200, responseCode);
        assertEquals("", responseContent.toString());
    }
}
