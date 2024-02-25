package org.game;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueManager {
    private static QueueManager instance;
    private BlockingQueue<String> userQueue; // Queue for users who want to battle
    private BlockingQueue<String> battleLogQueue; // Queue for the battle log

    public QueueManager() {
        // Initialize the queues
        userQueue = new ArrayBlockingQueue<>(1); // Queue with a length of 1 for users
        battleLogQueue = new ArrayBlockingQueue<>(1); // Queue with a length of 1 for battle log
    }

    public static synchronized QueueManager getInstance() {
        if (instance == null) {
            instance = new QueueManager();
        }
        return instance;
    }

    public void putUser(String username) {
        try {
            userQueue.put(username);
        } catch (InterruptedException e) { //falls der thread unterbrochen wird
            e.printStackTrace();
        }
    }

    public int checkUserSlots() {
        return userQueue.remainingCapacity(); // Check the number of available slots in the user queue
    }

    public void putBattleLog(String log) {
        try {
            battleLogQueue.put(log);
        } catch (InterruptedException e) { //falls der thread unterbrochen wird
            e.printStackTrace();
        }
    }

    public String takeUser() {
        try {
            return userQueue.take();
        } catch (InterruptedException e) { //falls der thread unterbrochen wird
            e.printStackTrace();
            return null;
        }
    }

    public String takeBattleLogQueue() {
        try {
            return battleLogQueue.take();
        } catch (InterruptedException e) { //falls der thread unterbrochen wird
            e.printStackTrace();
            return null;
        }
    }
}