package com.epam.jc.DbController.ConnectionPool;

import org.junit.Test;

import java.sql.Statement;

import static org.junit.Assert.*;

/**
 * Created on 06.04.16.
 *
 * @author Vladislav Boboshko
 */
public class ConnectionPoolTest {

    @Test
    public void connectionTest() throws Exception {
        ConnectionPool instance = ConnectionPool.getInstance();
        ConnectionPool instance2 = ConnectionPool.getInstance();
        assertTrue(instance == instance2);
        PooledConnection connp = PooledConnection.wrap(instance.takeConnection(), instance.getFreeConnections(),
                instance.getReservedConnections());
        assertNotNull(connp);
        Statement stat = connp.createStatement();
        assertNotNull(stat);
        stat.addBatch("INSERT INTO roles (name) VALUES (\"test\");");
        stat.addBatch("INSERT INTO roles (name) VALUES (\"test1\");");
        stat.addBatch("SELECT * FROM roles WHERE id >=3;");
        stat.executeBatch();
    }
}