package com.chernyak.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chernyak.model.ClientService;
import org.apache.log4j.Logger;

public abstract class Command {
    public ClientService clientService = ClientService.getInstance();
    public Logger log = Logger.getLogger(Command.class.getName());
    
    abstract void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
