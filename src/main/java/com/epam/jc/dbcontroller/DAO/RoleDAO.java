package com.epam.jc.DbController.DAO;

import com.epam.jc.DbController.ConnectionPool.ConnectionPool;
import com.epam.jc.DbController.ConnectionPool.PooledConnection;
import com.epam.jc.DbController.Entities.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class RoleDAO {
    private static final Logger logger = LogManager.getLogger(RoleDAO.class.getName());
    private RoleDAO() {}
    private static RoleDAO instance;
    public static RoleDAO getInstance() {
        return (instance==null)?(instance = new RoleDAO()):instance;
    }

    public List<Role> getAllRoles() {
        ConnectionPool instance = ConnectionPool.getInstance();
        List<Role> roles = new ArrayList<>();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM roles";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                roles.add(new Role(id, name));
            }
            return roles;
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<Role> getRole(Long id) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM roles WHERE id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet result = st.executeQuery();
            if (!result.next()) {
                throw new SQLException("No role with id #" + id + " is available.");
            }
            return Optional.of(new Role(result.getLong("id"), result.getString("name")));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
