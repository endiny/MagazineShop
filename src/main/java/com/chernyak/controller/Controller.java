package com.chernyak.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Map<String,Command> commands = new HashMap<>();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		commands.put("GET_COURSE_LIST_BY_LECTOR", new CommGetCourseByLecturer());
		commands.put("GET_COURSE_LIST", new CommGetCourses());
		commands.put("CHOOSE", new CommGetCourse());
		commands.put("REGISTER", new CommRegister());
		commands.put("PARTICIPIANT_LIST", new CommParticipList());
		commands.put("GRADE_PARTICIPIANT", new CommGrade());
		commands.put("WRITE_COMMENT", new CommWrite());
		commands.put("LOGIN", new CommLogin());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
         * @param request
         * @param response
         * @throws javax.servlet.ServletException
         * @throws java.io.IOException
	 */
        @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String command = request.getParameter("command");
		try{
                    commands.get(command).execute(request, response);
                }catch(RuntimeException ex) {
                    request.getRequestDispatcher("/WEB-INF/index.html").forward(request, response);
                }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         * @param request
         * @param response
         * @throws javax.servlet.ServletException
         * @throws java.io.IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	

}


