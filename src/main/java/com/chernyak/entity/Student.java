package com.chernyak.entity;

public class Student {
	private int id;
	private String firstName;
	private String lastName;
	
	public Student() {
		
	}
	
	public Student(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Student)) return false;

		Student student = (Student) o;

		if (getId() != student.getId()) return false;
		if (getFirstName() != null ? !getFirstName().equals(student.getFirstName()) : student.getFirstName() != null)
			return false;
		return !(getLastName() != null ? !getLastName().equals(student.getLastName()) : student.getLastName() != null);
	}

	@Override
	public int hashCode() {
		int result = getId();
		result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
		result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "" + firstName +
				" " + lastName;
	}
}
