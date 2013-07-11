<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.*"
         import="java.util.Collections"
         import="java.util.List" %>
<HTML>
<HEAD>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<script type="text/javascript" src="js_lib/jquery.js"></script>
	<script type="text/javascript" src="deck.js"></script>
	<title>Magpie</title>
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
<%
	int courseid = Integer.valueOf(request.getParameter("courseid"));
	Course course = CourseManager.instance().get(courseid);

	out.println("<H1>Enable or disable decks for " + course.getName() + "</H1>");
	out.println("<p>Changes will be saved automatically.</p>");

	List<Deck> decks = course.getDecks();
	Collections.sort(decks);
	for(Deck deck : decks) {
		String checkedString = deck.getActive() ? "checked=\"true\" " : "";
		out.print("<input type=\"checkbox\" class=\"deckCheckbox\" id=\"" + deck.getId() + "\" " + checkedString + "/>");
		out.println("<a href=\"wordsInDeck.jsp?deckid=" + deck.getId() + "\">" + deck.getName() + "</a><br />");
	}
%>
</BODY>
</HTML>
