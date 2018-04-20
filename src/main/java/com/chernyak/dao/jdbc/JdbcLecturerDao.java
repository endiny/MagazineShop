package com.chernyak.dao.jdbc;

import com.chernyak.dao.LecturerDao;
import com.chernyak.entity.Lecturer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by Chernyak on 18.07.2016.
 */
public class JdbcLecturerDao extends JdbcGenericDao<Lecturer> implements LecturerDao {
    
    public static final String LECTURER_COLUMN_ID = "id";
    public static final String LECTURER_COLUMN_FIRST_NAME = "first_name";
    public static final String LECTURER_COLUMN_LAST_NAME = "last_name";
    public static final String LECTURER_COLUMN_PASSWORD = "password";
    
    @Override
    protected String getSelectQuery() {
		return "SELECT * FROM lecturer WHERE id = ?";
	}

	@Override
	protected String getInsertQuery() {
		return "INSERT INTO lecturer (first_name, last_name, password) VALUES (?, ?, ?)";
	}

	@Override
	protected String getUpdateQuery() {
		return "UPDATE lecturer SET first_name = ?, last_name = ?, password = ? WHERE id = ?";
	}

	@Override
	protected String getDeleteQuery() {
		return "DELETE FROM lecturer WHERE id = ?";
	}

	@Override
	protected void prepareStatementForCreate(PreparedStatement statement, Lecturer entity) throws SQLException {
		statement.setString(1, entity.getFirstName());
		statement.setString(2, entity.getLastName());
		statement.setString(3, entity.getPassword());
		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement, Lecturer entity) throws SQLException {
		statement.setString(1, entity.getFirstName());
		statement.setString(2, entity.getLastName());
		statement.setString(3, entity.getPassword());
		statement.setInt(4, entity.getId());
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement statement, Lecturer entity) throws SQLException {
		statement.setInt(1, entity.getId());
		
	}

	@Override
	protected List<Lecturer> parseResultSet(ResultSet rs) throws SQLException {
	    List<Lecturer> res = new ArrayList<>();
	    while( rs.next() ){
		try {	
		    Lecturer lecturer = new Lecturer();
		    lecturer.setId(rs.getInt(LECTURER_COLUMN_ID));
                    lecturer.setFirstName(rs.getString(LECTURER_COLUMN_FIRST_NAME));
                    lecturer.setLastName(rs.getString(LECTURER_COLUMN_LAST_NAME));
                    lecturer.setPassword(rs.getString(LECTURER_COLUMN_PASSWORD));
                    res.add(lecturer);
	            }catch(SQLException ex) {
        	    Logger.getLogger(JdbcLecturerDao.class.getName()).log(Level.ERROR, null, ex);
                    } 
                }
        return res;
	}
}
