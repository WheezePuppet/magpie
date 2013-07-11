<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="edu.umw.cpsc.magpie.json.GetCardResponse" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%
	String username = (String) session.getAttribute("username");
	Student student = StudentManager.instance().get(username);

	if (student == null) {
		out.println(new GetCardResponse("nologin"));
		return;
	}

	int cid = Integer.parseInt(request.getParameter("cid"));
	String gradeString = request.getParameter("grade");
	int grade = gradeString == null ? 0 : Integer.parseInt(gradeString);
	boolean success = request.getParameter("success").equals("true") ? true : false;
	int time = Integer.parseInt(request.getParameter("time"));
	student.gradeCard(cid, grade, success, time);

	Card card = student.getNextCard();
	int timeStudying = student.getTime();
	out.println(new GetCardResponse(card == null ? "done" : "success", timeStudying, card));
%>
