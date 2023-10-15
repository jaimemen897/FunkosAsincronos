package repositories.funkos;

import exceptions.Funko.FunkoNotFoundException;
import models.Funko;
import repositories.crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface FunkoRepository extends CrudRepository<Funko, Long> {
    CompletableFuture<Funko> save(Funko funko) throws SQLException;

    CompletableFuture<Funko> update(Funko funko) throws SQLException;

    CompletableFuture<Boolean> deleteById(Long id) throws SQLException, FunkoNotFoundException, ExecutionException, InterruptedException;

    CompletableFuture<Void> deleteAll() throws SQLException;

    CompletableFuture<Optional<Funko>> findById(Long id) throws SQLException;

    CompletableFuture<List<Funko>> findAll() throws SQLException;

    CompletableFuture<List<Funko>> findByNombre(String nombre) throws SQLException;
}