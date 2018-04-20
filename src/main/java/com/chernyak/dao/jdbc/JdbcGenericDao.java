package com.chernyak.dao.jdbc;

import com.chernyak.dao.GenericDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public abstract class JdbcGenericDao<T> implements GenericDao<T> {

    private static Logger logger = Logger.getLogger(JdbcGenericDao.class.getName());
    

    public JdbcGenericDao() {

    }
    
     /**
     * Gets the sql query to retrieve one record.
     * SELECT * FROM [Table] WHERE id = ?;
     * @return String query
     */
    protected abstract String getSelectQuery();

    /**
     * Gets the sql query to insert new records into the database.
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     * @return String query
     */
    protected abstract String getInsertQuery();

    /**
     * Gets the sql query to update records.
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     * @return String query
     */
    protected abstract String getUpdateQuery();

    /**
     * Gets the sql query to delete records from the database.
     * DELETE FROM [Table] WHERE id= ?;
     * @return String query
     */
    protected abstract String getDeleteQuery();

    /**
     * Sets arguments insert query in accordance with the values of the fields on object.
     * @param statement
     * @param entity
     * @throws java.sql.SQLException
     */
    protected abstract void prepareStatementForCreate(PreparedStatement statement, T entity) throws SQLException;

    /**
     * Sets the update query arguments in accordance with field values on object.
     * @param statement
     * @param entity
     * @throws java.sql.SQLException
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T entity) throws SQLException;
    
    /**
     * Sets the delete query arguments in accordance with field values on object.
     * @param statement
     * @param entity
     * @throws java.sql.SQLException
     */
    protected abstract void prepareStatementForDelete(PreparedStatement statement, T entity) throws SQLException;
    
     /**
     * Parses ResultSet object and returns a list of relevant content ResultSet.
     * @param rs
     * @return List
     * @throws java.sql.SQLException
     */
    protected abstract List<T> parseResultSet(ResultSet rs) throws SQLException;


    @Override
    public void create(T object) {
        String sql = getInsertQuery();
        try (Connection connection = JdbcDaoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForCreate(statement, object);
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new JdbcDaoException("On insert modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, null, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T object) {
        String sql = getUpdateQuery();
        try (Connection connection = JdbcDaoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new JdbcDaoException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, null, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(T object) {
        String sql = getDeleteQuery();
        try (Connection connection = JdbcDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForDelete(statement, object);
            int count = statement.executeUpdate();
            if (count > 1) {
                throw new JdbcDaoException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, null, e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public T find(int key) {
        List<T> list;
        String sql = getSelectQuery();
        try (Connection connection = JdbcDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            if (list == null || list.isEmpty()) {
            return null;
            }
            if (list.size() > 1) {
            throw new JdbcDaoException("Received more than one record.");
            }
            return list.get(0);
        } catch (Exception e) {
            logger.log(Level.ERROR, null, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findAll() {
        List<T> list;
        String sql = getSelectQuery().split(" WHERE")[0];
        try (Connection connection = JdbcDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            logger.log(Level.ERROR, null, e);
            throw new RuntimeException(e);
        }
        return list;
    }

}