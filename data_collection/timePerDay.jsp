<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%
	DecimalFormat format = new DecimalFormat("#.###");
	Course course = CourseManager.instance().get(1);

	for (Student student : StudentManager.instance().values()) {
		int gradingGroup = student.getGradingGroup() == GradingGroup.SCORE ? 0 : 1;

		out.print(gradingGroup + ",");
		out.println(format.format(student.getAverageTime(course)));
	}
%>
