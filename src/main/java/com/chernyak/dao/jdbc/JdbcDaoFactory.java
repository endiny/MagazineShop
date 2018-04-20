package com.chernyak.dao.jdbc;

import com.chernyak.dao.*;


import java.sql.Connection;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.*;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Created by User on 19.06.2016.
 */
public class JdbcDaoFactory extends DaoFactory {
 
    private static Logger logger = Logger.getLogger(JdbcDaoFactory.class.getName());
    private static DataSource dataSource;
    private static JdbcDaoFactory instance;

    
    private JdbcDaoFactory() {
        try{
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/ElectivePool");
        }catch (Exception e) {
            logger.log(Level.ERROR, null, e);
            throw new RuntimeException(e);
        }
    }  
    
    public static synchronized JdbcDaoFactory getFactory() {
        if(instance == null) {
           instance = new JdbcDaoFactory();
        }
        return instance;
    }
    
    public static Connection getConnection() {
    	try{
    		return dataSource.getConnection();
        }catch (Exception e) {
        	logger.log(Level.ERROR, null, e);
        	throw new RuntimeException(e);
        }
    }
    
    @Override
    public StudentDao createStudentDao() {
        return new JdbcStudentDao();
    }

    @Override
    public CourseDao createCourseDao() {
        return new JdbcCourseDao();
    }

    @Override
    public LecturerDao createLecturerDao() {
        return new JdbcLecturerDao();
    }

    @Override
    public ParticipantDao createParticipantDao() {
        return new JdbcParticipantDao();
    }
}
