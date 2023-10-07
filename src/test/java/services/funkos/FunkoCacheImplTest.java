package services.funkos;

import enums.Modelo;
import models.Funko;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FunkoCacheImplTest {
    private FunkoCacheImpl cache;

    @BeforeEach
    public void setUp() {
        cache = new FunkoCacheImpl();
    }

    @AfterEach
    public void tearDown() {
        cache.shutdown();
    }

    @Test
    void putGet() {
        Long id = 95L;
        Funko funko = Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();
        cache.put(id, funko);
        Funko funkoCache = cache.get(funko.getId2());
        assertAll(
                () -> assertNotNull(funkoCache),
                () -> assertEquals(funko, funkoCache)
        );
    }

    @Test
    void remove() {
        Long id = 95L;
        Funko funko = Funko.builder().cod(UUID.randomUUID()).id2(id).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();
        cache.put(id, funko);
        cache.remove(id);

        assertNull(cache.get(id));
    }

    @Test
    void clear() throws InterruptedException {
        Long id = 95L;
        Funko funko = Funko.builder().cod(UUID.randomUUID()).id2(id).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();
        cache.put(id, funko);
        Thread.sleep(125000);
        assertNull(cache.get(id));
    }
}