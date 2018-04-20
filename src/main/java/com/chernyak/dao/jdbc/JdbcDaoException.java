/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chernyak.dao.jdbc;

/**
 *
 * @author Chernyak
 */
public class JdbcDaoException extends Exception {

    public JdbcDaoException() {
    }
    
    public JdbcDaoException(String msg) {
    super(msg);
    }
}
