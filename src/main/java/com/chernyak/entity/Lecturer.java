package com.chernyak.entity;

public class Lecturer {
	private int id;
	private String password;
	private String firstName;
	private String lastName;
	
	public Lecturer() {
		
	}
	
	public Lecturer(int id, String password, String firstName, String lastName) {
		this.id = id;
		this.password = password;
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



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Lecturer)) return false;

		Lecturer lecturer = (Lecturer) o;

		if (getId() != lecturer.getId())
			return false;
		if (getFirstName() != null ? !getFirstName().equals(lecturer.getFirstName()) : lecturer.getFirstName() != null)
			return false;
		return !(getLastName() != null ? !getLastName().equals(lecturer.getLastName()) : lecturer.getLastName() != null);
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
		return "" + (firstName != null ? firstName : "") +
				" " + (lastName != null ? lastName : "");
	}
	
		
}
