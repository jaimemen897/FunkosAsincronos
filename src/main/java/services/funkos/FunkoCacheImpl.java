package services.funkos;

import models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

//TODO: Codificar los métodos
public class FunkoCacheImpl implements FunkoCache {
    private final Logger logger = LoggerFactory.getLogger(FunkoCacheImpl.class);
    private final int maxSize;
    private final long cacheExpirationMillis;
    private final Map<Long, CompletableFuture<Funko>> cache;
    private final ScheduledExecutorService cleaner;
    private final AtomicLong lastAccessTime;

    public FunkoCacheImpl(int maxSize, long cacheExpirationMillis) {
        this.cacheExpirationMillis = cacheExpirationMillis;
        this.maxSize = maxSize;
        this.cache = new ConcurrentHashMap<>(maxSize);
        this.cleaner = Executors.newSingleThreadScheduledExecutor();
        this.lastAccessTime = new AtomicLong(System.currentTimeMillis());

        cleaner.scheduleAtFixedRate(this::clear, 0, 1, TimeUnit.MINUTES);
    }

    public CompletableFuture<Funko> getAsync(Long key, CompletableFuture<Funko> valueSupplier) {
        long currentTime = System.currentTimeMillis();
        lastAccessTime.set(currentTime);
        CompletableFuture<Funko> cachedValue = cache.get(key);

        if (cachedValue != null) {
            return cachedValue;
        } else {
            CompletableFuture<Funko> futureValue = new CompletableFuture<>();
            cache.put(key, futureValue);

            valueSupplier.thenAccept(result -> {
                futureValue.complete(result);
                cleaner.schedule(() -> cache.remove(key), cacheExpirationMillis, TimeUnit.MILLISECONDS);
            });

            return futureValue;
        }
    }

    @Override
    public void put(Long key, Funko value) {
        logger.debug("Añadiendo funko a la cache con id:" + key + " y valor:" + value);
        cache.put(key, CompletableFuture.completedFuture(value));
    }

    @Override
    public Funko get(Long key) {
        logger.debug("Obteniendo funko de la cache con id:" + key);
        return cache.get(key).join();
    }

    @Override
    public void remove(Long key) {
        if (cache.size() == maxSize) {
            logger.debug("Eliminando funko de la cache con id:" + key);
            cache.remove(key);
        } else {
            logger.debug("No se puede eliminar el funko de la cache con id:" + key + " porque la cache no tiene 10 elementos");
        }

    }

    @Override
    public void clear() {
        long currentTime = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> {
            boolean shouldRemove = currentTime - lastAccessTime.get() > cacheExpirationMillis;
            if (shouldRemove) {
                logger.debug("Eliminando funko de la cache con id:" + entry.getKey());
            }
            return shouldRemove;
        });
    }

    @Override
    public void shutdown() {
        logger.debug("Cerrando cache");
        cleaner.shutdown();
    }
}
