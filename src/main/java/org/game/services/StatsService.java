package org.game.services;

import org.game.DataBaseFunctions;
import org.game.DataBaseListString;
import org.game.DataBaseStringReturn;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;

public class StatsService implements Service {
    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            DataBaseStringReturn response = db.getUserStats(username);
                String[] userinfo = response.getValue();
                String StatsString = "Name: " + userinfo[0] + ", Elo: " + userinfo[1] + ", Wins: " + userinfo[2] + ", Losses: " + userinfo[3] + ", Money: " + userinfo[4];
                return new Response(HttpStatus.OK,
                        ContentType.PLAIN_TEXT,
                        StatsString);
        } else {
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
        }
    }
}

