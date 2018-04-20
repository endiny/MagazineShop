package com.chernyak.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chernyak.entity.Participant;

public class CommGrade extends Command {
	
     /**
     * Choosing student for comment
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Participant stud = clientService.getParticipantById(id);
		request.setAttribute("stud", stud);
		request.getServletContext().getRequestDispatcher("/WEB-INF/grade.jsp").forward(request, response);
    }

}
