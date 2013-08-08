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
	<link rel="stylesheet" type="text/css" href="semester.css" />
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

	out.println("<H1>Roster for " + course.getName() + "</H1>");

	out.println(getTable(course, GradingGroup.ALL));
%>
<%!
	String getTable(Course course, GradingGroup group) {

		StringBuilder builder = new StringBuilder("<TABLE border=\"1\">");

		builder.append("<TR><TH>Student</TH>");

        for (Calendar monday : course.getMondays()) {
			builder.append("<TH>" + DateHelper.format(monday) + "<br>(" +
                getGradedDays(course, monday) + " days)</TH>");
		}
		builder.append("<TH>Grade</TH>");
		builder.append("</TR>");
		for (Student student : course.getEnrolledStudents(group))
			builder.append(getRow(course, student));

		builder.append("</TABLE>");

		return builder.toString();
	}

	String getRow(Course course, Student student) {

		String username = student.getUsername();
		if (username.startsWith("mpiroc") || username.startsWith("stephen"))
			return "";

		int completedDays = 0;
		int gradedDays = 0;

		StringBuilder builder = new StringBuilder
			("<TR><TD>" + student.getLastName() + ", " + student.getFirstName() + "</TD>");

        int totalGradedDays = 0;
        int totalCompletedDays = 0;

        for (Calendar monday : course.getMondays()) {
            totalGradedDays += getGradedDays(course, monday);
            totalCompletedDays += getCompletedDays(course, student, monday);

            builder.append("<TD align=center>" + getCompletedDays(course, student,monday) + "</TD>");
        }

		long percentage = Math.round((double) totalCompletedDays / (double)
totalGradedDays * 100);

        builder.append("<TD align=center><b>" + percentage + "%</b><br>(" + totalCompletedDays + "/"
+ totalGradedDays + ")</TD>");

        return builder.toString();
    }

    int getGradedDays(Course course, Calendar monday) {

        Date mondayDate = monday.getTime();

        int gradedDays = 0;
            
        for (int i=0; i<4; i++) {
            Date date = DateHelper.addDays(mondayDate, i).getTime();

			if(course.includesDate(date)) {
				gradedDays++;
			}

		}

		// Friday - Sunday
		boolean graded = false;
		for (int day = 4; day < 7; day++) {
			Date date = DateHelper.addDays(mondayDate, day).getTime();

			if(course.includesDate(date))
				graded = true;
		}

		if(graded) {
			gradedDays++;
		}

		if (mondayDate.getTime() == 1279512000000L)
			gradedDays--;

        return gradedDays;
	}

    int getCompletedDays(Course course, Student student, Calendar monday) {

        int requiredMinutesPerDay = course.getMinsPerDay();

        Date mondayDate = monday.getTime();

        int completedDays = 0;
            
        for (int i=0; i<4; i++) {
            Date date = DateHelper.addDays(mondayDate, i).getTime();
			int min = student.getTime(date);

			boolean complete = min >= requiredMinutesPerDay ||
				student.finishedOn(date) ||
				student.troubleOn(date);

			if(course.includesDate(date)) {
				if (complete)
					completedDays++;
			}

		}

		// Friday - Sunday
		int min = 0;
		boolean finished = false;
		boolean graded = false;
		for (int day = 4; day < 7; day++) {
			Date date = DateHelper.addDays(mondayDate, day).getTime();
			min += student.getTime(date);

			if(student.finishedOn(date) || student.troubleOn(date))
				finished = true;
			if(course.includesDate(date))
				graded = true;
		}

		boolean complete = min >= requiredMinutesPerDay || finished;

		if(graded) {
			if(complete)
				completedDays++;
		}

        return completedDays;
	}
%>
</BODY>
</HTML>
