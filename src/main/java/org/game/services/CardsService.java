package org.game.services;

import org.game.DataBaseFunctions;
import org.game.DataBaseListString;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;

public class CardsService implements Service {

    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            DataBaseListString response = db.getStack(username);
            if(response.getHttp() == HttpStatus.OK){
                String Kartenliste="";
                for(String Kartenname:response.getValue()){
                    Kartenliste = Kartenliste + Kartenname + " ";
                }
                return new Response(HttpStatus.OK,
                        ContentType.PLAIN_TEXT,
                        Kartenliste);
            }
            else{
                return new Response(HttpStatus.NO_CONTENT,
                        ContentType.PLAIN_TEXT,
                        "You don't own any cards");
            }
        } else {
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
        }
    }
}

