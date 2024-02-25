package org.game.services;

import org.game.DataBaseFunctions;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.http.Method;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;


public class TradingsIDService implements Service {

    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        String TradeId = request.getPathParts().get(1);
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            if(request.getMethod() == Method.POST) {
                String CardId = request.getBody().substring(1, request.getBody().length() - 1);
                System.out.println(CardId);
                HttpStatus response = db.commitToTrade(username, TradeId, CardId);
                if (response == HttpStatus.OK) {
                    return new Response(HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            "Trading deal successfully executed.");
                }
                if(response == HttpStatus.FORBIDDEN){
                    return new Response(HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            "Trading deal unsuccessful. This could be due to this being your trade, the provided card not being yours or the card not matching the requirements.");
                }
                else{
                    return new Response(HttpStatus.NOT_FOUND,
                            ContentType.PLAIN_TEXT,
                            "Trade was not found.");
                }
            }
            //eig if(request.getMethod() == Method.DELETE) aber intelliJ glaubt mir dann nicht, dass es immer nen return gibt
            else{
                HttpStatus response = db.deleteTrade(username, TradeId);
                if(response == HttpStatus.OK){
                    return new Response(HttpStatus.CREATED,
                            ContentType.PLAIN_TEXT,
                            "Trading deal successfully deleted.");
                }
                if(response == HttpStatus.FORBIDDEN){
                    return new Response(HttpStatus.FORBIDDEN,
                            ContentType.PLAIN_TEXT,
                            "The deal is not yours, so you can't delete it.");
                }
                else{
                    return new Response(HttpStatus.NOT_FOUND,
                            ContentType.PLAIN_TEXT,
                            "The deal has not been found.");
                }
            }
        }
        else {
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
        }
    }
}
