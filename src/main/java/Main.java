import controllers.FunkoController;
import enums.Modelo;
import models.Funko;
import org.h2.command.dml.Call;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        FunkoController funkoController = FunkoController.getInstance();

        Callable<List<Funko>> loadCsv = () -> funkoController.loadCsv().get();
        Callable<Funko> expensiveFunko = () -> funkoController.expensiveFunko().get();
        Callable<Double> averagePrice = () -> funkoController.averagePrice().get();
        Callable<Map<Modelo, List<Funko>>> groupByModelo = () -> funkoController.groupByModelo().get();
        Callable<Map<Modelo, Long>> funkosByModelo = () -> funkoController.funkosByModelo().get();
        Callable<List<Funko>> funkosIn2023 = () -> funkoController.funkosIn2023().get();
        Callable<Double> numberStitch = () -> funkoController.numberStitch().get();
        Callable<List<Funko>> funkoStitch = () -> funkoController.funkoStitch().get();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<List<Funko>> future = executorService.submit(loadCsv);
        Future<Funko> future2 = executorService.submit(expensiveFunko);
        Future<Double> future3 = executorService.submit(averagePrice);
        Future<Map<Modelo, List<Funko>>> future4 = executorService.submit(groupByModelo);
        Future<Map<Modelo, Long>> future5 = executorService.submit(funkosByModelo);
        Future<List<Funko>> future6 = executorService.submit(funkosIn2023);
        Future<Double> future7 = executorService.submit(numberStitch);
        Future<List<Funko>> future8 = executorService.submit(funkoStitch);

        try {
            System.out.println("Funkos: " + future.get());
            System.out.println("Funko más caro: " + future2.get());
            System.out.println("Media de los precios: " + future3.get());
            System.out.println("Funkos agrupados por modelo: " + future4.get());
            System.out.println("Número de funkos por modelo: " + future5.get());
            System.out.println("Funkos lanzados en 2023: " + future6.get());
            System.out.println("Número de funkos de Stitch: " + future7.get());
            System.out.println("Funkos de Stitch: " + future8.get());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        executorService.shutdown();
    }
}