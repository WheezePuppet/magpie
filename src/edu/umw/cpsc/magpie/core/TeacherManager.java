package edu.umw.cpsc.magpie.core;

import java.sql.ResultSet;

public class TeacherManager extends AbstractItemManager<Teacher> {
	private TeacherManager() {
		super("user", "role='teacher'", new IItemFactory<Teacher>() {
			public Teacher create(ResultSet resultSet) {
				return new Teacher(resultSet);
			}
		});
	}

	public synchronized Teacher get(String username) {
		for (Teacher teacher : items.values()) {
			if (teacher.getUsername().equals(username))
				return teacher;
		}

		return null;
	}

	public static TeacherManager instance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final TeacherManager INSTANCE = new TeacherManager();
	}
}
