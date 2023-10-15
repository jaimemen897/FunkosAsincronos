package services.funkos;

import enums.Modelo;
import exceptions.Funko.FunkoNotFoundException;
import exceptions.Funko.FunkoNotStoragedException;
import models.Funko;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.funkos.FunkoRepositoryImpl;
import routes.Routes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunkosServiceImplTest {

    @Mock
    FunkoRepositoryImpl funkoRepository;

    @InjectMocks
    FunkosServiceImpl service;


    @Test
    void findAll() throws ExecutionException, InterruptedException {
        //Arrange
        var funkos = List.of(
                Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build(),
                Funko.builder().cod(UUID.randomUUID()).id2(96L).nombre("Mickey Mouse").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build()
        );
        when(funkoRepository.findAll()).thenReturn(CompletableFuture.completedFuture(funkos));

        //Act
        var result = service.findAll();

        //Assert
        assertAll("findAll",
                () -> assertEquals(result.size(), 2, "No se han recuperado dos funkos"),
                () -> assertEquals(result.get(0).getCod(), funkos.get(0).getCod(), "El primer funko no es el esperado"),
                () -> assertEquals(result.get(1).getCod(), funkos.get(1).getCod(), "El primer funko no es el esperado"),

                () -> assertEquals(result.get(0).getId2(), 95L, "El primer funko no es el esperado"),
                () -> assertEquals(result.get(1).getId2(), 96L, "El primer funko no es el esperado"),

                () -> assertEquals(result.get(0).getNombre(), "Rayo McQueen", "El primer funko no es el esperado"),
                () -> assertEquals(result.get(1).getNombre(), "Mickey Mouse", "El primer funko no es el esperado"),

                () -> assertEquals(result.get(0).getModelo(), Modelo.DISNEY, "El primer funko no es el esperado"),
                () -> assertEquals(result.get(1).getModelo(), Modelo.DISNEY, "El primer funko no es el esperado"),

                () -> assertEquals(result.get(0).getPrecio(), 100.0, "El primer funko no es el esperado"),
                () -> assertEquals(result.get(1).getPrecio(), 100.0, "El primer funko no es el esperado"),

                () -> assertEquals(result.get(0).getFechaLanzamiento(), LocalDate.parse("2021-10-07"), "El primer funko no es el esperado"),
                () -> assertEquals(result.get(1).getFechaLanzamiento(), LocalDate.parse("2021-10-07"), "El primer funko no es el esperado")
        );

        //Verify
        verify(funkoRepository, times(1)).findAll();
    }

    @Test
    void findByNombre() throws FunkoNotFoundException, ExecutionException, InterruptedException {
        //Arrange
        var funkos = List.of(Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build());

        when(funkoRepository.findByNombre("Rayo McQueen")).thenReturn(CompletableFuture.completedFuture(funkos));

        //Act
        var result = service.findByNombre("Rayo McQueen");

        //Assert
        assertAll("findByNombre",
                () -> assertEquals(result.size(), 1, "No se han recuperado dos funkos"),
                () -> assertEquals(result.get(0).getCod(), funkos.get(0).getCod(), "El primer funko no es el esperado"),
                () -> assertEquals(result.get(0).getId2(), 95L, "El primer funko no es el esperado"),
                () -> assertEquals(result.get(0).getNombre(), "Rayo McQueen", "El primer funko no es el esperado"),
                () -> assertEquals(result.get(0).getModelo(), Modelo.DISNEY, "El primer funko no es el esperado"),
                () -> assertEquals(result.get(0).getPrecio(), 100.0, "El primer funko no es el esperado"),
                () -> assertEquals(result.get(0).getFechaLanzamiento(), LocalDate.parse("2021-10-07"), "El primer funko no es el esperado")
        );

        //Verify
        verify(funkoRepository, times(2)).findByNombre("Rayo McQueen");
    }

    @Test
    void findById() throws FunkoNotFoundException, ExecutionException, InterruptedException {
        //Arrange
        var funko = Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.findById(95L)).thenReturn(CompletableFuture.completedFuture(Optional.of(funko)));

        //Act
        var result = service.findById(95L);

        //Assert
        assertAll("findById",
                () -> assertEquals(result.get().getCod(), funko.getCod(), "El primer funko no es el esperado"),
                () -> assertEquals(result.get().getId2(), 95L, "El primer funko no es el esperado"),
                () -> assertEquals(result.get().getNombre(), "Rayo McQueen", "El primer funko no es el esperado"),
                () -> assertEquals(result.get().getModelo(), Modelo.DISNEY, "El primer funko no es el esperado"),
                () -> assertEquals(result.get().getPrecio(), 100.0, "El primer funko no es el esperado"),
                () -> assertEquals(result.get().getFechaLanzamiento(), LocalDate.parse("2021-10-07"), "El primer funko no es el esperado")
        );

        //Verify
        verify(funkoRepository, times(2)).findById(95L);

    }

    @Test
    void findByIdNoExiste() throws FunkoNotFoundException, ExecutionException, InterruptedException {
        //Arrange
        var funko = Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.findById(95L)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        //Act and assert
        assertThrows(FunkoNotFoundException.class, () -> service.findById(95L));

        //Verify
        verify(funkoRepository, times(1)).findById(95L);
    }

    @Test
    void save() throws ExecutionException, FunkoNotStoragedException, InterruptedException {
        //Arrange
        var funko = Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.save(funko)).thenReturn(CompletableFuture.completedFuture(funko));

        //Act
        var result = service.save(funko);

        //Assert
        assertAll("save",
                () -> assertEquals(result.getCod(), funko.getCod(), "El primer funko no es el esperado"),
                () -> assertEquals(result.getId2(), 95L, "El primer funko no es el esperado"),
                () -> assertEquals(result.getNombre(), "Rayo McQueen", "El primer funko no es el esperado"),
                () -> assertEquals(result.getModelo(), Modelo.DISNEY, "El primer funko no es el esperado"),
                () -> assertEquals(result.getPrecio(), 100.0, "El primer funko no es el esperado"),
                () -> assertEquals(result.getFechaLanzamiento(), LocalDate.parse("2021-10-07"), "El primer funko no es el esperado")
        );

        //Verify
        verify(funkoRepository, times(1)).save(funko);
    }

    @Test
    void update() throws ExecutionException, InterruptedException, FunkoNotFoundException {
        //Arrange
        var funko = Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.update(funko)).thenReturn(CompletableFuture.completedFuture(funko));

        //Act
        var result = service.update(funko);

        //Assert
        assertAll("update",
                () -> assertEquals(result.getCod(), funko.getCod(), "El primer funko no es el esperado"),
                () -> assertEquals(result.getId2(), 95L, "El primer funko no es el esperado"),
                () -> assertEquals(result.getNombre(), "Rayo McQueen", "El primer funko no es el esperado"),
                () -> assertEquals(result.getModelo(), Modelo.DISNEY, "El primer funko no es el esperado"),
                () -> assertEquals(result.getPrecio(), 100.0, "El primer funko no es el esperado"),
                () -> assertEquals(result.getFechaLanzamiento(), LocalDate.parse("2021-10-07"), "El primer funko no es el esperado")
        );

        //Verify
        verify(funkoRepository, times(1)).update(funko);
    }

    @Test
    void updateNoExiste() throws ExecutionException, InterruptedException {
        //Arrange
        var funko = Funko.builder().cod(UUID.randomUUID()).id2(99L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.update(funko)).thenReturn(CompletableFuture.completedFuture(null));

        //Act and Assert
        assertThrows(FunkoNotFoundException.class, () -> service.update(funko));

        //Verify
        verify(funkoRepository, times(1)).update(funko);
    }


    @Test
    void deleteById() throws ExecutionException, InterruptedException, FunkoNotFoundException {
        //Arrange
        var funko = Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.deleteById(95L)).thenReturn(CompletableFuture.completedFuture(true));

        //Act
        var result = service.deleteById(95L);

        //Assert
        assertTrue(result, "No se ha eliminado el funko");

        //Verify
        verify(funkoRepository, times(1)).deleteById(95L);
    }

    @Test
    void deleteByIdNoExiste() throws FunkoNotFoundException, ExecutionException, InterruptedException {
        //Arrange
        Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.deleteById(95L)).thenReturn(CompletableFuture.completedFuture(false));

        var result = service.deleteById(95L);
    }


    @Test
    void deleteAll() throws ExecutionException, InterruptedException {
        //Arrange
        var funko = Funko.builder().cod(UUID.randomUUID()).id2(95L).nombre("Rayo McQueen").modelo(Modelo.DISNEY).precio(100.0).fechaLanzamiento(LocalDate.parse("2021-10-07")).build();

        when(funkoRepository.deleteAll()).thenReturn(CompletableFuture.completedFuture(null));

        service.deleteAll();

        verify(funkoRepository, times(1)).deleteAll();
    }
}