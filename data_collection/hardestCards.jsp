<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="edu.umw.cpsc.magpie.json.GetCardResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
	List<Card> hardestCards = new ArrayList<Card>();

	int totalStudents = StudentManager.instance().size();

	for (Card card : CardManager.instance().values()) {
		double addedEasiness = 0;

		for (Student student : StudentManager.instance().values()) {
			addedEasiness += student.getEasinessFor(card);
		}

		double averageEasiness = addedEasiness / totalStudents;

		if (averageEasiness < 2.46) {
			hardestCards.add(card) ;
		}
	}

	out.println(hardestCards.size() + "<br />");
	for (Card card : hardestCards) {
		out.println(card.getQuestion() + " &nbsp;&nbsp;&nbsp; " + card.getId() + "<br />");
	}
%>
