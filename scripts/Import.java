
import java.sql.*;
import java.io.*;

/** Import a deck file to the Magpie database.
 */
public class Import {

    public static void main(String args[]) {
        try {
            if (args.length < 2) {
                System.out.println(
                    "Usage: Import [deckFile] [courseNum] [rev=false].");
                System.exit(1);
            }
            String filename = args[0];
            String courseNum = args[1];
            boolean reverse = false;
            if (args.length == 3) {
                if (args[2].startsWith("rev")) {
                    String revVal = args[2].split("=")[1];
                    if (revVal.equals("true")) {
                        reverse = true;
                    }
                }
            }
            new Import().importDeck(filename, courseNum, reverse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importDeck(String filename, String courseNum, boolean reverse) 
        throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost/magpie?charSet=UTF-8&useUnicode=true&characterEncoding=UTF-8",
            "stephen","iloverae");

        Statement s = c.createStatement();
        s.executeUpdate("set character_set_server=utf8");
        s.executeUpdate("set character_set_connection=utf8");

        BufferedReader br = new BufferedReader(new InputStreamReader(new
            FileInputStream(filename),"UTF-8"));

        String deckName = br.readLine();

        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO deck (deckname, courseid, active) " +
            "VALUES (?, ?, 1)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, deckName);
        ps.setString(2, courseNum);
        ps.executeUpdate();
        ResultSet didKeys = ps.getGeneratedKeys();
        didKeys.next();
        int did = didKeys.getInt(1);

        PreparedStatement insertCardStmt = c.prepareStatement(
            "INSERT INTO card (question, answer, did, dir) " +
            "VALUES (?, ?, ?, 'F')");

        PreparedStatement insertRevCardStmt = c.prepareStatement(
            "INSERT INTO card (question, answer, did, inverseid, dir) " +
            "VALUES (?, ?, ?, ?, 'R')");

	    PreparedStatement setRevIdStmt = c.prepareStatement(
            "UPDATE card SET inverseid=? WHERE cid=?");

        PreparedStatement lastIdStmt = c.prepareStatement(
            "SELECT LAST_INSERT_ID()");

        String question = br.readLine();
        while (question != null) {
            String next = br.readLine();
            while (!next.equals(",")) {
                question = question + "\n" + next;
                next = br.readLine();
            }
            String answer = br.readLine();
            next = br.readLine();
            while (!next.equals(".")) {
                answer = answer + "\n" + next;
                next = br.readLine();
            }
            insertCardStmt.setString(1,question);
            insertCardStmt.setString(2,answer);
            insertCardStmt.setInt(3,did);
            insertCardStmt.executeUpdate();

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
            }
            question = br.readLine();
        }
    }

}
