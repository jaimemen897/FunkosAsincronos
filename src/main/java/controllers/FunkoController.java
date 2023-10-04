package controllers;

import enums.Modelo;
import models.Funko;
import models.IdGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FunkoController {
    private static FunkoController instance;
    private final List<Funko> funkos = new ArrayList<>();
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    public static FunkoController getInstance() {
        if (instance == null) {
            instance = new FunkoController();
        }
        return instance;
    }


    public FunkoController() {
        if (instance == null) {
            instance = this;
        }
    }

    public CompletableFuture<List<Funko>> loadCsv() {
        return CompletableFuture.supplyAsync(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader("src" + File.separator + "data" + File.separator + "funkos.csv"))) {
                String line = br.readLine();
                line = br.readLine();
                while (line != null) {
                    String[] split = line.split(",");

                    int year = Integer.parseInt(split[4].split("-")[0]);
                    int month = Integer.parseInt(split[4].split("-")[1]);
                    int day = Integer.parseInt(split[4].split("-")[2]);

                    LocalDate dia = LocalDate.of(year, month, day);

                    UUID cod = UUID.fromString(split[0].substring(0, 35));

                    funkos.add(Funko.builder()
                            .id(UUID.randomUUID())
                            .cod(cod)
                            .id2(idGenerator.getAndIncrement())
                            .nombre(split[1])
                            .modelo(Modelo.valueOf(split[2]))
                            .precio(Double.parseDouble(split[3]))
                            .fechaLanzamiento(dia)
                            .build());
                    line = br.readLine();
                }

            } catch (IOException e) {
                System.out.println("Error al leer el archivo CSV: " + e.getMessage());
            }
            funkos.forEach(System.out::println);
            return funkos;
        });
    }


}
