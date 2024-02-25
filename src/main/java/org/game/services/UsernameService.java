package org.game.services;

import org.game.DataBaseStringReturn;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.http.Method;
import org.game.httpserver.server.HeaderMap;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;
import org.game.DataBaseFunctions;


public class UsernameService implements Service {
    @Override
    public Response handleRequest(Request request) {
        String username = request.getPathParts().get(1);

        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken") || request.getHeaderMap().containsHeaderValue("Bearer admin-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            if (request.getMethod() == Method.GET) {
                DataBaseStringReturn value;
                value = db.getUserData(username);
                if (value.getHttp() == HttpStatus.OK) {
                    return new Response(value.getHttp(),
                            ContentType.PLAIN_TEXT,
                            "Username: " + value.getValue()[0] + " Bio: " + value.getValue()[1] + " Image: " + value.getValue()[2] + " Money: " + value.getValue()[3] + " Elo: " + value.getValue()[4]);
                }
                if (value.getHttp() == HttpStatus.NOT_FOUND) {
                    return new Response(value.getHttp(),
                            ContentType.PLAIN_TEXT,
                            "User not found.");
                }
            }
            if (request.getMethod() == Method.PUT) {
                String name = JsonUtils.getValueFromJSON(request.getBody(), "Name");
                String bio = JsonUtils.getValueFromJSON(request.getBody(), "Bio");
                String image = JsonUtils.getValueFromJSON(request.getBody(), "Image");
                HttpStatus status = db.UpdateUserData(username, name, bio, image);
                if (status == HttpStatus.OK) {
                    return new Response(HttpStatus.CREATED,
                            ContentType.PLAIN_TEXT,
                            "User successfully updated.");
                }
                else{
                    return new Response(status,
                            ContentType.PLAIN_TEXT,
                            "User not found.");
                }
            }
        }
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
    }
}
