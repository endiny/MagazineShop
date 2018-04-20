package com.chernyak.entity;

public class Participant {
	private int id;
	private Student student;
	private Course course;
	private int grade;
	private String comment;
	
	public Participant() {
		
	}
	
	public Participant(int id, Student student, Course course,
					   int grade, String comment) {
		this.id = id;
		this.student = student;
		this.course = course;
		this.grade = grade;
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Participant)) return false;

		Participant that = (Participant) o;

		if (getId() != that.getId()) return false;
		if (getGrade() != that.getGrade()) return false;
		if (!getStudent().equals(that.getStudent())) return false;
		if (!getCourse().equals(that.getCourse())) return false;
		return !(getComment() != null ? !getComment().equals(that.getComment()) : that.getComment() != null);

	}

	@Override
	public int hashCode() {
		int result = getId();
		result = 31 * result + getStudent().hashCode();
		result = 31 * result + getCourse().hashCode();
		result = 31 * result + getGrade();
		result = 31 * result + (getComment() != null ? getComment().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Course: " + course.getName() +
				" " + course.getDate()
		         + ", participant id="
				 + id + "name: " +
				 student +
				", grade = " + grade +
				", comment = " + comment;
	}
}
