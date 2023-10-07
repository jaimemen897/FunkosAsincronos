package services.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Getter
public class DataBaseManager implements AutoCloseable {
    private static DataBaseManager instance;
    private final String dir = "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    private HikariDataSource hikariDataSource;
    private static final Lock lock = new ReentrantLock();

    private DataBaseManager() {
        openConnection();
    }


    public static synchronized DataBaseManager getInstance() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        }
        if (instance == null) {
            lock.lock();
            if (instance == null) {
                instance = new DataBaseManager();
            }
            lock.unlock();
        }
        return instance;
    }

    private synchronized void openConnection() {
        try {
            InputStream dbProps = ClassLoader.getSystemResourceAsStream("database.properties");
            Properties properties = new Properties();
            properties.load(dbProps);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));

            hikariDataSource = new HikariDataSource(config);

            Connection connection = hikariDataSource.getConnection();

            Reader reader = new BufferedReader(new FileReader(dir + properties.getProperty("db.init")));
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(reader);

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de propiedades: " + e.getMessage());
        }
    }


    @Override
    public void close() {
        hikariDataSource.close();
    }


    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Error al obtener la conexión: " + e.getMessage());
            return null;
        }
    }

    }
