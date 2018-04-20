package com.chernyak.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chernyak.entity.Course;

public class CommGetCourse extends Command {
	
    /**
     * Registering student to course 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Course cour = clientService.getCourseById(id);
		request.setAttribute("cour", cour);
		request.getServletContext().getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
	}

}
