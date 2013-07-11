
import java.sql.*;
import java.io.*;

public class Chars {

    public static void main(String args[]) {
        try {
            new Chars().doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doIt() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost/magpie?useUnicode=true&amp;characterEncoding=UTF-8",
            "stephen","iloverae");
        PreparedStatement ps = c.prepareStatement("select question from card");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String theString = new String(rs.getString(1).getBytes("utf8"),"UTF8");
/*
Reader r = rs.getCharacterStream(1);
BufferedReader br = new BufferedReader(r);
String theString = br.readLine();
*/
            System.out.println("The card was " + theString);
        }
    }

}


