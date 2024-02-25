package org.game.services;

import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.http.Method;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;
import org.game.DataBaseFunctions;


public class UserService implements Service {
    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            String username = JsonUtils.getValueFromJSON(request.getBody(), "Username");
            String password = JsonUtils.getValueFromJSON(request.getBody(), "Password");
            if (db.CreateUser(username, password) == HttpStatus.CREATED) {
                return new Response(HttpStatus.CREATED,
                        ContentType.PLAIN_TEXT,
                        "User successfully created");
            }
        }
        return new Response(HttpStatus.CONFLICT,
                ContentType.PLAIN_TEXT,
                "User with same username already registered");
    }
}
