<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="edu.umw.cpsc.magpie.json.GetCardResponse" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %>
<%
	String username = (String) session.getAttribute("username");
	Student student = StudentManager.instance().get(username);

	if(student == null) {
		out.println(new GetCardResponse("nologin"));
	} else {
		CardAndReason cardAndReason = student.getNextCardAndReason();
		Card card = cardAndReason.getCard();
        String reason = cardAndReason.getReason();
		int timeStudying = student.getTime();
		out.println(new GetCardResponse(card == null ? "done" : "success",
timeStudying, card, student, reason));
	}
%>
