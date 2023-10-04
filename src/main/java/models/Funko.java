package models;

import enums.Modelo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class Funko {
    UUID id;
    UUID cod;
    Long id2;
    String nombre;
    Modelo modelo;
    double precio;
    LocalDate fechaLanzamiento;
}
