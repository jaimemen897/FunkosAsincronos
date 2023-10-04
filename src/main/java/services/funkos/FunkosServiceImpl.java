package services.funkos;

import exceptions.FunkoNotFoundException;
import models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.funkos.FunkoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

//TODO: Implementar todos estos métodos
public class FunkosServiceImpl implements FunkosService{

    private static final int CACHE_SIZE = 10;

    private static FunkosServiceImpl instance;
    private final FunkoCache cache;
    private final Logger logger = LoggerFactory.getLogger(FunkosServiceImpl.class);
    private final FunkoRepository funkoRepository;

    private FunkosServiceImpl(FunkoRepository funkoRepository) {
        this.funkoRepository = funkoRepository;
        this.cache = new FunkoCacheImpl(CACHE_SIZE);
    }

    public static FunkosServiceImpl getInstance(FunkoRepository funkoRepository) {
        if (instance == null) {
            instance = new FunkosServiceImpl(funkoRepository);
        }
        return instance;
    }


    @Override
    public List<Funko> findAll() throws SQLException, ExecutionException, InterruptedException {
        return funkoRepository.findAll().get();
    }

    @Override
    public List<Funko> findByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException {
        return funkoRepository.findByNombre(nombre).get();
    }

    @Override
    public Optional<Funko> findById(long id) throws SQLException, ExecutionException, InterruptedException {
        return funkoRepository.findById(id).get();
    }

    @Override
    public Funko save(Funko alumno) throws SQLException, ExecutionException, InterruptedException {
        return funkoRepository.save(alumno).get();
    }

    @Override
    public Funko update(Funko alumno) throws SQLException, FunkoNotFoundException, ExecutionException, InterruptedException {
        return funkoRepository.update(alumno).get();
    }

    @Override
    public boolean deleteById(long id) throws SQLException, ExecutionException, InterruptedException {
        return funkoRepository.deleteById(id).get();
    }

    @Override
    public void deleteAll() throws SQLException, ExecutionException, InterruptedException {
        funkoRepository.deleteAll().get();
    }
}
