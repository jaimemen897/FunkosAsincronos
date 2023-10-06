package repositories.funkos;

import enums.Modelo;
import models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.database.DataBaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FunkoRepositoryImpl implements FunkoRepository {
    private static FunkoRepositoryImpl instance;
    private final Logger logger = LoggerFactory.getLogger(FunkoRepositoryImpl.class);
    private final DataBaseManager db;

    private FunkoRepositoryImpl(DataBaseManager db) {
        this.db = db;
    }

    public static FunkoRepositoryImpl getInstance(DataBaseManager db) {
        if (instance == null) {
            instance = new FunkoRepositoryImpl(db);
        }
        return instance;
    }

    @Override
    public CompletableFuture<Funko> save(Funko funko) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO FUNKOS (cod, id2, nombre, modelo, precio, fechaLanzamiento) VALUES (?, ?, ?, ?, ?, ?)";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, funko.getCod().toString());
                stmt.setLong(2, funko.getId2());
                stmt.setString(3, funko.getNombre());
                stmt.setString(4, funko.getModelo().toString());
                stmt.setDouble(5, funko.getPrecio());
                stmt.setObject(6, funko.getFechaLanzamiento());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            System.out.println("Insertando funko: " + funko);
            return funko;
        });
    }

    @Override
    public CompletableFuture<Funko> update(Funko funko) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "UPDATE FUNKOS SET nombre = ?, modelo = ?, precio = ?, fechaLanzamiento = ? WHERE id = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, funko.getNombre());
                stmt.setString(2, funko.getModelo().toString());
                stmt.setDouble(3, funko.getPrecio());
                stmt.setDate(4, java.sql.Date.valueOf(funko.getFechaLanzamiento()));
                stmt.setLong(5, funko.getId2());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            return funko;
        });
    }

    @Override
    public CompletableFuture<Optional<Funko>> findById(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Funko> optionalFunko = Optional.empty();
            String query = "SELECT * FROM FUNKOS WHERE id = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, id);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    optionalFunko = Optional.of(Funko.builder()

                            .cod(UUID.fromString(rs.getString("cod")))
                            .id2(rs.getLong("id2"))
                            .nombre(rs.getString("nombre"))
                            .modelo(Modelo.valueOf(rs.getString("modelo")))
                            .precio(rs.getDouble("precio"))
                            .fechaLanzamiento(rs.getDate("fechaLanzamiento").toLocalDate())
                            .build());
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            return optionalFunko;
        });
    }

    @Override
    public CompletableFuture<List<Funko>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            List<Funko> lista = new ArrayList<>();
            String query = "SELECT * FROM FUNKOS";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    lista.add(Funko.builder()

                            .cod(UUID.fromString(rs.getString("cod")))
                            .id2(rs.getLong("id2"))
                            .nombre(rs.getString("nombre"))
                            .modelo(Modelo.valueOf(rs.getString("modelo")))
                            .precio(rs.getDouble("precio"))
                            .fechaLanzamiento(rs.getDate("fechaLanzamiento").toLocalDate())
                            .build());
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            return lista;
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(Long idDelete) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "DELETE FROM FUNKOS WHERE id = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, idDelete);
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            return true;
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM FUNKOS";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<List<Funko>> findByNombre(String nombre) {
        return CompletableFuture.supplyAsync(() -> {
            List<Funko> lista = new ArrayList<>();
            String query = "SELECT * FROM FUNKOS WHERE nombre = ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {
                stmt.setString(1, nombre);
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    lista.add(Funko.builder()

                            .cod(UUID.fromString(rs.getString("cod")))
                            .id2(rs.getLong("id2"))
                            .nombre(rs.getString("nombre"))
                            .modelo(Modelo.valueOf(rs.getString("modelo")))
                            .precio(rs.getDouble("precio"))
                            .fechaLanzamiento(rs.getDate("fechaLanzamiento").toLocalDate())
                            .build());
                }
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            return lista;
        });
    }
}
