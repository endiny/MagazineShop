package com.epam.jc.dbcontroller.DAO;

import com.epam.jc.dbcontroller.ConnectionPool.ConnectionPool;
import com.epam.jc.dbcontroller.ConnectionPool.PooledConnection;
import com.epam.jc.dbcontroller.Entities.Order;
import com.epam.jc.dbcontroller.Entities.Subscription;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 08.04.16.
 *
 * @author Vladislav Boboshko
 */
public class SubscriptionDAO {
    private SubscriptionDAO() {
        connectionPool = ConnectionPool.getInstance();
    }
    private static SubscriptionDAO instance;

    public static SubscriptionDAO getInstance() {
        return (instance==null)?(instance = new SubscriptionDAO()):instance;
    }

    private ConnectionPool connectionPool;

    public boolean addSubscription(Subscription subscription) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "INSERT INTO subscriptions (order_id, magazine_id, start_date, months) VALUES (?,?,?,?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, subscription.getOrderId());
            st.setLong(2, subscription.getMagazineId());
            st.setDate(3, subscription.getStartDate());
            st.setLong(4, subscription.getMonths());
            st.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Subscription> getSubscriptionsByOrder(Order order) {
        return getSubscriptionByOrder(order.getId());
    }

    public List<Subscription> getSubscriptionByOrder(Long orderId) {
        List<Subscription> subscriptions = new ArrayList<>();
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "SELECT * FROM subscriptions WHERE order_id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, orderId);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                subscriptions.add(new Subscription(
                        resultSet.getLong("order_id"),
                        resultSet.getLong("magazine_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getLong("months")
                ));
            }
            return subscriptions;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Subscription getSubscription(Long orderId, Long magazineId) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "SELECT * FROM subscriptions where order_id=(?) AND magazine_id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, orderId);
            st.setLong(2, magazineId);
            ResultSet resultSet = st.executeQuery();
            return new Subscription(
                    resultSet.getLong("order_id"),
                    resultSet.getLong("magazine_id"),
                    resultSet.getDate("start_date"),
                    resultSet.getLong("months"));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new Subscription(0L, 0L, new Date(0), 0L);
        }
    }

    public boolean updateSubscription(Subscription subscription) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "UPDATE subscriptions SET start_date=(?), months=(?) WHERE order_id=(?) and magazine_id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setDate(1, subscription.getStartDate());
            st.setLong(2, subscription.getMonths());
            st.setLong(3, subscription.getOrderId());
            st.setLong(4, subscription.getMagazineId());
            st.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubscription(Subscription subscription) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "DELETE FROM subscriptions WHERE order_id=(?) and magazine_id=(?) and start_date=(?) and months=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, subscription.getOrderId());
            st.setLong(2, subscription.getMagazineId());
            st.setDate(3, subscription.getStartDate());
            st.setLong(4, subscription.getMonths());
            st.execute();
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteSubscriptionsForOrder(Order order) {
        return deleteSubscriptionsForOrder(order.getId());
    }

    public boolean deleteSubscriptionsForOrder(Long orderId) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "DELETE FROM subscriptions WHERE order_id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, orderId);
            st.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
