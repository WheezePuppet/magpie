<%
	String un = (String) session.getAttribute("username");
	if (edu.umw.cpsc.magpie.util.UserHelper.getUser(un) == null)
		response.sendRedirect("login.jsp");
%>
