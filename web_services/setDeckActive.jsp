<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%
	String username = (String) session.getAttribute("username");
	int did = Integer.valueOf(request.getParameter("did"));
	boolean active = request.getParameter("active").equals("1");

	Deck deck = DeckManager.instance().get(did);
	if (deck == null) {
		out.print("failure");
		return;
	}

	Teacher teacher = TeacherManager.instance().get(username);

    // Spring 2011 -- assume all teachers have all courses
	if (teacher == null) {
	//if (teacher == null || !teacher.hasCourse(deck.getCourse())) {
		out.println("failure");
		return;
	}

	deck.setActive(active);
	out.print("success");
%>
