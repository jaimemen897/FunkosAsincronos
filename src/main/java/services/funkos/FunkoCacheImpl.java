package services.funkos;

import models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: Codificar los métodos
public class FunkoCacheImpl implements FunkoCache {
    private final Logger logger = LoggerFactory.getLogger(FunkoCacheImpl.class);


    @Override
    public void put(Long key, Funko value) {

    }

    @Override
    public Funko get(Long key) {
        return null;
    }

    @Override
    public void remove(Long key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void shutdown() {

    }
}
