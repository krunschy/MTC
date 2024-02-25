package org.game.services;
import java.util.List;
import java.util.ArrayList;

public class JsonUtils {

    public static List<String> deckIdsFromDeck(String deck) {
        List<String> deckIds = new ArrayList<>();

        // Remove the surrounding square brackets and quotation marks
        String cleanInput = deck.replaceAll("[\\[\\]\"]", "");

        // Split the cleaned input string by ", "
        String[] stringArray = cleanInput.split(", ");

        // Add each ID to the list
        for (String id : stringArray) {
            deckIds.add(id);
        }

        return deckIds;
    }

    public static String getNameFromToken(String input) {
        // Split the string based on the space character (' ')
        if(input==null){ //in case of no token at all, cause input.split wont work otherwise
            return "";
        }
        String[] parts = input.split(" ");

        // Retrieve the second part (index 1) if it exists
        if (parts.length > 1) {
            String tokenWithSuffix = parts[1];

            // Split the token based on the '-' character
            String[] tokenParts = tokenWithSuffix.split("-");

            // Retrieve the first part (index 0) if it exists
            if (tokenParts.length > 0) {
                return tokenParts[0];
            }
        }
        return null; // Invalid input string or token
    }
    public static String getValueFromJSON(String json, String key) {
        int startIndex = json.indexOf("\"" + key + "\":") + key.length() + 3;
        if (startIndex == -1) {
            // Key not found, return null or empty string
            return "";
        }
        // Skip any whitespace characters after the colon
        while (json.charAt(startIndex) == ' ' || json.charAt(startIndex) == '\t' || json.charAt(startIndex) == '\n' || json.charAt(startIndex) == '\r') {
            startIndex++;
        }
        int endIndex;
        if (json.charAt(startIndex) == '"') {
            // If the value is a string (starts with a quote), find the next quote
            startIndex++; // Move past the opening quote
            endIndex = json.indexOf("\"", startIndex);
        } else {
            // If the value is not a string, find the next comma or closing brace
            endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }
        }
        return json.substring(startIndex, endIndex);
    }
    public static List<String> getIndividualJsonObjects(String jsonArray) {
        List<String> individualObjects = new ArrayList<>();
        int startIndex = 0;
        int endIndex;
        while ((endIndex = jsonArray.indexOf("},", startIndex)) != -1) {
            individualObjects.add(jsonArray.substring(startIndex, endIndex + 1));
            startIndex = endIndex + 2; // Move past the comma and space
        }
        // Add the last object (no comma after the last object)
        individualObjects.add(jsonArray.substring(startIndex));
        return individualObjects;
    }
}
