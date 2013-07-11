<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.text.DecimalFormat" %>
<%
	Collection<Card> cards = CardManager.instance().values();
	DecimalFormat format = new DecimalFormat("#.###");

	for (Card card : cards) {
		out.print(format.format(card.getAverageScore()) + ",");
		out.print(format.format(card.getAverageResponseTime()) + "\n");
	}
%>
