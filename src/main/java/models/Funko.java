package models;

import enums.Modelo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Funko {

    private final UUID cod;
    private final Long id2;
    private final String nombre;
    private final Modelo modelo;
    private final double precio;
    private final LocalDate fechaLanzamiento;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
