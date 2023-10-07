package routes;

import lombok.Getter;

import java.io.File;

@Getter
public class Routes {
    private static Routes instance;
    private final String rutaFunkosCsv = "src" + File.separator + "data" + File.separator + "funkos.csv";
    private final String rutaFunkosJson = "src" + File.separator + "data" + File.separator + "funkos.json";

    public static Routes getInstance() {
        if (instance == null) {
            instance = new Routes();
        }
        return instance;
    }
}
