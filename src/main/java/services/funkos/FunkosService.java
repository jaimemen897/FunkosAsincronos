package services.funkos;

import exceptions.FunkoNotFoundException;
import models.Funko;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface FunkosService {
    List<Funko> findAll() throws SQLException, ExecutionException, InterruptedException;

    List<Funko> findAllByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException;

    Optional<Funko> findById(long id) throws SQLException, ExecutionException, InterruptedException;

    Funko save(Funko alumno) throws SQLException, ExecutionException, InterruptedException;

    Funko update(Funko alumno) throws SQLException, FunkoNotFoundException, ExecutionException, InterruptedException;

    boolean deleteById(long id) throws SQLException, ExecutionException, InterruptedException;

    void deleteAll() throws SQLException, ExecutionException, InterruptedException;
}
