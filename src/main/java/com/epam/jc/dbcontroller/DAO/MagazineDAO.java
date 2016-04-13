package com.epam.jc.DbController.DAO;

import com.epam.jc.DbController.ConnectionPool.ConnectionPool;
import com.epam.jc.DbController.ConnectionPool.PooledConnection;
import com.epam.jc.DbController.Entities.Magazine;

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
public class MagazineDAO {
    private MagazineDAO() {}
    private static MagazineDAO instance;
    public static MagazineDAO getInstance() {
        return (instance==null)?(instance = new MagazineDAO()):instance;
    }

    public boolean addMagazine(Magazine magazine) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "INSERT INTO magazines (name, price, description) VALUES (?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, magazine.getName());
            st.setDouble(2, magazine.getPrice());
            st.setString(3, magazine.getDescription());
            st.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMagazine(Magazine magazine) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "UPDATE magazines SET name = (?), price = (?), description = (?) WHERE id=(?);";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, magazine.getName());
            st.setDouble(2, magazine.getPrice());
            st.setString(3, magazine.getDescription());
            st.setLong(4, magazine.getId());
            st.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Magazine getMagazine(Long id) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM magazines WHERE id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet result = st.executeQuery();
            if (!result.next()) {
                throw new SQLException("No magazine with id #" + id + " is available.");
            }
            return new Magazine(result.getLong("id"),
                    result.getString("name"),
                    result.getDouble("price"),
                    result.getString("description"));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new Magazine(0L, "", 0.0, "");
        }
    }

    public List<Magazine> getAllMagazines() {
        ConnectionPool instance = ConnectionPool.getInstance();
        List<Magazine> magazines = new ArrayList<>();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "SELECT * FROM magazines";
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                Long id = result.getLong("id");
                String name = result.getString("name");
                Double price = result.getDouble("price");
                String desc = result.getString("decription");
                magazines.add(new Magazine(id, name, price, desc));
            }
            return magazines;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean deleteMagazine(Long id) {
        ConnectionPool instance = ConnectionPool.getInstance();
        try (PooledConnection conn = PooledConnection.wrap(instance.takeConnection(),
                instance.getFreeConnections(), instance.getReservedConnections())) {
            String sql = "DELETE FROM magazines WHERE id=(?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            st.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMagazine(Magazine magazine) {
        return deleteMagazine(magazine.getId());
    }
}
