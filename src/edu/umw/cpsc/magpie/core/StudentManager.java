package edu.umw.cpsc.magpie.core;

import java.sql.ResultSet;

public class StudentManager extends AbstractItemManager<Student> {
	private StudentManager() {
		super("user", "role='student'", new IItemFactory<Student>() {
			public Student create(ResultSet resultSet) {
				return new Student(resultSet);
			}
		});
	}

	public synchronized Student get(String username) {
		for (Student student : items.values()) {
			if (student.getUsername().equals(username))
				return student;
		}

		return null;
	}

	public static StudentManager instance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final StudentManager INSTANCE = new StudentManager();
	}
}
