import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;


public class Main
{

    public static void main (String[] args) throws IOException, SQLException, InterruptedException {

        Dotenv dotenv = null;
        dotenv = Dotenv.configure().load();
                System.getenv("SHELL");
                String token = dotenv.get("token");
                String dbURL = dotenv.get("dbURL");
                String password = dotenv.get("password");

        Connection connection = SQLsetup.connection(dbURL,"root", password);
        OAuth2Credential credential = new OAuth2Credential("twitch", token);

        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .withEnableHelix(true)
            .withChatAccount(credential)
            .build();

        JSON reader = new JSON();
        StartChecking.twitchinsightscheck(twitchClient, connection, reader, token);

    }


}
