package org.game.services;

import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;

public class EchoService implements Service {
    @Override
    public Response handleRequest(Request request) {
        return new Response(HttpStatus.OK,
                ContentType.PLAIN_TEXT,
                "Echo-" + request.getBody());
    }
}