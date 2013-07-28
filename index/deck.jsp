<meta http-equiv="Content-Type" pageEncoding="utf-8" contentType="text/html; charset=utf-8">
<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.*"
         import="java.util.Collections"
         import="java.util.List" %>
<HTML>
<HEAD>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="deck.css" />
	<script type="text/javascript" src="js_lib/jquery.js"></script>
	<script type="text/javascript" src="deck.js"></script>
	<title>Magpie</title>
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
<% 
    response.setContentType( "text/html; charset=utf-8" );

	int deckid = Integer.valueOf(request.getParameter("id"));
	Deck deck = DeckManager.instance().get(deckid);

	out.println("<H1>" + deck.getName() + "</H1>");
    List<Card> cards = deck.getCards();

    out.println("<script>");
    out.println("MAGPIE = {};");
    out.println("var loadCardStats = function() {");
    out.println("MAGPIE.cardStats = new Array();");
    for (int i=0; i<cards.size(); i++) {
        Card card = cards.get(i);
        String question = card.getQuestion().replaceAll("\n","<br/>");
        String answer = card.getAnswer().replaceAll("\n","<br/>");
        Card.Stats stats = card.getStatsFor((Student)theUser);
        out.println("MAGPIE.cardStat = {};");
        out.println("MAGPIE.cardStat.question = \"" + question + "\";");
        out.println("MAGPIE.cardStat.answer = \"" + answer + "\";");
        out.println("MAGPIE.cardStat.numSuccessfulReviews = " + 
            stats.numSuccessfulReviews + ";");
        out.println("MAGPIE.cardStat.numReviews = " + 
            stats.numReviews + ";");
        out.println("MAGPIE.cardStat.rate = " + 
            ((double)stats.numSuccessfulReviews / stats.numReviews * 100.0)
            + ";");
        out.println("MAGPIE.cardStat.averageReviewTime = " +
            (int) stats.averageReviewTime + ";");
        out.println("MAGPIE.cardStat.mostRecentDate = \"" + 
            stats.mostRecentDate + "\";");
        out.println("MAGPIE.cardStats.push(MAGPIE.cardStat);");
    }
    out.println("}");
    out.println("</script>");
%>
<center>
<div>
    <table id=cardTable border=1 cellpadding=1 cellspacing=1 >
    </table>
</div>
</center>

</BODY>
</HTML>
