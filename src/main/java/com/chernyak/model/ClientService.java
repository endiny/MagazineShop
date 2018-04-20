package com.chernyak.model;

import com.chernyak.dao.*;
import com.chernyak.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class with with methods, which works  
 */
public class ClientService {
	
	private static ClientService instance = new ClientService();
	private static DaoFactory daoFactory = DaoFactory.getFactory();

	private ClientService(){
	}
	
	public static ClientService getInstance(){
		return instance;
	}

	public Lecturer getLecturerById(int id) {
		LecturerDao lecturerDao = daoFactory.createLecturerDao();
		return lecturerDao.find(id);
	}

        public Student getStudentById(int id) {
		StudentDao studentDao = daoFactory.createStudentDao();
		return studentDao.find(id);
	}

	public Course getCourseById(int id) {
		CourseDao courseDao = daoFactory.createCourseDao();
		return courseDao.find(id);
	}

	public Participant getParticipantById(int id) {
		ParticipantDao participantDao = daoFactory.createParticipantDao();
		return participantDao.find(id);
	}
	
	public List<Course> getCourses(){
            CourseDao courseDao = daoFactory.createCourseDao();
            return courseDao.findAll();
	}

	public List<Participant> getParticipants() {
		ParticipantDao participantDao = daoFactory.createParticipantDao();
		return participantDao.findAll();
	}

	private Student getStudentByName(String firstName, String lastName) {
		StudentDao studentDao = daoFactory.createStudentDao();
                Student st = studentDao.getStudentByName(firstName, lastName);
                if (st != null) {
                   return st;
                } else {
                    st = new Student();
                    st.setFirstName(firstName);
                    st.setLastName(lastName);
		    studentDao.create(st);
                    st = studentDao.getStudentByName(firstName, lastName);
                }
                return st;
	}

	public boolean registerStudent(String firstName, String lastName, int courseId) {
		Student student = getStudentByName(firstName, lastName);
		Course course = getCourseById(courseId);
		ParticipantDao participantDao = daoFactory.createParticipantDao();
                Participant participant = new Participant();
                participant.setCourse(course);
                participant.setStudent(student);
		participantDao.create(participant);
		return true;
	}

	public List<Course> getCoursesByLecturer(int lecturerId) {
		List<Course> courses = new ArrayList<>();
		for(Course c : getCourses()) {
			if(c.getLecturer().getId() == lecturerId) {
				courses.add(c);
			}
		}
		return courses;
	}

	public List<Participant> getParticipantByCourseId(int courseId) {
		List<Participant> participants = new ArrayList<>();
                ParticipantDao participantDao = daoFactory.createParticipantDao();
                return participants = participantDao.getParticipantByCourseId(courseId);
	}

	public boolean gradeStudent(int participantId, int grade, String comment) {
		Participant participant = getParticipantById(participantId);
		participant.setGrade(grade);
		participant.setComment(comment);
		ParticipantDao participantDao = daoFactory.createParticipantDao();
		participantDao.update(participant);
		return true;
	}

}
