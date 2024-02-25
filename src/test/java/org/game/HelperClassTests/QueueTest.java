package org.game.HelperClassTests;

import org.game.QueueManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueueTest {
    //teste obs eh get zeug rein und rauszuhaun

    @Test
    void putandtake() {
        QueueManager queueManager = new QueueManager();
        String username = "testUser";
        queueManager.putUser(username);
        assertEquals(username, queueManager.takeUser());
        assertEquals(1, queueManager.checkUserSlots());
        queueManager.putUser("user1");
        assertEquals(0, queueManager.checkUserSlots());
        String log = "Test log message";
        queueManager.putBattleLog(log);
        assertEquals(log, queueManager.takeBattleLogQueue());
    }
}

