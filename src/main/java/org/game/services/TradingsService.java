package org.game.services;

import org.game.DataBaseFunctions;
import org.game.DataBaseListListString;
import org.game.DataBaseListString;
import org.game.DataBaseStringReturn;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.http.Method;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;

import java.util.List;

public class TradingsService implements Service {
    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            if(request.getMethod() == Method.GET) {
                DataBaseListListString response = db.browseTrades(username);
                //if (response.getHttp() == HttpStatus.OK) gibt lt api eh nur den fall
                    List<List<String>> tradetable = response.getValue();
                    String result = "";
                    for (List<String> entry : tradetable) {
                        String reqdamage = entry.get(0);
                        String reqelement = entry.get(1);
                        String reqtype = entry.get(2);
                        String element = entry.get(3);
                        String cardtype = entry.get(4);
                        String name = entry.get(5);
                        String damage = entry.get(6);
                        String line = "Name: " + name + ", Element: " + element + ", Cardtype: " + cardtype + ", Damage: " + damage + ", Required Element: " + reqelement + ", Required Type: " + reqtype + ", required Damage: " + reqdamage + "\n";
                        result += line;
                    }
                    return new Response(HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            result);
                }
            //eig if(request.getMethod() == Method.POST) aber intelliJ glaubt mir dann nicht, dass es immer nen return gibt
            else{
                String TradeId = JsonUtils.getValueFromJSON(request.getBody(), "Id");
                String CardId = JsonUtils.getValueFromJSON(request.getBody(), "CardToTrade");
                String ReqType = JsonUtils.getValueFromJSON(request.getBody(), "Type");
                String ReqDmg = JsonUtils.getValueFromJSON(request.getBody(), "MinimumDamage");
                String ReqEle = "none";
                //eigentlich sollte es von reqDmg und reqEle nur eins geben lt angabe und das ghört überprüft, aber api specification sagt, reqele gibts gar net
                //man kanns anpassen, dass nur eins geben soll, aber dann gehört auch der nächste check entsprechend:
                if(TradeId.isBlank() || CardId.isBlank() || ReqType.isBlank() || ReqDmg.isBlank()){
                    return new Response(HttpStatus.FORBIDDEN,
                            ContentType.PLAIN_TEXT,
                            "The required values are not set.");
                }
                HttpStatus response = db.createTradeOffer(username, TradeId, CardId, ReqType, Integer.parseInt(ReqDmg), ReqEle);
                if(response == HttpStatus.CREATED){
                    return new Response(HttpStatus.CREATED,
                            ContentType.PLAIN_TEXT,
                            "Trading deal successfully generated.");
                }
                if(response == HttpStatus.FORBIDDEN){
                    return new Response(HttpStatus.FORBIDDEN,
                            ContentType.PLAIN_TEXT,
                            "The offered card is not owned by you or the offered card is locked in the deck.");
                }
                else{
                    return new Response(HttpStatus.CONFLICT,
                            ContentType.PLAIN_TEXT,
                            "A deal with this deal ID already exists.");
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
