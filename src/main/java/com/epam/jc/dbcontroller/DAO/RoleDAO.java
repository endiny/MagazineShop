package com.epam.jc.dbcontroller.DAO;

import com.epam.jc.dbcontroller.ConnectionPool.ConnectionPool;
import com.epam.jc.dbcontroller.ConnectionPool.PooledConnection;
import com.epam.jc.dbcontroller.Entities.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class RoleDAO {
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
            e.printStackTrace();
            return Collections.<Role>emptyList();
        }
    }

    public Role getRole(Long id) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM roles WHERE id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet result = st.executeQuery();
            return new Role(result.getLong("id"), result.getString("name"));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new Role(0L, "");
        }
    }
}
