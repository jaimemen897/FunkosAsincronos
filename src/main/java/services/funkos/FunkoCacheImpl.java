package services.funkos;

import models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//TODO: Codificar los métodos
public class FunkoCacheImpl implements FunkoCache {
    private final Logger logger = LoggerFactory.getLogger(FunkoCacheImpl.class);
    private final int maxSize;
    private final Map<Long, Funko> cache;
    private final ScheduledExecutorService cleaner;

    //TODO: Cambiar esto para que se elimine solo, esto es temporal
    public FunkoCacheImpl(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<Long, Funko>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Funko> eldest) {
                return size() > maxSize;
            }
        };
        this.cleaner = Executors.newSingleThreadScheduledExecutor();
        this.cleaner.scheduleAtFixedRate(this::clear, 1,1, TimeUnit.MINUTES);
    }


    @Override
    public void put(Long key, Funko value) {

    }

    @Override
    public Funko get(Long key) {
        return null;
    }

    @Override
    public void remove(Long key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void shutdown() {

    }
}
