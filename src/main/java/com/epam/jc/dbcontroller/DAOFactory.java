package com.epam.jc.dbcontroller;

import com.epam.jc.dbcontroller.DAO.*;
import com.epam.jc.dbcontroller.Entities.Magazine;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class DAOFactory {
    private static DAOFactory instance;
    private DAOFactory() {}
    public static DAOFactory getInstance() {
        return (instance==null)?(instance = new DAOFactory()):instance;
    }

    public MagazineDAO getMagazineDAO() {
        return MagazineDAO.getInstance();
    }

    public OrderDAO getOrderDAO() {
        return OrderDAO.getInstance();
    }

    public RoleDAO getRoleDAO() {
        return RoleDAO.getInstance();
    }

    public SubscriptionDAO getSubscriptionDAO() {
        return SubscriptionDAO.getInstance();
    }

    public UserDAO getUserDAO() {
        return UserDAO.getInstance();
    }

}
