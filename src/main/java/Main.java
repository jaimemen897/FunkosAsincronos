import controllers.FunkoController;
import models.Funko;
import repositories.funkos.FunkoRepositoryImpl;
import services.database.DataBaseManager;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        FunkoController funkoController = FunkoController.getInstance();
        Callable<List<Funko>> callableLoadCsv = () -> funkoController.loadCsv().get();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(callableLoadCsv);
        DataBaseManager db = DataBaseManager.getInstance();

        FunkoRepositoryImpl funkoRepository = FunkoRepositoryImpl.getInstance(db);
        funkoRepository.findAll().thenAcceptAsync(System.out::println);







        executorService.shutdown();
    }
}
