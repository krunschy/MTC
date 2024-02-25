package org.game.services;

import org.game.DataBaseFunctions;
import org.game.DataBaseListListString;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;
import java.util.List;


public class ScoreboardService implements Service {
    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            DataBaseListListString response = db.getScoreboard();
            List<List<String>> scoreboard = response.getValue();
            String result = "";
            for (List<String> entry : scoreboard) {
                String name = entry.get(0);
                String elo = entry.get(1);
                String line = "Name: " + name + ", Elo: " + elo + "\n";
                result += line;
            }
            return new Response(HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    result);
        } else {
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
        }
    }
}
