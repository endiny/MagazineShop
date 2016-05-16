package com.epam.jc.DbController.DAO;

import com.epam.jc.DbController.ConnectionPool.ConnectionPool;
import com.epam.jc.DbController.ConnectionPool.PooledConnection;
import com.epam.jc.DbController.Entities.Order;
import com.epam.jc.DbController.Entities.Subscription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created on 08.04.16.
 *
 * @author Vladislav Boboshko
 */
public class SubscriptionDAO {
    private static final Logger logger = LogManager.getLogger(SubscriptionDAO.class.getName());
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
            String sql = "INSERT INTO subscriptions (order_id, magazine_id, months) VALUES (?,?,?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, subscription.getOrderId());
            st.setLong(2, subscription.getMagazineId());
            st.setLong(3, subscription.getMonths());
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
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
                        resultSet.getLong("months")
                ));
            }
            return subscriptions;
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<Subscription> getSubscription(Long orderId, Long magazineId) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "SELECT * FROM subscriptions where order_id=(?) AND magazine_id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, orderId);
            st.setLong(2, magazineId);
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("No subscription with orderId # " + orderId + " and magazineId #" +
                    magazineId + " is available");
            }
            return Optional.of(new Subscription(
                    resultSet.getLong("order_id"),
                    resultSet.getLong("magazine_id"),
                    resultSet.getLong("months")));
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public boolean updateSubscription(Subscription subscription) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "UPDATE subscriptions SET start_date=(?), months=(?) WHERE order_id=(?) and magazine_id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, subscription.getMonths());
            st.setLong(2, subscription.getOrderId());
            st.setLong(3, subscription.getMagazineId());
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean deleteSubscription(Subscription subscription) {
        try (PooledConnection conn = PooledConnection.wrap(connectionPool.takeConnection(),
                connectionPool.getFreeConnections(), connectionPool.getReservedConnections())) {
            String sql = "DELETE FROM subscriptions WHERE order_id=(?) and magazine_id=(?) and months=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, subscription.getOrderId());
            st.setLong(2, subscription.getMagazineId());
            st.setLong(3, subscription.getMonths());
            return (st.executeUpdate() != 0);
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
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
