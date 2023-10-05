package controllers;

import enums.Modelo;
import lombok.Getter;
import models.Funko;
import models.IdGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
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
            return funkos;
        });
    }

    public CompletableFuture<Funko> expensiveFunko() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().max(Comparator.comparingDouble(Funko::getPrecio)).get());
    }

    public CompletableFuture<Double> averagePrice() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().mapToDouble(Funko::getPrecio).average().getAsDouble());
    }

    public CompletableFuture<Map<Modelo, List<Funko>>> groupByModelo() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().collect(Collectors.groupingBy(Funko::getModelo)));
    }

    public CompletableFuture<Map<Modelo, Long>> funkosByModelo() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().collect(Collectors.groupingBy(Funko::getModelo, Collectors.counting())));
    }

    public CompletableFuture<List<Funko>> funkosIn2023() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().filter(funko -> funko.getFechaLanzamiento().getYear() == 2023).collect(Collectors.toList()));
    }

    public CompletableFuture<Double> numberStitch() {
        return CompletableFuture.supplyAsync(() -> (double) funkos.stream().filter(funko -> funko.getNombre().contains("Stitch")).count());
    }

    public CompletableFuture<List<Funko>> funkoStitch() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().filter(funko -> funko.getNombre().contains("Stitch")).collect(Collectors.toList()));
    }
}
