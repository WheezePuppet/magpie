<%@ include file="redirectHeader.jsp" %>
<html> 
<head> 
	<link rel="stylesheet" type="text/css" href="login.css" />
	<script type="text/javascript" src="changePassword.js"></script>
	<title>Magpie - Change password</title>
</head>
<body>
	<div id=title> 
		<%@include file="title.html"%>
	</div> 
	<div id=name>Change password</div>
	<div id=greeting>
		Welcome to Magpie! Please change your password to something you'll
remember. (If you ever forget your password, you'll need to send mail to
stephen AT umw DOT edu to get it reset.)
	</div>
	<br/><br/>
	<form action="processChangePassword.jsp" onsubmit="return validateForm(this)" method="POST">
		<table id=logincontrols cellpadding=3>
			<tr><td>Enter your new password:</td><td><input type="password" name="password" id="password1" size="15" /></td></tr>
			<tr><td>Enter it again:</td><td><input type="password" id="password2" size="15" /></td></tr>
			<tr><td colspan=2 style="{text-align:center;}"><input type="submit" value="Submit" /></td></tr>
		</table>
	</form>
	<div id=msg>
<%
	String failed = request.getParameter("failed");
	if (failed != null && failed.equals("1"))
		out.println("Error: Please try again.");
%>
	</div>
</body>
</html>
