<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%
	String username = (String) session.getAttribute("username");
	Student student = StudentManager.instance().get(username);

	if (student == null) {
		response.sendRedirect("login.jsp");
		return;
	}
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<title>Magpie -- instructions</title>
</head>
<body>
	<%@ include file="header.jsp" %>
	<h1>Magpie -- instructions</h1>
	<p>Hello, and welcome to Magpie!</p>
	<p>
	Our goal is for Magpie to be a useful tool to you in your study of foreign
	languages. It's a simple "flashcard" like program that is designed to help
	you learn, master, and retain vocabulary words.</p>
	<p>
	Magpie will be synced throughout the semester with the textbook lessons
	your class is learning. As you proceed through the course, vocabulary
	from new chapters will be automatically "rolled in" to your current
	library of cards. These new words will come up in Magpie at the
	appropriate time, so that you can learn them when you need to. There's
	nothing you need to do for that to happen!</p>
	<p>
	Magpie is really simple to use. All you have to do, once you log in is:
	<ol>
		<li>Look at the word in the yellow box. This is one of your class's
		vocabulary words which it is now time to learn. You may already
		know it well, or you may barely remember it, or it may be brand new
		to you. In any case, look at it for a moment and decide what its
		translation is. If you can, visualize that translation briefly in
		your mind.</li>
		<li>When you've formed it in your mind (or decided you don't know
		the answer) click the white box. (Alternatively, you can press the
		spacebar.) This will bring up the correct definition. See whether
		or not you were correct!</li>
<%
	if (student.getGradingGroup().toDbString().equals("timer")) {
%>
		<li>Now tell Magpie whether you had the right answer or not by 
		clicking "Yes" or "No." (Alternatively, you can press the "y" or "n"
		key on your keyboard.)
		<b>Note: for Magpie to work properly, it is
		important for you to answer honestly! Answering "Yes" to a word that
		you don't really know will tell Magpie that you already know it,
		and cause Magpie not to show you that word again very often.
		Correctly saying "No" tells Magpie that it's a difficult word for 
		you, and Magpie will bring it up more frequently until you learn it.
		</b>
		</li>
<%
	} else {
%>
		<li>Now tell Magpie how well you know that card, on a scale of 0
		to 5. A score of 0 means "I didn't know that card at all," while 5
		means "I knew it perfectly well." Note that any score from 0-2 means
		that you <i>didn't</i> know the card, and any score from 3-5 means
		that you <i>did</i> know it. (You can click the buttons or press
		the number keys.)
		<b>Note: for Magpie to work properly, it is
		important for you to answer honestly! Giving a high score to a word
		that you don't really know will tell Magpie that you already know it,
		and cause Magpie not to show you that word again very often.
		Giving an accurate, low score tells Magpie that it's a difficult word
		for you, and Magpie will bring it up more frequently until you learn
		it.</b>
		</li>
<%
	}
%>
		<li>As soon as you score the card, you will be presented with
		another. Try to get in a rhythm as you continue to drill yourself!
		</li>
		<li>If you work hard enough, it is possible to get to a point where
		Magpie has no more cards to show you for the day. In this case,
		you'll receive a message and be invited back to Magpie for the
		following day. <b>Don't rush to try to get to this point, though.
		Magpie will only work properly if you use it at a normal pace and
		answer the questions honestly. This is the way to help you learn
		the language the best, and do the best in the course.</b>
		</li>
	</ol></p>
	<p>Your instructor will get a daily update from Magpie of your progress,
	so he or she can check off that you've used it for the allotted minutes
	each day. (If you receive the "done" message indicating that you ran
	out of cards, he or she will see that, too.)</p>
	<p>If you run into any difficulties, or have questions about using
	Magpie, don't hesitate to send email to stephen@umw.edu.</p>

	<p>That's it! Good luck, and have fun using Magpie!</p>
</body>
</html>
