package edu.umw.cpsc.magpie.core;

import java.sql.ResultSet;

public class Teacher extends User {
	public Teacher(ResultSet resultSet) {
		super(resultSet);
	}

	public Role getRole() {
		return Role.TEACHER;
	}

	public String getInsertValues() {
		return "(username, password, firstname, lastname, role) VALUES + ('" +
			username + "', '" + password + "', '" + firstName + "', '" + lastName + "', 'teacher')";
	}
}
