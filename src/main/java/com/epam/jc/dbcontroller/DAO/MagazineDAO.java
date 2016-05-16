package com.epam.jc.DbController.DAO;

import com.epam.jc.DbController.ConnectionPool.ConnectionPool;
import com.epam.jc.DbController.ConnectionPool.PooledConnection;
import com.epam.jc.DbController.Entities.Magazine;
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
public class MagazineDAO {
    private final static Logger logger = LogManager.getLogger(MagazineDAO.class.getName());
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
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            logger.error("Unable to add magazine");
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
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            logger.error("Unable to update magazine");
            return false;
        }
    }

    public Optional<Magazine> getMagazine(Long id) {
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
            return Optional.of(new Magazine(result.getLong("id"),
                    result.getString("name"),
                    result.getDouble("price"),
                    result.getString("description")));
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return Optional.empty();
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
                String desc = result.getString("description");
                magazines.add(new Magazine(id, name, price, desc));
            }
            return magazines;
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
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
            return (st.executeUpdate() != 0);
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean deleteMagazine(Magazine magazine) {
        return deleteMagazine(magazine.getId());
    }
}
