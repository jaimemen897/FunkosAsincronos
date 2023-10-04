package models;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static IdGenerator instance;

    private AtomicLong myId = new AtomicLong(1);

    private IdGenerator() {
    }

    public synchronized static IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public long getAndIncrement() {
        return myId.getAndIncrement();
    }
}