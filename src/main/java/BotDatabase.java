import java.sql.*;

public class BotDatabase {

    public static void addsBannedBots (Connection connection, String name) throws SQLException {

        String sql = "INSERT INTO bot(name) VALUES(?)";     //adds banned bots to database

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean alreadyBanned(Connection connection, String name){

            String query = "select * from bot where name='" + name + "'";       //if bot is already banned -> no ban message
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (!rs.next()) {
                    return false;
                } else{
                    return true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        return false;
        }

}
