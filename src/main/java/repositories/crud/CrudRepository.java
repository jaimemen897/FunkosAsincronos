package repositories.crud;

import exceptions.Funko.FunkoNotFoundException;
import models.Funko;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface CrudRepository<T, ID> {
    CompletableFuture<Funko> save(T t) throws SQLException;

    CompletableFuture<Funko> update(T t) throws SQLException, FunkoNotFoundException;

    CompletableFuture<Optional<Funko>> findById(ID id) throws SQLException;

    CompletableFuture<List<Funko>> findByNombre(String nombre) throws SQLException;

    CompletableFuture<List<Funko>> findAll() throws SQLException;

    CompletableFuture<Boolean> deleteById(ID id) throws SQLException, FunkoNotFoundException, ExecutionException, InterruptedException;

    CompletableFuture<Void> deleteAll() throws SQLException;
}
