package org.game.services;

import org.game.DataBaseStringReturn;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.http.Method;

import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;
import org.game.DataBaseFunctions;

public class SessionService implements Service {
    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            String username = JsonUtils.getValueFromJSON(request.getBody(), "Username");
            String password = JsonUtils.getValueFromJSON(request.getBody(), "Password");
            if (db.LoginUser(username, password) == HttpStatus.OK) {
                return new Response(HttpStatus.OK,
                        ContentType.PLAIN_TEXT,
                        "Authorization: Bearer " + username + "-mtcgToken");
            }
        }
        return new Response(HttpStatus.UNAUTHORIZED,
                ContentType.PLAIN_TEXT,
                "login unsuccessful");
    }
}