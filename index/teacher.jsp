<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.DateHelper"
         import="java.util.ArrayList"
         import="java.util.Calendar" %>
<HTML>
<HEAD>
	<title>Magpie</title>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="teacher.css" />
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
<%
	Teacher teacher = TeacherManager.instance().get((String) session.getAttribute("username"));
	ArrayList<Course> courses = teacher.getCourses();

	out.println("<H1>Courses for " + teacher.getFirstName() + " " + teacher.getLastName() + "</H1>");

	for (Course course : courses) {
		ArrayList<Calendar> mondays = course.getMondays();

		out.println("<H2>" + course.getName() + "</H2>");
    
        out.println("<form action=minsPerDayController.jsp method=get>");
        out.println("    Minimum time per day:&nbsp; ");
        out.println("    <input type=text size=1 name=minsPerDay value=\"" +
            course.getMinsPerDay() + "\"/>minutes");
        out.println("    <input type=hidden name=courseid value=\"" + course.getId() + "\"/>");
        out.println("    <input type=submit name=Set value=Set />");
        out.println("</form>");

        out.println("<br/>");

		out.println("Grades");
        out.println("<br/>");
		out.println("<ul>");
        out.println("<li><a href=\"semester.jsp?courseid=" + course.getId() +
"\"><b>Overall semester grades</b></a></li>");
		for (Calendar calendar : mondays) {
			boolean todayOrEarlier = DateHelper.todayOrEarlier(calendar);

			out.println("<li>");
			if (todayOrEarlier) {
				out.println("<a href=\"course.jsp?courseid=" + course.getId() + "&startTime=" + calendar.getTimeInMillis() + "\">");
            }
			out.println("Week of " + DateHelper.format(calendar));
			if (todayOrEarlier) {
				out.println("</a>");
            }
			out.println("</li>");
		}
		out.println("</ul>");

        //out.println("<a href=\"deck.jsp?courseid=" + course.getId() + "\">Enable or disable decks</a>");
    }
%>
</BODY>
</HTML>
