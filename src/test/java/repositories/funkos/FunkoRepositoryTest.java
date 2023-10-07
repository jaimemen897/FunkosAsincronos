package repositories.funkos;

import enums.Modelo;
import models.Funko;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.database.DataBaseManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @AfterEach
    void tearDown() throws SQLException {
        funkoRepository.deleteAll();
    }

    @Test
    void save() throws SQLException, ExecutionException, InterruptedException {
        Funko funko = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(95L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko savedFunko = funkoRepository.save(funko).get();
        assertAll(
                () -> assertNotNull(savedFunko),
                () -> assertEquals(funko.getModelo(), savedFunko.getModelo()),
                () -> assertEquals(funko.getNombre(), savedFunko.getNombre()),
                () -> assertEquals(funko.getFechaLanzamiento(), savedFunko.getFechaLanzamiento()),
                () -> assertEquals(funko.getPrecio(), savedFunko.getPrecio()),
                () -> assertEquals(funko.getId2(), savedFunko.getId2()),
                () -> assertEquals(funko.getCod(), savedFunko.getCod())
        );
    }

    @Test
    void update() throws SQLException, ExecutionException, InterruptedException {
        Funko funko = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(95L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko savedFunko = funkoRepository.save(funko).get();
        savedFunko.setNombre("Rayo McQueen 2");
        savedFunko.setPrecio(200.0);
        Funko updatedFunko = funkoRepository.update(savedFunko).get();
        assertAll(
                () -> assertNotNull(updatedFunko),
                () -> assertEquals(savedFunko.getModelo(), updatedFunko.getModelo()),
                () -> assertEquals(savedFunko.getNombre(), updatedFunko.getNombre()),
                () -> assertEquals(savedFunko.getFechaLanzamiento(), updatedFunko.getFechaLanzamiento()),
                () -> assertEquals(savedFunko.getPrecio(), updatedFunko.getPrecio()),
                () -> assertEquals(savedFunko.getId2(), updatedFunko.getId2()),
                () -> assertEquals(savedFunko.getCod(), updatedFunko.getCod())
        );

    }

    @Test
    void findById() throws SQLException, ExecutionException, InterruptedException {
        Funko funko = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(95L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko savedFunko = funkoRepository.save(funko).get();
        Optional<Funko> foundFunko = funkoRepository.findById(savedFunko.getId2()).get();
        assertAll(
                () -> assertTrue(foundFunko.isPresent()),
                () -> assertEquals(savedFunko.getModelo(), foundFunko.get().getModelo()),
                () -> assertEquals(savedFunko.getNombre(), foundFunko.get().getNombre()),
                () -> assertEquals(savedFunko.getFechaLanzamiento(), foundFunko.get().getFechaLanzamiento()),
                () -> assertEquals(savedFunko.getPrecio(), foundFunko.get().getPrecio()),
                () -> assertEquals(savedFunko.getId2(), foundFunko.get().getId2()),
                () -> assertEquals(savedFunko.getCod(), foundFunko.get().getCod())
        );

    }

    @Test
    void findAll() throws SQLException, ExecutionException, InterruptedException {
        Funko funko1 = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(95L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko funko2 = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(96L)
                .nombre("Mate")
                .modelo(Modelo.DISNEY)
                .precio(200.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        funkoRepository.save(funko1).get();
        funkoRepository.save(funko2).get();
        List<Funko> foundFunkos = funkoRepository.findAll().get();
        assertEquals(2, foundFunkos.size());
    }

    @Test
    void findByNombre() throws SQLException, ExecutionException, InterruptedException {
        Funko funko1 = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(95L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko funko2 = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(96L)
                .nombre("Mate")
                .modelo(Modelo.DISNEY)
                .precio(200.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        funkoRepository.save(funko1).get();
        funkoRepository.save(funko2).get();
        List<Funko> foundFunkos = funkoRepository.findByNombre("Rayo McQueen").get();
        System.out.println(foundFunkos);
        assertAll(
                () -> assertEquals(1, foundFunkos.size()),
                () -> assertEquals(funko1.getModelo(), foundFunkos.get(0).getModelo()),
                () -> assertEquals(funko1.getNombre(), foundFunkos.get(0).getNombre()),
                () -> assertEquals(funko1.getFechaLanzamiento(), foundFunkos.get(0).getFechaLanzamiento()),
                () -> assertEquals(funko1.getPrecio(), foundFunkos.get(0).getPrecio()),
                () -> assertEquals(funko1.getId2(), foundFunkos.get(0).getId2()),
                () -> assertEquals(funko1.getCod(), foundFunkos.get(0).getCod())
        );
    }

    @Test
    void deleteById() throws SQLException, ExecutionException, InterruptedException {
        Funko funko1 = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(95L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko savedFunko = funkoRepository.save(funko1).get();
        funkoRepository.deleteById(savedFunko.getId2()).get();
        Optional<Funko> foundFunko = funkoRepository.findById(savedFunko.getId2()).get();
        assertFalse(foundFunko.isPresent());
    }

    @Test
    void deleteAll() throws SQLException, ExecutionException, InterruptedException {
        Funko funko1 = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(95L)
                .nombre("Rayo McQueen")
                .modelo(Modelo.DISNEY)
                .precio(100.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        Funko funko2 = Funko.builder()
                .cod(UUID.randomUUID())
                .id2(96L)
                .nombre("Mate")
                .modelo(Modelo.DISNEY)
                .precio(200.0)
                .fechaLanzamiento(LocalDate.parse("2021-10-07"))
                .build();
        funkoRepository.save(funko1).get();
        funkoRepository.save(funko2).get();
        funkoRepository.deleteAll().get();
        List<Funko> foundFunkos = funkoRepository.findAll().get();
        assertEquals(0, foundFunkos.size());
    }
}