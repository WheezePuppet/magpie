<html> 
<head> 
	<link rel="stylesheet" type="text/css" href="login.css" />
	<script type="text/javascript" src="js_lib/jquery.js" ></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#username").focus();
		});
	</script>
	<title>Magpie -- Login</title>
</head>
<body>
	<div id=title> 
		<%@include file="title.html"%>
	</div> 
	<div id=name>Login to Magpie</div>
	<form action="processLogin.jsp" method="POST">
		<table id=logincontrols cellpadding=3>
			<tr><td><label for="username">Username (your UMW NetID)</label></td><td><input type="text" name="username" id="username" size="15" /></td></tr>
			<tr><td><label for="password">Password</label></td><td><input type="password" name="password" id="password" size="15" /></td></tr>
			<tr><td colspan=2 style="{text-align:center;}"><input type="submit" value="Login" /></td></tr>
		</table>
	</form>
	<div id=msg>
<%
	String failed = request.getParameter("failed");
	if (failed != null && failed.equals("1"))
		out.print("Could not login!");
%>
	</div>
</body>
</html>
