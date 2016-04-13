package com.epam.jc.DbController;

import com.epam.jc.DbController.DAO.*;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class DAOFactory {

    public static MagazineDAO getMagazineDAO() {
        return MagazineDAO.getInstance();
    }

    public static OrderDAO getOrderDAO() {
        return OrderDAO.getInstance();
    }

    public static RoleDAO getRoleDAO() {
        return RoleDAO.getInstance();
    }

    public static SubscriptionDAO getSubscriptionDAO() {
        return SubscriptionDAO.getInstance();
    }

    public static UserDAO getUserDAO() {
        return UserDAO.getInstance();
    }

}
