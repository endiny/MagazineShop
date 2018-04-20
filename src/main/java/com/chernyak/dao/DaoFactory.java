package com.chernyak.dao;

import com.chernyak.dao.jdbc.JdbcDaoFactory;

/**
 * Created by User on 19.06.2016.
 */
public abstract class DaoFactory {
    public abstract StudentDao createStudentDao();
    public abstract CourseDao createCourseDao();
    public abstract LecturerDao createLecturerDao();
    public abstract ParticipantDao createParticipantDao();

    public static DaoFactory getFactory() {
    	return JdbcDaoFactory.getFactory();
    }
   
}
