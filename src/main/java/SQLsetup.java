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

    public static ArrayList normalUserCheck (ArrayList<String> viewers, Connection connection) {

        ArrayList<String> unchecked = new ArrayList<String>();

        for (int i = 0; i < viewers.size(); i++) {
            String user = viewers.get(i);
            String query = "select * from bot where name=\'" + user + "\'";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next() == false){
                    unchecked.add(user);
                }
                while (rs.next()) {
                    user = rs.getString("bot");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return unchecked;
        }
        return unchecked;
    }
}
