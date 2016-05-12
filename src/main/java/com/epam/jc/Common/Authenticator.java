package com.epam.jc.Common;

import com.epam.jc.DbController.DAOFactory;
import com.epam.jc.DbController.Entities.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Optional;

/**
 * Created on 20.04.16.
 *
 * @author Vladislav Boboshko
 */
public class Authenticator {
    public static User authorize(String login, String password) {
        Optional<User> userOptional = DAOFactory.getUserDAO().getUserByLogin(login);
        if (!userOptional.isPresent()) {
            return null;
        }
        User user = userOptional.get();
        String encrypted = DigestUtils.md5Hex(DigestUtils.md5Hex(password));
        if (user.getLogin().equals(login) && user.getPasswd().equals(encrypted)) {
            return user;
        } else {
            return null;
        }
    }

    public static User authorize(User user) {
        return authorize(user.getLogin(), user.getPasswd());
    }
}
