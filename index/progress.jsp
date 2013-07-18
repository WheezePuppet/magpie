<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.DateHelper"
         import="java.text.DecimalFormat"
         import="java.util.ArrayList"
         import="java.util.Date"
         import="java.util.Calendar" %>
<HTML>
<HEAD>
	<title>Magpie</title>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="course.css" />
	<link rel="stylesheet" type="text/css" href="progress.css" />
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
<%!
	final int NUM_DAYS = 7;
	final int MILLISECONDS_PER_DAY = 1000*60*60*24;
    DecimalFormat percentFormat = new DecimalFormat(".0");
%>
<%
	String username = (String) session.getAttribute("username");
	Student student = (Student) UserHelper.getUser(username);

	int courseid = Integer.valueOf(request.getParameter("courseid"));
	Course course = CourseManager.instance().get(courseid);
    int requiredMinutesPerDay = course.getMinsPerDay();
    ArrayList<Calendar> mondays = course.getMondays();

	out.println("<H1>Progress for " + student.getFirstName() + " " + student.getLastName() + "</H1>");

    StringBuilder builder = new StringBuilder(
        "<TABLE id=progress cellpadding=\"2\" cellspacing=\"2\" border=\"1\">");
    builder.append("<TR><TH>Days</TH><TH>Minutes</TH><TH>Num Cards</TH>" +
        "<TH>% Correct</TH></TR>");

    for (Calendar monday : mondays) {

        boolean todayOrEarlier = DateHelper.todayOrEarlier(monday);
        if (!todayOrEarlier) {
            continue;
        }

        for (int i = 0; i < 7; i++) {

            Date date = DateHelper.addDays(
                    new Date(monday.getTimeInMillis()), i).getTime();
			int min = student.getTime(date);
			int numSuccessful = student.getNumSuccessfulReviews(date);
			int numTotal = student.getNumTotalReviews(date);

            if (min == 0) {
                builder.append("<TR class=missingDay >");
            } else {
                builder.append("<TR class=practiceDay >");
            }
            builder.append("<TD class=dateClass >" + DateHelper.format(date) + "</TD>");

			builder.append("<TD class=dateClass >" + min + "</TD>");
			builder.append("<TD class=numCards >" + numTotal + "</TD>");

            if (numTotal > 0) {
                builder.append("<TD class=percentCorrect >" + 
                    percentFormat.format(
                            100 * ((double)numSuccessful)/numTotal) + 
                    "%</TD>");
            } else {
                builder.append("<TD class=percentCorrect >&#8212</TD>");
            }
        }

    }
    builder.append("</TABLE>");

    out.println(builder);
%>

</BODY>
</HTML>
