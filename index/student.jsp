<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="redirectHeader.jsp" %>
<html> 
<head> 
	<title>Magpie</title> 
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="student.css" />
	<link rel="stylesheet" type="text/css" href="title.css" />
	<script type="text/javascript" src="js_lib/json2.js"></script> 
	<script type="text/javascript" src="js_lib/jquery.js"></script>
	<script type="text/javascript" src="js_lib/jquery.cookie.js"></script>
	<script type="text/javascript" src="student.js"></script>
</head> 
<body> 
	<div id=pagecontent> 
		<div id=title> 
			<%@include file="title.html"%>
		</div> 

		<div id=testarea> 
			<div id=question class=display > 
				<div class=centereddisplay></div> 
			</div> 
<!--
			<div id=answer class=display >
				<div class=centereddisplay></div> 
			</div> 
-->
			<div>
				<div id=response></div>
			</div> 
		</div> 
	</div> 
	<%@ include file="header.jsp" %>
	<%@ include file="ad.jsp" %>
</body> 
</html> 
