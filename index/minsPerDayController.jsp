<%@ page import="edu.umw.cpsc.magpie.core.*" %>

<%
    Course theCourse = CourseManager.instance().get(Integer.valueOf(request.getParameter("courseid")));
    theCourse.setMinsPerDay(Integer.valueOf(request.getParameter("minsPerDay")));
    response.sendRedirect("teacher.jsp");
%>
