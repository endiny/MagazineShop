package com.epam.jc.dbcontroller.entities;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */


public class User {
    private Long id;
    private String login;
    private String name;
    private String passwd;

    public User(String login, String name, String passwd, String role) {
        this.login = login;
        this.name = name;
        this.passwd = passwd;
        this.role = role;
    }

    public Long getId() {
        return id;

    }

    public String getLogin() {
        return login;

    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;
}
