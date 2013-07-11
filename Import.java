
import java.sql.*;
import java.io.*;

public class Import {

    public static void main(String args[]) {
        try {
            if (args.length < 2) {
                System.out.println("Usage: Import [deckFile] [courseNum].");
                System.exit(1);
            }
            String filename = args[0];
            String courseNum = args[1];
            new Import().importDeck(filename, courseNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importDeck(String filename, String courseNum) 
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
        br.readLine();   // Skip blank

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
            "INSERT INTO card (question, answer, did) " +
            "VALUES (?, ?, ?)");

        String question = br.readLine();
        while (question != null) {
            br.readLine();   // Skip ","
            String answer = br.readLine();
            insertCardStmt.setString(1,question);
            insertCardStmt.setString(2,answer);
            insertCardStmt.setInt(3,did);
            insertCardStmt.executeUpdate();

            br.readLine();   // Skip blank
            br.readLine();   // Skip "."
            br.readLine();   // Skip blank

            question = br.readLine();
        }

    }

}


