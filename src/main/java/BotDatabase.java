import java.sql.*;
import java.util.ArrayList;

public class BotDatabase {

    private Connection connection;

    public BotDatabase(String dbURL, String username, String password) throws SQLException {
        this.connection = DriverManager.getConnection(dbURL, username, password);
    }

    public void addsBannedBots(String name) {

        String sql = "INSERT INTO bot(name) VALUES(?)";     //adds banned bots to database

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean alreadyBanned(String name) throws SQLException {

        String query = "select * from bot where `name`=?";       //if bot is already banned -> no ban message

        ResultSet rs = null;
        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1,name);
            stmt.executeQuery();
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null){
                rs.close();
            }
        }
        return false;
    }

    public ArrayList<String> normalUserCheck(ArrayList<String> viewers) {

        ArrayList<String> unchecked = new ArrayList<>();

        ResultSet rs = null;
        for (String user : viewers) {
            String query = "select * from bot where `name`=?";   //checks if user is already banned
            try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
                stmt.setString(1,user);
                stmt.executeQuery();
                rs = stmt.executeQuery();
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
