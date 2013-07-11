
import java.sql.*;
import java.io.*;

public class Write {

    public static void main(String args[]) {
        try {
            new Write().doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doIt() throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(new
            FileInputStream("logos.txt"),"UTF-8"));
String logos = br.readLine();
        br.close();

PrintWriter pw = new PrintWriter(new OutputStreamWriter(new
FileOutputStream("output.txt"),"UTF-8"));
pw.println("I love " + logos);
pw.close();
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(
            "jdbc:mysql://localhost/magpie?charSet=UTF-8&useUnicode=true&characterEncoding=UTF-8",
            "stephen","iloverae");

        Statement s = c.createStatement();
        s.executeUpdate("set character_set_server=utf8");
        s.executeUpdate("set character_set_connection=utf8");
        s.executeUpdate("insert into card (question) values ('" +
            logos + "')");

    }

}


