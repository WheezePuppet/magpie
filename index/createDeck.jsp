<%@ page import="edu.umw.cpsc.magpie.core.*" %>
<%@ page import="edu.umw.cpsc.magpie.util.*" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.sql.*" %>

<%
    request.setCharacterEncoding("UTF-8");

	User theUser = 
        UserHelper.getUser((String) session.getAttribute("username"));
    // For now, just add the new deck to the user's first course.
    int courseId = theUser.getCourses().get(0).getId();

	String deckName = request.getParameter("name");
    if (deckName == null  ||  deckName.equals("")) {
        out.println("You must enter a deck name!");
        out.flush();
        return;
    }
	String reverseCheckbox = request.getParameter("addrev");
	boolean reverse = reverseCheckbox != null  &&  
        reverseCheckbox.equals("on");

    Class.forName("com.mysql.jdbc.Driver");
    Connection c = DriverManager.getConnection(
        "jdbc:mysql://localhost/magpie?charSet=UTF-8&useUnicode=true&characterEncoding=UTF-8",
        "stephen","iloverae");

    // Get the bloody connection in UTF-8 mode!!
    Statement s = c.createStatement();
    s.executeUpdate("set character_set_server=utf8");
    s.executeUpdate("set character_set_connection=utf8");


    PreparedStatement ps = c.prepareStatement(
        "INSERT INTO deck (deckname, courseid, active) " +
        "VALUES (?, ?, 0)", Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, deckName);
    ps.setInt(2, courseId);
    ps.executeUpdate();
    ResultSet didKeys = ps.getGeneratedKeys();
    didKeys.next();
    int did = didKeys.getInt(1);


    PreparedStatement insertCardStmt = c.prepareStatement(
        "INSERT INTO card (question, answer, did, dir) " +
        "VALUES (?, ?, ?, 'F')", Statement.RETURN_GENERATED_KEYS);

    PreparedStatement insertRevCardStmt = c.prepareStatement(
        "INSERT INTO card (question, answer, did, inverseid, dir) " +
        "VALUES (?, ?, ?, ?, 'R')", Statement.RETURN_GENERATED_KEYS);

    PreparedStatement setRevIdStmt = c.prepareStatement(
        "UPDATE card SET inverseid=? WHERE cid=?");

    PreparedStatement lastIdStmt = 
        c.prepareStatement("SELECT LAST_INSERT_ID()");

    PreparedStatement hydrateCardStmt = 
        c.prepareStatement("SELECT * FROM card WHERE cid=?");

    Enumeration<String> paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
        String paramName = paramNames.nextElement();
        if (paramName.startsWith("q")) {
            int questionNum = Integer.valueOf(paramName.substring(1));
            String question = request.getParameter(paramName);
            String answer = request.getParameter("a" + questionNum);
            insertCardStmt.setString(1,question);
            insertCardStmt.setString(2,answer);
            insertCardStmt.setInt(3,did);
            insertCardStmt.executeUpdate();
            ResultSet cidKeys = insertCardStmt.getGeneratedKeys();
            cidKeys.next();
            int cid = cidKeys.getInt(1);
            hydrateCardStmt.setInt(1,cid);
            ResultSet hydrateCardRs = hydrateCardStmt.executeQuery();
            hydrateCardRs.next();
            CardManager.instance().add(new Card(hydrateCardRs));

            if (reverse) {

                ResultSet rs = lastIdStmt.executeQuery();
                rs.next();
                int lastId = rs.getInt(1);

                insertRevCardStmt.setString(1,answer);
                insertRevCardStmt.setString(2,question);
                insertRevCardStmt.setInt(3,did);
                insertRevCardStmt.setInt(4,lastId);
                insertRevCardStmt.executeUpdate();

                rs = lastIdStmt.executeQuery();
                rs.next();
                int revId = rs.getInt(1);

                setRevIdStmt.setInt(1, revId);
                setRevIdStmt.setInt(2, lastId);
                setRevIdStmt.executeUpdate();

                ResultSet revcidKeys = insertRevCardStmt.getGeneratedKeys();
                revcidKeys.next();
                int revcid = revcidKeys.getInt(1);
                hydrateCardStmt.setInt(1,revcid);
                ResultSet hydrateRevCardRs = hydrateCardStmt.executeQuery();
                hydrateRevCardRs.next();
                CardManager.instance().add(new Card(hydrateRevCardRs));
            }
            
        }
    }

    PreparedStatement hydrateDeckStmt = 
        c.prepareStatement("SELECT * FROM deck WHERE did=?");
    hydrateDeckStmt.setInt(1,did);
    ResultSet hydrateDeckRs = hydrateDeckStmt.executeQuery();
    hydrateDeckRs.next();

    DeckManager.instance().add(new Deck(hydrateDeckRs));
    // Deck theNewDeck = DeckManager.instance().get(did);
    CourseManager.instance().get(courseId).addDeckById(did);
    response.sendRedirect("decks.jsp");
%>
