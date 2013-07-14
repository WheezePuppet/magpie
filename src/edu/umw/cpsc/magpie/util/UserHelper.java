package edu.umw.cpsc.magpie.util;

import edu.umw.cpsc.magpie.core.*;

public class UserHelper {
	public synchronized static User getUser(String username) {
		User user = StudentManager.instance().get(username);
		if (user == null)
			user = TeacherManager.instance().get(username);

		return user;
	}

	public synchronized static User getUser(String username, String password) {
		User user = getUser(username);
System.out.println("in user helper. User is " + user);

		if (user == null || !user.getPassword().equals(password))
			return null;

		return user;
	}
}
