package com.epam.jc.DbController.DAO;

import com.epam.jc.DbController.ConnectionPool.ConnectionPool;
import com.epam.jc.DbController.ConnectionPool.PooledConnection;
import com.epam.jc.DbController.Entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */

// TODO: 08/04/16 Logging is absend

public class UserDAO {
    private UserDAO() {}
    private static UserDAO instance;
    public static UserDAO getInstance() {
        return (instance==null)?(instance=new UserDAO()):instance;
    }

    public boolean addUser(User user) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "INSERT INTO users (login, passwd, real_name, role_id) VALUES (?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, user.getLogin());
            st.setString(2, user.getPasswd());
            st.setString(3, user.getName());
            st.setString(4, user.getRole());
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<User> getUser(Long userId) {
        ConnectionPool instance = ConnectionPool.getInstance();

        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM users where id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, userId);
            ResultSet result = st.executeQuery();
            if (!result.next()) {
                throw new SQLException("No User with id #" + userId + " is available");
            }
            Long id = result.getLong("id");
            String login = result.getString("login");
            String passwd = result.getString("passwd");
            String name = result.getString("real_name");
            String role = result.getString("role_id");
            return Optional.of(new User(id, login, name, passwd, role));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> getUserByLogin(String login) {
        ConnectionPool instance = ConnectionPool.getInstance();

        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM users where login=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, login);
            ResultSet result = st.executeQuery();
            if (!result.next()) {
                throw new SQLException("No User with login #" + login + " is available");
            }
            Long id = result.getLong("id");
            String username = result.getString("login");
            String passwd = result.getString("passwd");
            String name = result.getString("real_name");
            String role = result.getString("role_id");
            return Optional.of(new User(id, username, name, passwd, role));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "DELETE from users WHERE id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(User user) {
        return deleteUser(user.getId());
    }

    public boolean updateUser(User user) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "UPDATE users SET login = (?), passwd = (?), real_name = (?), role_id = (?) WHERE id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, user.getLogin());
            st.setString(2, user.getPasswd());
            st.setString(3, user.getName());
            st.setString(4, user.getRole());
            st.setLong(5, user.getId());
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        ConnectionPool instance = ConnectionPool.getInstance();
        List<User> users = new ArrayList<>();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM users";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                Long id = result.getLong("id");
                String login = result.getString("login");
                String passwd = result.getString("passwd");
                String name = result.getString("name");
                String role = result.getString("role_id");
                users.add(new User(id, login, name, passwd, role));
            }
            return users;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}