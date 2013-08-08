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
    response.setContentType("text/html; charset=utf-8");
%>

<H1>Deck Builder</H1>

<FORM ACTION=createDeck.jsp METHOD=POST ACCEPT-CHARSET="UTF-8">
<div>
New deck name: <input id=deckName type=text size=50 name=name />
</div>

<center>
<table id=cardTable >
<tr><th>"Question"</th><th>"Answer"</th><th></th></tr>

<tr><td>
    <textarea cols=40 rows=3 name=q1></textarea>
    </td>
    <td>
    <textarea cols=40 rows=3 name=a1></textarea>
    </td>
    <td class=widget >
    <img id=addNew src="images/plus.png" />
    </td>
</tr>

</table>

<div id=revcheckboxdiv>
Add "reverse" version of each card <input type=checkbox id=addrev name=addrev />
</div>
<div id=submitdiv>
<input type=submit id=submit value="Build deck" />
</div>
</center>

</FORM>

</BODY>
</HTML>
