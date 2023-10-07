package repositories.funkos;

import enums.Modelo;
import models.Funko;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.database.DataBaseManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class FunkoRepositoryTest {

    private FunkoRepository funkoRepository;

    @BeforeEach
    void setUp() throws SQLException {
        funkoRepository = FunkoRepositoryImpl.getInstance(DataBaseManager.getInstance());
        funkoRepository.deleteAll();
    }

    @Test
    void save() throws SQLException, ExecutionException, InterruptedException {
        Funko funko = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(1L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko savedFunko = funkoRepository.save(funko).get();
        assertAll(
                () -> assertNotNull(savedFunko),
                () -> assertNotNull(savedFunko.getModelo()),
                () -> assertEquals(funko.getNombre(), savedFunko.getNombre()),
                () -> assertEquals(funko.getFechaLanzamiento(), savedFunko.getFechaLanzamiento()),
                () -> assertNotNull(savedFunko.getCod()),
                () -> assertNotNull(savedFunko.getCreatedAt()),
                () -> assertNotNull(savedFunko.getUpdatedAt())
        );
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void findByNombre() {
    }

    @Test
    void exportJson() {
    }
}