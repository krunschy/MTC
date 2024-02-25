package org.game.services;

import org.game.DataBaseFunctions;
import org.game.DataBaseListListString;
import org.game.QueueManager;
import org.game.httpserver.http.ContentType;
import org.game.httpserver.http.HttpStatus;
import org.game.httpserver.server.Request;
import org.game.httpserver.server.Response;
import org.game.httpserver.server.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class BattleService implements Service {
//Grundidee: Wir nutzen 2 Blocking Queues, mit länge 1
//    Wenn jetzt ein user anmarschiert kommt, dann checkt er ob platz in der userqueue is. falls ja, trägt er sich ein und wartet auf battlelogqueue
//  Falls nein, nimmt er den namen aus der userqueue und führt damit ein battle durch. das battlelog schreibt er dann in battlelogqueue. Damit kann auch der erste user weitermachen
    // (der wartet, weil takebattlelogQueue() als take() implementiert ist). Dann können beide threads ihre response schreiben.
    public Response handleRequest(Request request) {
        //sehr umständliche aber verlässlichere art den Username zu kriegen
        String username = JsonUtils.getNameFromToken(request.getHeaderMap().getHeader("Authorization"));
        if (request.getHeaderMap().containsHeaderValue("Bearer " + username + "-mtcgToken")) {
            DataBaseFunctions db = DataBaseFunctions.getInstance();
            QueueManager Q = QueueManager.getInstance();
            int userslots = Q.checkUserSlots();
            String battlelog = "";
            String opponent = "";
            if(userslots == 1){
                Q.putUser(username);
                battlelog = Q.takeBattleLogQueue();
            }
            if(userslots == 0) {
                opponent = Q.takeUser();
                battlelog = db.DatabaseBattle(username, opponent);
                Q.putBattleLog(battlelog);
            }
            return new Response(HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    battlelog);
        } else {
            return new Response(HttpStatus.UNAUTHORIZED,
                    ContentType.PLAIN_TEXT,
                    "Unauthorized");
        }
    }
}
