package edu.umw.cpsc.magpie.util;

import edu.umw.cpsc.magpie.core.*;

public class DbHelper {
	public synchronized static void clearAll() {
		CardManager.instance().clear();
		CourseManager.instance().clear();
		DeckManager.instance().clear();
		StudentManager.instance().clear();
		TeacherManager.instance().clear();
	}

	public synchronized static void clearDbAll() {
		CardManager.instance().clearDb();
		CourseManager.instance().clearDb();
		DeckManager.instance().clearDb();
		StudentManager.instance().clearDb();
		TeacherManager.instance().clearDb();
	}

	public synchronized static void loadAll() {
		CardManager.instance().load();
		CourseManager.instance().load();
		DeckManager.instance().load();
		StudentManager.instance().load();
		TeacherManager.instance().load();
	}

	public synchronized static void loadSandbox() {
		MagpieConnection.instance().setConnection("jdbc:mysql://localhost/magpie_test", "matthew", "f00b4r");
		clearDbAll();
		loadAll();
	}
}

