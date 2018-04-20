package com.chernyak.dao.jdbc;

import com.chernyak.dao.StudentDao;
import com.chernyak.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by Chernyak on 18.07.2016.
 */
public class JdbcStudentDao extends JdbcGenericDao<Student> implements StudentDao {

    public static final String STUDENT_COLUMN_ID = "id";
    public static final String STUDENT_COLUMN_FIRST_NAME = "first_name";
    public static final String STUDENT_COLUMN_LAST_NAME = "last_name";


    
	@Override
	protected String getSelectQuery() {
		return "SELECT * FROM student WHERE id = ?";
	}

	@Override
	protected String getInsertQuery() {
		return "INSERT INTO student (first_name, last_name) VALUES (?, ?)";
	}

	@Override
	protected String getUpdateQuery() {
		return "UPDATE student SET first_name = ?, last_name = ? WHERE id = ?";
	}

	@Override
	protected String getDeleteQuery() {
		return "DELETE FROM student WHERE id = ?";
	}

	@Override
	protected void prepareStatementForCreate(PreparedStatement statement, Student entity) throws SQLException {
		statement.setString(1, entity.getFirstName());
		statement.setString(2, entity.getLastName());		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement, Student entity) throws SQLException {
		statement.setString(1, entity.getFirstName());
		statement.setString(2, entity.getLastName());
		statement.setInt(3, entity.getId());
		
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement statement, Student entity) throws SQLException {
		statement.setInt(1, entity.getId());
	}

	@Override
	protected List<Student> parseResultSet(ResultSet rs) throws SQLException {
		List<Student> res = new ArrayList<>();
		while( rs.next() ){
		    try {	
			Student student = new Student();
			student.setId(rs.getInt(STUDENT_COLUMN_ID));
			student.setFirstName(rs.getString(STUDENT_COLUMN_FIRST_NAME));
			student.setLastName(rs.getString(STUDENT_COLUMN_LAST_NAME));
                        res.add(student);
		    }catch(SQLException ex) {
        	    Logger.getLogger(JdbcStudentDao.class.getName()).log(Level.ERROR, null, ex);
                } 
            }
        return res;
	}
        
    /**
     * Retrun first Student object with equal first name and last name
     * @param firstName
     * @param lastName
     * @return
     */
    @Override
    public Student getStudentByName(String firstName, String lastName) {
        List<Student> list;
        String sql = "SELECT * FROM student WHERE first_name = ? AND last_name = ?";
        try (Connection connection = JdbcDaoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            if (list == null || list.isEmpty()) {
            return null;
            }
            return list.get(0);
        } catch (SQLException e) {
            Logger.getLogger(JdbcStudentDao.class.getName()).log(Level.ERROR, null, e);
            return null;
        }
    }
    
}
