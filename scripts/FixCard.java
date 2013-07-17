
import java.sql.*;
import java.io.*;

/** Fix (meaning "replace the question and answer) a single card in the Magpie
 * database. 
 */
public class FixCard {

    public static void main(String args[]) {
        try {
            if (args.length != 0) {
                System.out.println("Usage: FixCard. " + 
                    "You will be prompted for a card id, a question " +
                    "followed by a comma on a line by itself, and an " +
                    "answer.");
                System.exit(1);
            }
            new FixCard().fix();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fix() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost/magpie?charSet=UTF-8&useUnicode=true&characterEncoding=UTF-8",
            "stephen","iloverae");

        Statement s = c.createStatement();
        s.executeUpdate("set character_set_server=utf8");
        s.executeUpdate("set character_set_connection=utf8");

        BufferedReader br = new BufferedReader(new InputStreamReader(
            System.in, "UTF-8"));

        PreparedStatement updateCardStmt = c.prepareStatement(
            "UPDATE card set question=?, answer=? where cid=?");

        int id = Integer.valueOf(br.readLine());

        String question = br.readLine();
        String next = br.readLine();
        while (!next.equals(",")) {
            question = question + "\n" + next;
            next = br.readLine();
        }
        String answer = br.readLine();
        next = br.readLine();
        while (next != null) {
            answer = answer + "\n" + next;
            next = br.readLine();
        }

        updateCardStmt.setString(1,question);
        updateCardStmt.setString(2,answer);
        updateCardStmt.setInt(3,id);

        updateCardStmt.executeUpdate();

    }

}


