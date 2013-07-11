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
	int courseid = Integer.valueOf(request.getParameter("courseid"));
	Course course = CourseManager.instance().get(courseid);


	long startMs = Long.valueOf(request.getParameter("startTime"));
	Date start = DateHelper.stripTime(new Date(startMs)).getTime();

	out.println("<H1>Roster for " + course.getName() + ", Week of " + DateHelper.format(start) + "</H1>");

	out.println(getTable(course, start, GradingGroup.ALL));
%>
<%!
	String getTable(Course course, Date start, GradingGroup group) {

		// Start on a Monday
		StringBuilder builder = new StringBuilder("<TABLE border=\"1\">");

		builder.append("<TR><TH>Student</TH>");
		for (int i = 0; i < 4; i++) {
			Calendar calendar = DateHelper.addDays(start, i);
			String dateClass = course.includesDate(calendar) ? "" : "class=\"ungraded\"";
			builder.append("<TH " + dateClass + ">" + DateHelper.format(calendar) + "</TH>");
		}

        String dateClass = 
            course.includesDate(DateHelper.addDays(start,4)) ||
            course.includesDate(DateHelper.addDays(start,5)) ||
            course.includesDate(DateHelper.addDays(start,6)) ?
            "" : "class=\"ungraded\"";
		builder.append("<TH " + dateClass + ">" + DateHelper.format(DateHelper.addDays(start, 4)) + " - " +
			DateHelper.format(DateHelper.addDays(start, 6)) + "</TH>");
		builder.append("<TH>Grade</TH>");
		builder.append("</TR>");

		for (Student student : course.getEnrolledStudents(group))
			builder.append(getRow(course, student, start));

		builder.append("</TABLE>");

		return builder.toString();
	}

	String getRow(Course course, Student student, Date start) {

        int requiredMinutesPerDay = course.getMinsPerDay();

		String username = student.getUsername();
		if (username.startsWith("mpiroc") || username.startsWith("stephen"))
			return "";

		int completedDays = 0;
		int gradedDays = 0;

		StringBuilder builder = new StringBuilder
			("<TR><TD>" + student.getLastName() + ", " + student.getFirstName() + "</TD>");

		// Monday - Thursday
		for (int day = 0; day < 4; day++) {
			Date date = DateHelper.addDays(start, day).getTime();
			int min = student.getTime(date);

			boolean complete = min >= requiredMinutesPerDay ||
				student.finishedOn(date) ||
				student.troubleOn(date);

			String dateClass = "class=\"ungraded\"";

			if(course.includesDate(date)) {
				dateClass = complete ? "class=\"finished\"" : "";
				gradedDays++;
				if (complete)
					completedDays++;
			}

			builder.append("<TD " + dateClass + ">" + min + "</TD>");
		}

		// Friday - Sunday
		int min = 0;
		boolean finished = false;
		boolean graded = false;
		for (int day = 4; day < 7; day++) {
			Date date = DateHelper.addDays(start, day).getTime();
			min += student.getTime(date);

			if(student.finishedOn(date) || student.troubleOn(date))
				finished = true;
			if(course.includesDate(date))
				graded = true;
		}

		boolean complete = min >= requiredMinutesPerDay || finished;

		String dateClass = "class=\"ungraded\"";

		if(graded) {
			dateClass = complete ? "class=\"finished\"" : "";
			gradedDays++;
			if(complete)
				completedDays++;
		}

		if (start.getTime() == 1279512000000L)
			gradedDays--;

		long percentage = Math.round((double) completedDays / (double) gradedDays * 100);

		builder.append("<TD " + dateClass + ">" + min + "</TD>");
		builder.append("<TD>" + percentage + "%</TD>");
		//builder.append("<TD>" + (completedDays * 20) + "/" + (gradedDays * 20) + "</TD>");
		builder.append("</TR>");

		return builder.toString();
	}
%>
</BODY>
</HTML>
