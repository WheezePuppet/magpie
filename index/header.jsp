<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.*"
         import="java.util.Calendar" %>
<%
	User theUser = UserHelper.getUser((String) session.getAttribute("username"));
	Role role = theUser == null ? null : theUser.getRole();
    Course c = theUser.getCourses().get(0);
%>
<div id="header">
<% if(role == Role.STUDENT) { %>
	<a href="student.jsp">Practice!</a> |
	<a href="instructions.jsp">Instructions</a> |
	<a href="progress.jsp?courseid=<%= c.getId() %>">Progress</a> |
<% } if(role == Role.TEACHER) { %>
	<a href="index.jsp">Home</a> | 
<% } %>
	<a href="logout.jsp">Logout</a>
<% if (role == Role.STUDENT) { %>
<p>You have studied for <span id="time"><%= ((Student) theUser).getTime() %></span>  minutes today.</p>
<% } %>
</div>
