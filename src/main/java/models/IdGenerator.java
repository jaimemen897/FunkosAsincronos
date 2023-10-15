package models;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IdGenerator {
    private static final Lock lock = new ReentrantLock();
    private static IdGenerator instance;
    private final AtomicLong myId = new AtomicLong(1);

    private IdGenerator() {}

    public synchronized static IdGenerator getInstance() {
        if (instance == null) {
            lock.lock();
            if (instance == null) {
                instance = new IdGenerator();
            }
            lock.unlock();
        }
        return instance;
    }

    public Long getAndIncrement() {
        return myId.getAndIncrement();
    }
}