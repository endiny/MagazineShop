package com.epam.jc.REST.Security;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;

import javax.servlet.http.HttpSession;
import javax.ws.rs.ForbiddenException;
import java.util.HashMap;

/**
 * Created on 13/04/16.
 *
 * @author Vladislav Boboshko
 */
public class LoginDispatcher {
    private LoginDispatcher() {
        roles = new HashMap<>( );
        roles.put(1L, "admin");
        roles.put(2L, "user");
    }
    private static LoginDispatcher instance;
    public static LoginDispatcher getInstance() {
        return (instance==null)?(instance = new LoginDispatcher()):instance;
    }

    public boolean authorize(String login, String password, HttpSession session) {
        User user = DAOFactory.getUserDAO().getUserByLogin(login);
        if (!(user.getPasswd().equals(password) && user.getLogin().equals(login))) {
            return false;
        }
        user.setPasswd("");
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(0);
        return true;
    }

    public boolean isUserInRole(HttpSession session, String role) {
        User user = (User)session.getAttribute("user");
        if (user == null) {
            return false;
        }
        return roleChecker(user.getRole()).equals(role);

    }

    private String roleChecker(Long id) {
        return roles.getOrDefault(id, "unknown");
    }

    private HashMap<Long, String> roles;
}