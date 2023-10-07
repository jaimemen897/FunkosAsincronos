package services.funkos;

import exceptions.FunkoNotFoundException;
import models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.funkos.FunkoRepositoryImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FunkosServiceImpl implements FunkosService {

    private static FunkosServiceImpl instance;
    private final FunkoCache cache;
    private final Logger logger = LoggerFactory.getLogger(FunkosServiceImpl.class);
    private final FunkoRepositoryImpl funkoRepository;
    private static final Lock lock = new ReentrantLock();

    private FunkosServiceImpl(FunkoRepositoryImpl funkoRepository) {
        this.funkoRepository = funkoRepository;
        this.cache = new FunkoCacheImpl();
    }

    public static FunkosServiceImpl getInstance(FunkoRepositoryImpl funkoRepository) {
        if (instance == null) {
            lock.lock();
            if (instance == null) {
                instance = new FunkosServiceImpl(funkoRepository);
            }
            lock.unlock();
        }
        return instance;
    }


    @Override
    public List<Funko> findAll() throws ExecutionException, InterruptedException {
        return funkoRepository.findAll().get();

    }

    @Override
    public List<Funko> findByNombre(String nombre) throws ExecutionException, InterruptedException {
        return funkoRepository.findByNombre(nombre).get();
    }

    @Override
    public Optional<Funko> findById(long id) throws ExecutionException, InterruptedException {
        Funko funko = cache.get(id);
        if (funko != null) {
            logger.debug("Funko obtenido de la cache con id: " + id);
            return Optional.of(funko);
        } else {
            logger.debug("Funko obtenido de la base de datos con id: " + id);
            return funkoRepository.findById(id).get();
        }
    }

    @Override
    public Funko save(Funko funko) throws ExecutionException, InterruptedException {
        funko = funkoRepository.save(funko).get();
        cache.put(funko.getId2(), funko);
        logger.debug("Funko guardado en la base de datos con id: " + funko.getId2());
        return funko;
    }

    @Override
    public Funko update(Funko funko) throws FunkoNotFoundException, ExecutionException, InterruptedException {
        logger.debug("Actualizando funko con id: " + funko.getId2());
        funko = funkoRepository.update(funko).get();
        cache.put(funko.getId2(), funko);
        return funko;
    }

    @Override
    public boolean deleteById(long id) throws ExecutionException, InterruptedException {
        logger.debug("Eliminando: " + id);
        var deleted = funkoRepository.deleteById(id).get();
        if (deleted) {
            cache.remove(id);
        }
        return deleted;
    }

    @Override
    public void deleteAll() throws ExecutionException, InterruptedException {
        logger.debug("Eliminando todos los funkos");
        funkoRepository.deleteAll().get();
        cache.clear();
    }

    public void close() throws SQLException {
        cache.shutdown();
    }
}
