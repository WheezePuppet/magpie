<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.*" %>
<%
	String username = (String) session.getAttribute("username");
	User user = UserHelper.getUser(username);

	if (user == null)
		response.sendRedirect("login.jsp?failed=1");

	String password = request.getParameter("password");
	user.setPassword(new SHA1Generator().SHA1(password));

	if (user.getRole() == Role.STUDENT)
		response.sendRedirect("student.jsp");
	else if (user.getRole() == Role.TEACHER)
		response.sendRedirect("teacher.jsp");
	else
		System.err.println("MAGPIE: Unexpected state in proccessChangePassword.jsp.");
%>
