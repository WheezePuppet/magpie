<meta http-equiv="Content-Type" pageEncoding="utf-8" contentType="text/html; charset=utf-8">
<%@ include file="redirectHeader.jsp" %>
<%@ page import="edu.umw.cpsc.magpie.core.*"
         import="edu.umw.cpsc.magpie.util.*"
         import="java.sql.Date"
         import="java.util.Collections"
         import="java.util.List" %>
<HTML>
<HEAD>
	<link rel="stylesheet" type="text/css" href="header.css" />
	<link rel="stylesheet" type="text/css" href="deckBuilder.css" />
	<script type="text/javascript" src="js_lib/jquery.js"></script>
	<script type="text/javascript" src="deckBuilder.js"></script>
	<title>Magpie</title>
</HEAD>
<BODY>
	<%@ include file="header.jsp" %>
<% 
    response.setContentType( "text/html; charset=utf-8" );
%>

<H1>Deck Builder</H1>

<FORM ACTION=createDeck.jsp METHOD=POST>
<div>
New deck name: <input id=deckName type=text size=50 name=name />
</div>

<center>
<table id=cardTable >
<tr><th>"Question"</th><th>"Answer"</th></tr>

<tr><td>
    <textarea cols=40 rows=3 name=q1></textarea>
    </td>
    <td>
    <textarea cols=40 rows=3 name=a1></textarea>
    </td>
</tr>

</table>
</center>

</FORM>

</BODY>
</HTML>
