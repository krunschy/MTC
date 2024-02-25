package org.game.services;

import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.http.Method;
import java.util.List;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;
import org.game.DataBaseFunctions;
public class PackagesService implements Service {
    @Override
    public Response handleRequest(Request request) {
        if (request.getHeaderMap().containsHeaderValue("Bearer admin-mtcgToken")) {
            List<String> PackKarten = JsonUtils.getIndividualJsonObjects(request.getBody());
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            //I am deeply sorry for this function call.
            HttpStatus status = db.createPackage(JsonUtils.getValueFromJSON(PackKarten.get(0), "Id"), JsonUtils.getValueFromJSON(PackKarten.get(0), "Name"), (int) Double.parseDouble(JsonUtils.getValueFromJSON(PackKarten.get(0), "Damage")),
                    JsonUtils.getValueFromJSON(PackKarten.get(1), "Id"), JsonUtils.getValueFromJSON(PackKarten.get(1), "Name"), (int) Double.parseDouble(JsonUtils.getValueFromJSON(PackKarten.get(1), "Damage")),
                    JsonUtils.getValueFromJSON(PackKarten.get(2), "Id"), JsonUtils.getValueFromJSON(PackKarten.get(2), "Name"), (int) Double.parseDouble(JsonUtils.getValueFromJSON(PackKarten.get(2), "Damage")),
                    JsonUtils.getValueFromJSON(PackKarten.get(3), "Id"), JsonUtils.getValueFromJSON(PackKarten.get(3), "Name"), (int) Double.parseDouble(JsonUtils.getValueFromJSON(PackKarten.get(3), "Damage")),
                    JsonUtils.getValueFromJSON(PackKarten.get(4), "Id"), JsonUtils.getValueFromJSON(PackKarten.get(4), "Name"), (int) Double.parseDouble(JsonUtils.getValueFromJSON(PackKarten.get(4), "Damage")));
            if (status == HttpStatus.CREATED) {
                return new Response(status,
                        ContentType.PLAIN_TEXT,
                        "Package and cards successfully created");
            } else {
                return new Response(status,
                        ContentType.PLAIN_TEXT,
                        "At least one card in the packages already exists");
            }
        } else {
            return new Response(HttpStatus.FORBIDDEN,
                    ContentType.PLAIN_TEXT,
                    "Provided user is not \"admin\"");
        }
//      return new Response(HttpStatus.UNAUTHORIZED,
//                ContentType.PLAIN_TEXT,
//                "UNAUTHORIZED");
    }
}
