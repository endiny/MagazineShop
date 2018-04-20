package com.chernyak.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chernyak.entity.Participant;

public class CommParticipList extends Command {
	
    /**
     * Showing all students in selected course
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer id = Integer.valueOf(request.getParameter("id"));
		List<Participant> pList = clientService.getParticipantByCourseId(id);
		request.setAttribute("pList", pList);
		request.getServletContext().getRequestDispatcher("/WEB-INF/participList.jsp").forward(request, response);
	}

}
