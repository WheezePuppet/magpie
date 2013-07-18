<meta http-equiv="Content-Type" pageEncoding="utf-8" contentType="text/html; charset=utf-8">
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.OutputStreamWriter" %>
<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ include file="redirectHeader.jsp" %>
<html> 
<head> 
	<title>Magpie</title> 
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="student.css" />
	<link rel="stylesheet" type="text/css" href="preview.css" />
	<script type="text/javascript" src="js_lib/json2.js"></script> 
	<script type="text/javascript" src="js_lib/jquery.js"></script>
	<script type="text/javascript" src="js_lib/jquery.cookie.js"></script>
	<script type="text/javascript" src="student.js"></script>
</head> 
<body> 
	<%@ include file="header.jsp" %>
<% response.setContentType( "text/html; charset=utf-8" ); %>
<%!
    String printCard(Card newCard) {
        return "<tr><td class=question>" + newCard.getQuestion() +
            "</td><td class=separator>&#8212" + 
            "</td><td class=answer>" + newCard.getAnswer() +
            "</td></tr>";
    }
%>
<%!
    String printTableOfCards(ArrayList<Card> cards) {

        String html = "";
        html += "<div id=cardTable>";
        html += "<div id=firstHalfTableCards>";
        html += "<table border=0>";

        for (int i=0; i<(cards.size()+1)/2; i++) {
            html += printCard(cards.get(i));
        }
        html += "</table>";
        html += "</div>";
        html += "<div id=secondHalfTableCards>";
        html += "<table border=0>";

        for (int i=(cards.size()+1)/2; i<cards.size(); i++) {
            html += printCard(cards.get(i));
        }

        if (cards.size() % 2 == 1) {
            html += "<tr><td></td><td>.</td><td></td></tr>";
        }

        html += "</table>";
        html += "</div>";
        html += "</div>";

        return html;
    }
%>


	<div id=pagecontent> 
		<div id=title> 
			<%@include file="title.html"%>
		</div> 

<%
    ArrayList<Card> newCards = 
        ((Student) theUser).getNewCards();
    ArrayList<Card> recentlyMissedCards = 
        ((Student) theUser).getRecentlyMissedCards();

    if (newCards.size() > 0) {
        out.println("<div id=newCardsDiv>");
        out.println("<div id=newCardsTitle>New cards for today</div>");
        out.println(printTableOfCards(newCards));
        out.println("</div>");
    }

    if (newCards.size() > 0  &&  recentlyMissedCards.size() > 0) {
        out.println("<div><hr/></div>");
    }

    if (recentlyMissedCards.size() > 0) {
        out.println("<div id=recentlyMissedCardsDiv>");
        out.println("<div id=recentlyMissedTitle>" +
            "Cards you've recently missed</div>");
        out.println(printTableOfCards(recentlyMissedCards));
        out.println("</div>");
    }


    if (newCards.size() == 0  &&  recentlyMissedCards.size() == 0) {
		response.sendRedirect("student.jsp");
        return;
    }
%>
	</div> 


    <div id=button>
        <form method=get action=student.jsp >
            <input type=submit value="Okay!" />
        </form>
    </div>

	<div id="ad">This application is brought to you by the UMW Computer Science department</div> 
</body> 
</html> 
