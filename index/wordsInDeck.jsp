<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.DateHelper"
         import="java.util.ArrayList"
         import="java.util.Date"
         import="java.util.Calendar" %>
<HTML>
<HEAD>
	<title>Magpie</title>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="course.css" />
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
<%!
	final int NUM_DAYS = 7;
	final int MILLISECONDS_PER_DAY = 1000*60*60*24;
%>
<%
	int deckId = Integer.valueOf(request.getParameter("deckid"));
    Deck deck = DeckManager.instance().get(deckId);
%>
<H1><%= deck.getName() %></H1>
<table border=1 cellpadding=5>
<tr><th>Card #</th><th>Content</th></tr>
<%
    ArrayList<Card> cards = deck.getCards();
    for (int i=0; i<cards.size(); i++) {
        Card card = cards.get(i);
        out.println("<tr><td>" + card.getId() + "</td><td>" + card.getQuestion() + "</td></tr>");
    }
    out.println("</table>");
%>
</BODY>
</HTML>
