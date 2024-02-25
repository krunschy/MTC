package org.game;

import org.game.DataBaseFunctions;
import org.game.httpserver.http.HttpStatus;
import org.game.services.*;
import org.game.httpserver.server.Server;
import org.game.httpserver.utils.Router;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private static Router configureRouter()
        {
            Router router = new Router();
            router.addService("/echo", new EchoService());
            router.addService("/users", new UserService());
            router.addService("/users/{username}", new UsernameService());
            router.addService("/sessions", new SessionService());
            router.addService("/packages", new PackagesService());
            router.addService("/transactions/packages", new TransactionService());
            router.addService("/cards", new CardsService());
            router.addService("/deck", new DeckService());
            router.addService("/stats", new StatsService());
            router.addService("/scoreboard", new ScoreboardService());
            router.addService("/battles", new BattleService());
            router.addService("/tradings", new TradingsService());
            router.addService("/tradings/{tradingdealid}", new TradingsIDService());
            return router;
        }
}