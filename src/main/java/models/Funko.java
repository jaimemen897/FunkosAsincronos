package models;


import enums.Modelo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;


@Builder
public record Funko(UUID cod, String nombre, Modelo modelo, double precio, LocalDate fechaLanzamiento) {
}
