package edu.umw.cpsc.magpie.core;

import java.util.ArrayList;
import java.sql.ResultSet;

public class CourseManager extends AbstractItemManager<Course> {
	private CourseManager() {
		super("course", new IItemFactory<Course>() {
			public Course create(ResultSet resultSet) {
				return new Course(resultSet);
			}
		});
	}

	public static CourseManager instance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final CourseManager INSTANCE = new CourseManager();
	}
}
