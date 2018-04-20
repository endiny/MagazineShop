package com.chernyak.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chernyak.entity.Course;

/**
 * Created by Chernyak on 19.07.2016.
 */
public class CommGetCourses extends Command {
	
    /**
     * Showing all courses for students
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Course> result = clientService.getCourses();
		request.setAttribute("result", result);
		request.getServletContext().getRequestDispatcher("/WEB-INF/courseList.jsp").forward(request, response);
	}

}
