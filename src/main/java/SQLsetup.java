import java.sql.*;
import java.util.ArrayList;

public class SQLsetup {

    public static Connection connection(String dbURL, String username, String password) throws SQLException {

        try {
            Connection connection = DriverManager.getConnection(dbURL, username, password);
            return connection;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }

    }

    public static ArrayList botcheck (ArrayList<String> viewers, Connection connection) {

        ArrayList<String> unchecked = new ArrayList<String>();

        String namessss = "wwwww";

        String query = "select * from bot where name=" + namessss;
        String user = namessss;
        System.out.println(query);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                user = rs.getString("bot");
                System.out.println("USER: " + user);
            }
        } catch (SQLException ex) {
            unchecked.add(user);
            ex.printStackTrace();
        }
        return unchecked;

    }
}
