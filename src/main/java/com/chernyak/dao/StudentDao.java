package com.chernyak.dao;

import com.chernyak.entity.Student;

/**
 * Student DAO interface
 */
public interface StudentDao extends GenericDao<Student> {
    
    Student getStudentByName(String firstName, String lastName);
}
