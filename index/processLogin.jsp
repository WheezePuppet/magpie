<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.*" %>
<%
	String username = request.getParameter("username");
	if (username.endsWith("@mail.umw.edu"))
		username = username.substring(0, username.lastIndexOf("@mail.umw.edu"));
	else if (username.endsWith("@umw.edu"))
		username = username.substring(0, username.lastIndexOf("@umw.edu"));
		
	String debug = request.getParameter("debug");
	String password = request.getParameter("password");
	if (debug == null || !debug.equals("true"))
		password = new SHA1Generator().SHA1(password);

System.out.println("Calling userhelper with " + username + ", " + password);
	User user = UserHelper.getUser(username, password);

	if (user == null) {
		response.sendRedirect("login.jsp?failed=1");
		return;
	}

	session.setAttribute("username", username);
	if (user.getRole() == Role.STUDENT) {
		Cookie c = new Cookie("gradingGroup", ((Student) user).getGradingGroup().toDbString());
		c.setMaxAge(60*60*24*7*52);
		response.addCookie(c);
	}

	if (user.dueForPasswordChange())
		response.sendRedirect("changePassword.jsp");
	else if (user.getRole() == Role.STUDENT)
		response.sendRedirect("preview.jsp");
	else if (user.getRole() == Role.TEACHER)
		response.sendRedirect("teacher.jsp");
	else
		System.err.println("MAGPIE: Unexpected state in proccessLogin.jsp.");
%>
