package DAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;


public class DBConnection {
    private static final String CONFIG_FILE_PATH = "dbConnection.properties";

    public static DataSource getDataSource() {
        Properties properties = new Properties();
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            if (inputStream == null) {
                throw new IOException("Unable to find the dbConnection.properties file.");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            return null;
        }

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(properties.getProperty("db.url"));
        dataSource.setUser(properties.getProperty("db.user"));
        dataSource.setPassword(properties.getProperty("db.password"));
        return dataSource;
    }

    private DBConnection() {
        throw new IllegalStateException("DBConnection class");
    }
    }