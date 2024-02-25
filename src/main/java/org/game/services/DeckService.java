package org.game.services;

import org.game.DataBaseFunctions;
import org.game.DataBaseListString;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.http.Method;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;
import java.util.List;

public class DeckService implements Service{
    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            if(request.getMethod() == Method.GET) {
                DataBaseListString response = db.getDeck(username);
                if (response.getHttp() == HttpStatus.OK) {
                    String Kartenliste = "";
                    for (String Kartenname : response.getValue()) {
                        Kartenliste = Kartenliste + Kartenname + " ";
                    }
                    return new Response(HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            Kartenliste);
                } else {
                    return new Response(HttpStatus.NO_CONTENT,
                            ContentType.PLAIN_TEXT,
                            "You don't have any cards in your deck");
                }
            }
            //eig if(request.getMethod() == Method.PUT) aber intelliJ glaubt mir dann nicht, dass es immer nen return gibt
            else{
                List<String> CardIds = JsonUtils.deckIdsFromDeck(request.getBody());
                if(CardIds.size() < 4){
                    return new Response(HttpStatus.BAD_REQUEST,
                            ContentType.PLAIN_TEXT,
                            "The provided deck did not include the required amount of cards");
                }
                HttpStatus response = db.createDeck(username, CardIds.get(0), CardIds.get(1), CardIds.get(2), CardIds.get(3));
                if(response == HttpStatus.OK){
                    return new Response(HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            "The deck has been successfully configured");
                }
                else{
                    return new Response(HttpStatus.FORBIDDEN,
                            ContentType.PLAIN_TEXT,
                            "At least one of the provided cards does not belong to you or is not available.");
                }
            }
        } else {
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
        }
    }
}
