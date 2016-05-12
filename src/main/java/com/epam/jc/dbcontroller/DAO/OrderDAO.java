package com.epam.jc.DbController.DAO;

import com.epam.jc.DbController.ConnectionPool.ConnectionPool;
import com.epam.jc.DbController.ConnectionPool.PooledConnection;
import com.epam.jc.DbController.Entities.Order;
import com.epam.jc.DbController.Entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class OrderDAO {
    private OrderDAO() {
        connectionPool = ConnectionPool.getInstance();
    }
    private static OrderDAO instance;
    public static OrderDAO getInstance() {
        return (instance==null)?(instance = new OrderDAO()):instance;
    }

    private ConnectionPool connectionPool;

    public boolean addOrder(Order order) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "INSERT INTO orders (user_id, order_timestamp, to_pay, address, is_paid) VALUES (?,?,?,?,?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, order.getUserId());
            st.setTimestamp(2, order.getOrderTime());
            st.setDouble(3, order.getToPay());
            st.setString(4, order.getShipAddress());
            st.setBoolean(5, order.isPaid());
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Order> getOrder(Long id) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "SELECT * FROM orders WHERE id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet result = st.executeQuery();
            if (!result.next()) {
                throw new SQLException("No order with id #" + id + " is available.");
            }
            return Optional.of(new Order(
                    result.getLong("id"),
                    result.getLong("user_id"),
                    result.getTimestamp("order_timestamp"),
                    result.getDouble("to_pay"),
                    result.getBoolean("is_paid"),
                    result.getString("address")));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        try(PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "SELECT * FROM orders";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                orders.add(new Order(
                        result.getLong("id"),
                        result.getLong("user_id"),
                        result.getTimestamp("order_timestamp"),
                        result.getDouble("to_pay"),
                        result.getBoolean("is_paid"),
                        result.getString("address")
                ));
            }
            return orders;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Order> getOrdersForUser(User user) {
        return getOrdersForUser(user.getId());
    }

    public List<Order> getOrdersForUser(Long userId) {
        List<Order> orders = new ArrayList<>();
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "SELECT * FROM orders WHERE user_id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, userId);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                orders.add(new Order(
                        resultSet.getLong("id"),
                        resultSet.getLong("user_id"),
                        resultSet.getTimestamp("order_timestamp"),
                        resultSet.getDouble("to_pay"),
                        resultSet.getBoolean("is_paid"),
                        resultSet.getString("address")
                ));
            }
            return orders;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean updateOrder(Order order) {
        try(PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "UPDATE orders SET user_id=(?), order_timestamp=(?), to_pay=(?), is_paid=(?), address=(?) WHERE id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, order.getUserId());
            st.setTimestamp(2, order.getOrderTime());
            st.setDouble(3, order.getToPay());
            st.setBoolean(4, order.isPaid());
            st.setString(5, order.getShipAddress());
            st.setLong(6, order.getId());
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrder(Order order) {
        return deleteOrder(order.getId());
    }

    public boolean deleteOrder(Long id) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "DELETE FROM orders WHERE id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Order> getLatestForUser(Long userId) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "SELECT * FROM orders WHERE user_id=(?) ORDER BY id DESC LIMIT 1;";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, userId);
            ResultSet result = st.executeQuery();
            if (!result.next()) {
                throw new SQLException("No orders for user with id #" + userId + " is available.");
            }
            return Optional.of(new Order(
                    result.getLong("id"),
                    result.getLong("user_id"),
                    result.getTimestamp("order_timestamp"),
                    result.getDouble("to_pay"),
                    result.getBoolean("is_paid"),
                    result.getString("address")));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
