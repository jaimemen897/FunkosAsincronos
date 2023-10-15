package controllers.funkos;

import controllers.FunkoController;
import enums.Modelo;
import models.Funko;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

class FunkoControllerTest {
    private FunkoController funkoController;

    @BeforeEach
    void setUp() throws Exception {
        funkoController = FunkoController.getInstance();
        funkoController.loadCsv().get();
    }

    @AfterEach
    void tearDown() {
        funkoController.getFunkos().clear();
    }

    @Test
    void loadCsvTest() throws Exception {
        Callable<List<Funko>> loadCsv = () -> funkoController.loadCsv().get();
        loadCsv.call();
        assertAll(
                () -> assertNotNull(funkoController.getFunkos()),
                () -> assertEquals(90, funkoController.getFunkos().size())
        );
    }

    @Test
    void expensiveFunko() throws Exception {
        Callable<Funko> expensiveFunko = () -> funkoController.expensiveFunko().get();
        expensiveFunko.call();
        assertAll(
                () -> assertNotNull(funkoController.expensiveFunko()),
                () -> assertEquals(52.99, funkoController.expensiveFunko().get().getPrecio()),
                () -> assertEquals("Peaky Blinders Tommy", funkoController.expensiveFunko().get().getNombre())

        );
    }

    @Test
    void averagePrice() throws Exception {
        Callable<Double> averagePrice = () -> funkoController.averagePrice().get();
        averagePrice.call();
        assertAll(
                () -> assertNotNull(funkoController.averagePrice()),
                () -> assertEquals(33.51222222222222, funkoController.averagePrice().get())
        );
    }

    @Test
    void groupByModelo() throws Exception {
        Callable<Map<Modelo, List<Funko>>> groupByModelo = () -> funkoController.groupByModelo().get();
        groupByModelo.call();
        assertAll(
                () -> assertNotNull(funkoController.groupByModelo()),
                () -> assertEquals(4, funkoController.groupByModelo().get().size())
        );
    }

    @Test
    void funkosByModelo() throws Exception {
        Callable<Map<Modelo, Long>> funkosByModelo = () -> funkoController.funkosByModelo().get();
        funkosByModelo.call();
        assertAll(
                () -> assertNotNull(funkoController.funkosByModelo()),
                () -> assertEquals(4, funkoController.funkosByModelo().get().size()),
                () -> assertEquals(26, funkoController.funkosByModelo().get().get(Modelo.MARVEL)),
                () -> assertEquals(23, funkoController.funkosByModelo().get().get(Modelo.ANIME)),
                () -> assertEquals(26, funkoController.funkosByModelo().get().get(Modelo.DISNEY)),
                () -> assertEquals(15, funkoController.funkosByModelo().get().get(Modelo.OTROS))
        );
    }

    @Test
    void funkosIn2023() throws Exception {
        Callable<List<Funko>> funkosIn2023 = () -> funkoController.funkosIn2023().get();
        funkosIn2023.call();
        assertAll(
                () -> assertNotNull(funkoController.funkosIn2023()),
                () -> assertEquals(57, funkoController.funkosIn2023().get().size()),
                () -> assertEquals(2023, funkoController.funkosIn2023().get().get(0).getFechaLanzamiento().getYear())
        );
    }

    @Test
    void numberStitch() throws Exception {
        Callable<Double> numberStitch = () -> funkoController.numberStitch().get();
        numberStitch.call();
        assertAll(
                () -> assertNotNull(funkoController.numberStitch()),
                () -> assertEquals(26, funkoController.numberStitch().get())
        );
    }

    @Test
    void funkoStitch() throws Exception {
        Callable<List<Funko>> funkoStitch = () -> funkoController.funkoStitch().get();
        funkoStitch.call();
        var result = funkoController.funkoStitch().get().get(0).getNombre().contains("Stitch");
        assertAll(
                () -> assertNotNull(funkoController.funkoStitch()),
                () -> assertEquals(26, funkoController.funkoStitch().get().size()),
                () -> assertTrue(result)
        );
    }
}