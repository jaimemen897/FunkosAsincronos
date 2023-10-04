package repositories.funkos;

import models.Funko;
import repositories.crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FunkoRepository extends CrudRepository<Funko, Long> {
    CompletableFuture<List<Funko>> findByNombre(String nombre) throws SQLException;
}
