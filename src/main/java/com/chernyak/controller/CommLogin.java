package com.chernyak.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommLogin extends Command {

     /**
     * Redirecting to "sign in" page
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */	
    @Override
	void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
	}

}
