<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="java.util.List" %>
<HTML>
<HEAD>
	<title>Magpie</title>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="course.css" />
	<link rel="stylesheet" type="text/css" href="decks.css" />
	<script type="text/javascript" src="js_lib/jquery.js"></script>
	<script type="text/javascript" src="decks.js"></script>
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
<%
    List<Course> courses = theUser.getCourses();
    StringBuilder builder = new StringBuilder(
        "<FORM METHOD=POST ACTION=decksController.jsp>");
    
    for (int i=0; i<courses.size(); i++) {
        Course course = courses.get(i);
	    out.println("<H1>Decks for " + course.getName() + ":</H1>");

        builder.append(
            "<TABLE class=decks cellpadding=\"2\" cellspacing=\"2\" " +
            "border=\"1\">");
        builder.append("<TR><TH>Deck</TH><TH>Active</TH><TH>Color</TH></TR>");

        List<Deck> decks = course.getDecks();
        for (int j=0; j<decks.size(); j++) {
            Deck deck = decks.get(j);
            builder.append("<TR><TD><a href=\"deck.jsp?id=" + deck.getId() + 
                "\">" + deck.getName() + "</TD>" +
                "<TD class=activeCheckbox>" +
                buildActiveCheckbox(course.getId(), deck.getId(), deck) +
                "</TD><TD>" +
                buildColorDropDown(course.getId(), deck.getId(), deck) +
                "</TD></TR>");
        }
        builder.append("</TABLE>");
        builder.append("<br>");
    }

    builder.append("<center>");
    builder.append("<input id=decksSubmitButton type=submit " +
        "value=\"Save changes\" />");
    builder.append("</center>");
    builder.append("</FORM>");
    out.println(builder);
%>
<%!
    public String buildActiveCheckbox(int courseId, int deckId, Deck deck) {
        String checkedString = deck.getActive() ? "checked" : "";
        return "<input class=activeCheckbox " + checkedString + 
            " type=checkbox name=\"active_c"+courseId+"d"+deckId+"\" />";
    }
%>
<%!
    public String buildColorDropDown(int courseId, int deckId, Deck deck) {
        String names[] = { "yellow", "purple", "lightpink", "pink", 
            "blue", "red", "orange", "green", "aqua", "grey" };
        String colors[] = { "#FFFF66", "#9370DB", "#FFC0CB", "#FF66FF",
            "#0000FF", "#FF0000", "#FFA500", "#90EE90", "#7FFFD4", "#BBBBBB" };
        String color = deck.getColor();
        StringBuilder builder = new StringBuilder(
            "<SELECT style=\"background-color:" + color + "\" " +
            "name=\"color_c"+courseId+"d"+deckId+"\" >\n");
        for (int i=0; i<colors.length; i++) {
            String selectedString = 
                color.equals(colors[i]) ? "selected " : "";
            builder.append("    <OPTION " + selectedString + 
                "style=\"background-color:" + colors[i] + 
                "\" value=\"" + colors[i] + "\">" +
                "" + names[i] + "</OPTION>");
        }
        return builder.toString();
    }
%>

</BODY>
</HTML>
