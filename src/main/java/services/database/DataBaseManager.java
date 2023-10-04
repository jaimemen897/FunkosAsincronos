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


@Getter
public class DataBaseManager implements AutoCloseable {
    private static DataBaseManager instance;
    private final String dir = Paths.get("").toAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
    private final String propertiesPath = Paths.get("").toAbsolutePath() + File.separator + "resources" + File.separator + "database.properties";
    private HikariDataSource hikariDataSource;

    private DataBaseManager() {
        openConnection();
    }


    public static DataBaseManager getInstance() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        }
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    private void openConnection() {
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
