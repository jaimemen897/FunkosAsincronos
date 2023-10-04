package services.funkos;

import exceptions.FunkoNotFoundException;
import models.Funko;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

//TODO: Implementar todos estos métodos
public class FunkosServiceImpl implements FunkosService{
    @Override
    public List<Funko> findAll() throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public List<Funko> findAllByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Optional<Funko> findById(long id) throws SQLException, ExecutionException, InterruptedException {
        return Optional.empty();
    }

    @Override
    public Funko save(Funko alumno) throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Funko update(Funko alumno) throws SQLException, FunkoNotFoundException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public boolean deleteById(long id) throws SQLException, ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public void deleteAll() throws SQLException, ExecutionException, InterruptedException {

    }
}
