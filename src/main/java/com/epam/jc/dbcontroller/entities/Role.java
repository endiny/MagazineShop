package com.epam.jc.dbcontroller.Entities;

/**
 * Created on 07.04.16.
 *
 * @author Vladislav Boboshko
 */
public class Role {
    private Long id;
    private String name;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
