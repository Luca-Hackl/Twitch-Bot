import java.sql.*;
import java.util.ArrayList;

public class SQLsetup {

    public static Connection connection(String dbURL, String username, String password) throws SQLException {

        try {
            return DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }

    }

    public static ArrayList <String> normalUserCheck (ArrayList<String> viewers, Connection connection) {

        ArrayList<String> unchecked = new ArrayList<String>();

        for (String user : viewers) {
            String query = "select * from bot where name='" + user + "'";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (!rs.next()) {
                    unchecked.add(user);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return unchecked;
        }
        return unchecked;
    }
}
