package services.funkos;

import models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class FunkoCacheImpl implements FunkoCache {
    private final Logger logger = LoggerFactory.getLogger(FunkoCacheImpl.class);
    private final int maxSize = 10;

    private final Map<Long, CompletableFuture<Funko>> cache;


    private final ScheduledExecutorService cleaner;

    public FunkoCacheImpl() {
        this.cache = new LinkedHashMap<Long, CompletableFuture<Funko>>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, CompletableFuture<Funko>> eldest) {
                return size() > maxSize;
            }
        };
        //Crea el programador para la limpieza automatica
        this.cleaner = Executors.newSingleThreadScheduledExecutor();

        //Programar la limpieza cada dos minutos
        this.cleaner.scheduleAtFixedRate(this::clear, 2, 2, TimeUnit.MINUTES);
    }

    @Override
    public void put(Long key, Funko value) {
        logger.debug("Añadiendo funko a la cache con id:" + key + " y valor:" + value);
        cache.put(key, CompletableFuture.completedFuture(value));
    }

    @Override
    public Funko get(Long key) {
        logger.debug("Obteniendo funko de la cache con id:" + key);

        if (cache.get(key) == null) {
            System.out.println("No existe el funko con id en la cache:" + key);
        }

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
        cache.entrySet().removeIf(entry -> {
            boolean shouldRemove = false;
            try {
                shouldRemove = entry.getValue().get().getUpdatedAt().plusMinutes(2).isBefore(LocalDateTime.now());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
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
