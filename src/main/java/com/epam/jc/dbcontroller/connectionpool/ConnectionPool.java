package com.epam.jc.DbController.ConnectionPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    public BlockingQueue<Connection> getFreeConnections() {
        return freeConnections;
    }

    public BlockingQueue<Connection> getReservedConnections() {
        return reservedConnections;
    }

    private BlockingQueue<Connection> freeConnections;
    private BlockingQueue<Connection> reservedConnections;

    private static ConnectionPool instance;

    public static ConnectionPool getInstance() {
        return (instance == null)?(instance = new ConnectionPool()):instance;
    }

    private String driverName;
    private String url;
    private String user;
    private String password;

    private int poolSize;

    private ConnectionPool() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("/Users/endiny/workspace/MagazineServer/src/main/resources/db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        driverName = properties.getProperty("driver", "org.postgresql.Driver");
        url = properties.getProperty("url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        user = properties.getProperty("user", "subscript");
        password = properties.getProperty("password", "111111");
        poolSize = Integer.parseInt(properties.getProperty("poolsize", "5"));

        initPoolData();
    }

    private void initPoolData() {
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driverName);
            reservedConnections = new ArrayBlockingQueue<>(poolSize);
            freeConnections = new ArrayBlockingQueue<>(poolSize);

            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                PooledConnection pooledConnection =
                        PooledConnection.wrap(connection, freeConnections, reservedConnections);
                freeConnections.add(pooledConnection);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQLException in ConnectionPool", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find database driver class", e);
        }
    }

    public Connection takeConnection() {
        Connection connection = null;
        try {
            connection = freeConnections.take();
            reservedConnections.add(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error connecting to the data source.", e);
        }
        return connection;
    }
}
