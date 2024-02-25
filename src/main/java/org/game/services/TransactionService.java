package org.game.services;

import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;
import org.game.DataBaseFunctions;

public class TransactionService implements Service {

    @Override
    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            HttpStatus response = db.buyPackage(username);
            if (response == HttpStatus.OK){
                return new Response(HttpStatus.OK,
                ContentType.PLAIN_TEXT,
                        "A package has been successfully bought");
            }
            else if (response == HttpStatus.FORBIDDEN){
                return new Response(HttpStatus.FORBIDDEN,
                ContentType.PLAIN_TEXT,
                        "Not enough money for buying a card package");
            }
            else{
                return new Response(HttpStatus.NOT_FOUND,
                ContentType.PLAIN_TEXT,
                        "No card package available for buying");
            }

        } else {
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
        }
    }
}
