package com.chernyak.dao.jdbc;

import com.chernyak.dao.CourseDao;
import com.chernyak.entity.Course;
import com.chernyak.entity.Lecturer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by Chernyak on 18.07.2016.
 */
public class JdbcCourseDao extends JdbcGenericDao<Course> implements CourseDao {
    public static final String COURSE_COLUMN_ID = "id";
    public static final String COURSE_COLUMN_NAME = "name";
    public static final String COURSE_COLUMN_DATE = "date";
    public static final String COURSE_COLUMN_LECTURER_ID = "lecturer_id";

    
	@Override
	protected String getSelectQuery() {
		return "SELECT c.*, l.first_name, l.last_name, l.password "
        		+ "FROM course c LEFT JOIN lecturer l ON c.lecturer_id = l.id "
        		+ "WHERE c.id = ?";
	}

	@Override
	protected String getInsertQuery() {
		return "INSERT INTO course (name, date, lecturer_id) VALUES (?, ?, ?)";
	}

	@Override
	protected String getUpdateQuery() {
		return "UPDATE course SET name = ?, date = ?, lecturer_id = ? WHERE id = ?";
	}

	@Override
	protected String getDeleteQuery() {
		return "DELETE FROM course WHERE id = ?";
	}

	@Override
	protected void prepareStatementForCreate(PreparedStatement statement, Course entity) throws SQLException {
		statement.setString(1, entity.getName());
		statement.setString(2, entity.getDate());
		statement.setInt(3, entity.getLecturer().getId());	
		
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement, Course entity) throws SQLException {
		statement.setString(1, entity.getName());
		statement.setString(2, entity.getDate());
		statement.setInt(3,entity.getLecturer().getId());
		statement.setInt(4,entity.getId());
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement statement, Course entity) throws SQLException {
		statement.setInt(1,entity.getId());
	}

	@Override
	protected List<Course> parseResultSet(ResultSet rs) throws SQLException {
	    List<Course> res = new ArrayList<>();
            while( rs.next() ){
                try{
        	   Course course = new Course();
        	   course.setId(rs.getInt(COURSE_COLUMN_ID));
        	   course.setName(rs.getString(COURSE_COLUMN_NAME));
        	   course.setDate(rs.getString(COURSE_COLUMN_DATE));
                   Lecturer lecturer = new Lecturer();
                   lecturer.setId(rs.getInt(COURSE_COLUMN_LECTURER_ID));
                   lecturer.setFirstName(rs.getString(JdbcLecturerDao.LECTURER_COLUMN_FIRST_NAME));
                   lecturer.setLastName(rs.getString(JdbcLecturerDao.LECTURER_COLUMN_LAST_NAME));
                   lecturer.setPassword(rs.getString(JdbcLecturerDao.LECTURER_COLUMN_PASSWORD));
                   course.setLecturer(lecturer);
                   res.add(course);
                   }catch(SQLException ex) {
            	       Logger.getLogger(JdbcCourseDao.class.getName()).log(Level.ERROR, null, ex);
                       throw ex;
                   } 
            }
        return res;
	}
    
}
