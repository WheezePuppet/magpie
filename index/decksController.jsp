<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="edu.umw.cpsc.magpie.util.*" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.List" %>

<%
	User theUser = 
        UserHelper.getUser((String) session.getAttribute("username"));

    // Ugh, I can't think of a better way to do this. Set all decks
    // temporarily to inactive, so that only those whose checkboxes were
    // checked get (re?)-activated.
    List<Course> courses = theUser.getCourses();
    for (int i=0; i<courses.size(); i++) {
        Course course = courses.get(i);
        List<Deck> decks = course.getDecks();
        for (int j=0; j<decks.size(); j++) {
            decks.get(j).setActive(false);
        }
    }
    Enumeration<String> paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
        String paramName = paramNames.nextElement();
        out.println(paramName + " = " + request.getParameter(paramName) + "<br/>");
        String attr = paramName.split("_")[0];
        String courseDeck = paramName.split("_")[1];
        int deckId = Integer.valueOf(courseDeck.split("d")[1]);

        Deck deck = DeckManager.instance().get(deckId);

        if (attr.equals("active")) {
            deck.setActive(request.getParameter(paramName).equals("on"));
        } else if (attr.equals("color")) {
            deck.setColor(request.getParameter(paramName));
        }
    }
    response.sendRedirect("decks.jsp");
%>
