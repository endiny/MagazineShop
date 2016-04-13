package com.epam.jc.DbController.DAO;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 08/04/16.
 *
 * @author Vladislav Boboshko
 */
public class UserDAOTest {
    @Test
    public void crudTest() throws Exception {
        UserDAO userDAO = DAOFactory.getUserDAO();
        User endiny = userDAO.getUser(1L);
        assertEquals(endiny.getLogin(), "endiny");
    }
}