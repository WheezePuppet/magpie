<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.text.DecimalFormat" %>
<%
	ReviewList allReviews = new ReviewList();

	for (Student student : StudentManager.instance().values()) {
		if (student.getGradingGroup() == GradingGroup.TIMER)
			continue;

		ReviewList tempList = student.getReviewsClone();

		for (int i = 0; i < tempList.size(); i++) {
			Review review = tempList.get(i);
			if (review.getScore() >= 3)
				allReviews.add(review);
		}
	}

	for (int i = 0; i < allReviews.size(); i++) {
		Review review = allReviews.get(i);

		out.println(review.getScore() + "," + review.getResponseTime());
	}
%>
