package com.epam.jc.Common;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Created on 20.04.16.
 *
 * @author Vladislav Boboshko
 */
public class Authenticator {
    private final static Logger logger = LogManager.getLogger(Authenticator.class.getName());
    public static User authorize(String login, String password) {
        Optional<User> userOptional = DAOFactory.getUserDAO().getUserByLogin(login);
        if (!userOptional.isPresent()) {
            return null;
        }
        User user = userOptional.get();
        String encrypted = DigestUtils.md5Hex(DigestUtils.md5Hex(password));
        if (user.getLogin().equals(login) && user.getPasswd().equals(encrypted)) {
            logger.debug("User " + login + "has authorized");
            return user;
        } else {
            logger.debug("User" + login + "hasn't authorized");
            return null;
        }
    }

    public static User authorize(User user) {
        return authorize(user.getLogin(), user.getPasswd());
    }
}
