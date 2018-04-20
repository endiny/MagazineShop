package com.chernyak.dao.jdbc;

import com.chernyak.dao.ParticipantDao;
import com.chernyak.entity.Course;
import com.chernyak.entity.Lecturer;
import com.chernyak.entity.Participant;
import com.chernyak.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by Chernyak on 18.07.2016.
 */
public class JdbcParticipantDao extends JdbcGenericDao<Participant> implements ParticipantDao {

    public static final String PARTICIPANT_COLUMN_ID = "id_p";
    public static final String PARTICIPANT_COLUMN_GRADE = "grade";
    public static final String PARTICIPANT_COLUMN_COMMENT = "comment";
    public static final String PARTICIPANT_COLUMN_STUDENT_ID = "student_id";
    public static final String PARTICIPANT_COLUMN_COURSE_ID = "course_id";
    public static final String PARTICIPANT_LECTURER_ID = "lecturer_id";
    public static final String PARTICIPANT_LECTURER_FIRST_NAME = "fname";
    public static final String PARTICIPANT_LECTURER_LAST_NAME = "lname";
    

	@Override
	protected String getSelectQuery() {
		return "SELECT p.*, p.id AS id_p, c.*, s.*, "
		           + "l.first_name AS fname, l.last_name AS lname, l.password"
		           + " FROM participant p "
		           + "LEFT JOIN course c ON c.id = course_id "
		           + "LEFT JOIN student s ON s.id = student_id "
		           + "LEFT JOIN lecturer l ON c.lecturer_id = l.id WHERE p.id = ?";
	}

	@Override
	protected String getInsertQuery() {
		return "INSERT INTO participant (course_id, student_id) VALUES (?, ?)";
	}

	@Override
	protected String getUpdateQuery() {
		return "UPDATE participant SET course_id = ?, student_id = ?, grade = ?, comment = ? WHERE id = ?";
	}

	@Override
	protected String getDeleteQuery() {
		return "DELETE FROM participant WHERE id = ?";
	}

	@Override
	protected void prepareStatementForCreate(PreparedStatement statement, Participant entity) throws SQLException {
		statement.setInt(1, entity.getCourse().getId());
		statement.setInt(2, entity.getStudent().getId());
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement statement, Participant entity) throws SQLException {
		statement.setInt(1, entity.getCourse().getId());
		statement.setInt(2, entity.getStudent().getId());
		statement.setInt(3, entity.getGrade());
		statement.setString(4,entity.getComment());
		statement.setInt(5,entity.getId());
		
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement statement, Participant entity) throws SQLException {
		statement.setInt(1,entity.getId());
		
	}

	@Override
	protected List<Participant> parseResultSet(ResultSet rs) throws SQLException {
		List<Participant> res = new ArrayList<>();
        while( rs.next() ){
        	try{
        	    Participant paricipant = new Participant();
        	    paricipant.setId(rs.getInt(PARTICIPANT_COLUMN_ID));
        	    paricipant.setGrade(rs.getInt(PARTICIPANT_COLUMN_GRADE));
        	    paricipant.setComment(rs.getString(PARTICIPANT_COLUMN_COMMENT) );
        	    
        	    Student student = new Student();
		    student.setId(rs.getInt(PARTICIPANT_COLUMN_STUDENT_ID));
		    student.setFirstName(rs.getString(JdbcStudentDao.STUDENT_COLUMN_FIRST_NAME));
		    student.setLastName(rs.getString(JdbcStudentDao.STUDENT_COLUMN_LAST_NAME));
		    paricipant.setStudent(student);
				
		    Course course = new Course();
	            course.setId(rs.getInt(PARTICIPANT_COLUMN_COURSE_ID));
	            course.setName(rs.getString(JdbcCourseDao.COURSE_COLUMN_NAME));
	            course.setDate(rs.getString(JdbcCourseDao.COURSE_COLUMN_DATE));
	        	
	            Lecturer lecturer = new Lecturer();
	            lecturer.setId(rs.getInt(PARTICIPANT_LECTURER_ID));
	            lecturer.setFirstName(rs.getString(PARTICIPANT_LECTURER_FIRST_NAME));
	            lecturer.setLastName(rs.getString(PARTICIPANT_LECTURER_LAST_NAME));
	            lecturer.setPassword(rs.getString(JdbcLecturerDao.LECTURER_COLUMN_PASSWORD));
	            course.setLecturer(lecturer);
	            paricipant.setCourse(course);
                    res.add(paricipant);
                    }catch(SQLException ex) {
            	    Logger.getLogger(JdbcParticipantDao.class.getName()).log(Level.ERROR, null, ex);
                    throw ex;
                    } 
                }
        return res;
	}

    @Override
    public List<Participant> getParticipantByCourseId(int courseId) {
        List<Participant> list;
        String sql = "SELECT p.*, p.id AS id_p, c.*, s.*, "
		           + "l.first_name AS fname, l.last_name AS lname, l.password"
		           + " FROM participant p "
		           + "LEFT JOIN course c ON c.id = course_id "
		           + "LEFT JOIN student s ON s.id = student_id "
		           + "LEFT JOIN lecturer l ON c.lecturer_id = l.id WHERE course_id = ?";
        try (Connection connection = JdbcDaoFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            return list;
        } catch (SQLException e) {
            Logger.getLogger(JdbcParticipantDao.class.getName()).log(Level.ERROR, null, e);
            return null;
        } 
    }
	
}
