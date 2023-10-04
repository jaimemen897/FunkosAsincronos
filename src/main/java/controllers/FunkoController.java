package controllers;

import enums.Modelo;
import models.Funko;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FunkoController {
    private static FunkoController instance;
    private final List<Funko> funkos = new ArrayList<>();

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

                    funkos.add(new Funko(cod, split[1], Modelo.valueOf(split[2]), Double.parseDouble(split[3]), dia));
                    line = br.readLine();
                }

            } catch (IOException e) {
                System.out.println("Error al leer el archivo CSV: " + e.getMessage());
            }
            return funkos;
        });
    }

    public CompletableFuture<Funko> expensiveFunko() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().max(Comparator.comparingDouble(Funko::precio)).get());
    }

    public CompletableFuture<Double> averagePrice() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().mapToDouble(Funko::precio).average().getAsDouble());
    }

    public CompletableFuture<Map<Modelo, List<Funko>>> groupByModelo() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().collect(Collectors.groupingBy(Funko::modelo)));
    }

    public CompletableFuture<Map<Modelo, Long>> funkosByModelo() {
        return CompletableFuture.supplyAsync(() -> funkos.stream().collect(Collectors.groupingBy(Funko::modelo, Collectors.counting())));
    }

    public CompletableFuture<List<Funko>> funkosIn2023(){
        return CompletableFuture.supplyAsync(() -> funkos.stream().filter(funko -> funko.fechaLanzamiento().getYear() == 2023).collect(Collectors.toList()));
    }
    public CompletableFuture<Double> numberStitch(){
        return CompletableFuture.supplyAsync(() -> (double) funkos.stream().filter(funko -> funko.nombre().contains("Stitch")).count());
        }

    public CompletableFuture<List<Funko>> funkoStitch(){
        return CompletableFuture.supplyAsync(() -> funkos.stream().filter(funko -> funko.nombre().contains("Stitch")).collect(Collectors.toList()));
    }
}
