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
	String username = (String) session.getAttribute("username");
	Student student = (Student) UserHelper.getUser(username);

	int courseid = Integer.valueOf(request.getParameter("courseid"));
	Course course = CourseManager.instance().get(courseid);
    int requiredMinutesPerDay = course.getMinsPerDay();
    ArrayList<Calendar> mondays = course.getMondays();

	out.println("<H1>Progress for " + student.getFirstName() + " " + student.getLastName() + "</H1>");

    StringBuilder builder = new StringBuilder("<TABLE border=\"1\">");
    builder.append("<TR><TH>Days</TH><TH>Minutes</TH><TH>Grade</TH></TR>");

    for (Calendar monday : mondays) {

        boolean todayOrEarlier = DateHelper.todayOrEarlier(monday);
        if (!todayOrEarlier) {
            continue;
        }

        long weeklyGrade = getWeeklyGrade(student, course, monday);

        // Mon-Thurs
        for (int i = 0; i < 4; i++) {
            builder.append("<TR>");
            Date date = DateHelper.addDays(new Date(monday.getTimeInMillis()), i).getTime();
            String dateClass = course.includesDate(date) ? "" : "class=\"ungraded\"";
            builder.append("<TD " + dateClass + ">" + DateHelper.format(date) + "</TD>");
			int min = student.getTime(date);

			boolean complete = min >= requiredMinutesPerDay ||
				student.finishedOn(date) ||
				student.troubleOn(date);

			dateClass = "class=\"ungraded\"";

			if(course.includesDate(date)) {
				dateClass = complete ? "class=\"finished\"" : "";
			}

			builder.append("<TD " + dateClass + ">" + min + "</TD>");
            if (i==0) {
                builder.append("<TD rowspan=5 align=center>Grade:<br>" + weeklyGrade + "%</TD></TR>");
            }
        }

        String dateClass = 
            course.includesDate(DateHelper.addDays(new Date(monday.getTimeInMillis()),4)) ||
            course.includesDate(DateHelper.addDays(new Date(monday.getTimeInMillis()),5)) ||
            course.includesDate(DateHelper.addDays(new Date(monday.getTimeInMillis()),6)) ?
            "" : "class=\"ungraded\"";

        // Fri-Sun
        builder.append("<TR>");
        builder.append("<TD " + dateClass + ">" + DateHelper.format(DateHelper.addDays(new Date(monday.getTimeInMillis()), 4)) + " - " + DateHelper.format(DateHelper.addDays(new Date(monday.getTimeInMillis()), 6)) + "</TD>");

		int min = 0;
		boolean finished = false;
		boolean graded = false;
		for (int day = 4; day < 7; day++) {
			Date date = DateHelper.addDays(new Date(monday.getTimeInMillis()), day).getTime();
			min += student.getTime(date);

			if(student.finishedOn(date) || student.troubleOn(date))
				finished = true;
			if(course.includesDate(date))
				graded = true;
		}

		boolean complete = min >= requiredMinutesPerDay || finished;

		dateClass = "class=\"ungraded\"";

		if(graded) {
			dateClass = complete ? "class=\"finished\"" : "";
		}

		builder.append("<TD " + dateClass + ">" + min + "</TD></TR>");

    }
    builder.append("</TABLE>");

    out.println(builder);
%>

<%!
    long getWeeklyGrade(Student s, Course c, Calendar monday) {

        int requiredMinutesPerDay = c.getMinsPerDay();

        int gradedDays = 0;
        int completedDays = 0;

        // Mon-Thurs
        for (int i = 0; i < 4; i++) {
            Date date = DateHelper.addDays(new Date(monday.getTimeInMillis()), i).getTime();
			int min = s.getTime(date);

			boolean complete = min >= requiredMinutesPerDay ||
				s.finishedOn(date) ||
				s.troubleOn(date);

			if(c.includesDate(date)) {
				gradedDays++;
				if (complete)
					completedDays++;
			}

        }

        // Fri-Sat
		int min = 0;
		boolean finished = false;
		boolean graded = false;
		for (int day = 4; day < 7; day++) {
			Date date = DateHelper.addDays(new Date(monday.getTimeInMillis()), day).getTime();
			min += s.getTime(date);

			if(s.finishedOn(date) || s.troubleOn(date))
				finished = true;
			if(c.includesDate(date))
				graded = true;
		}

		boolean complete = min >= requiredMinutesPerDay || finished;

		if(graded) {
			gradedDays++;
			if(complete)
				completedDays++;
		}
		long percentage = Math.round((double) completedDays / (double) gradedDays * 100);
        return percentage;
    }
%>
</BODY>
</HTML>
