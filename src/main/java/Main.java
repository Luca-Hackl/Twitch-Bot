import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws IOException, SQLException, InterruptedException {

        Dotenv dotenv = Dotenv.configure().load();
        String token = dotenv.get("token");
        String dbURL = dotenv.get("dbURL");
        String password = dotenv.get("password");

        BotDatabase conni = new BotDatabase(dbURL, "root", password);

        assert token != null;
        OAuth2Credential credential = new OAuth2Credential("twitch", token);

        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withEnableHelix(true)
                .withChatAccount(credential)
                .build();


        StartChecking checker = new StartChecking(conni);
        checker.twitchInsightsCheck(twitchClient, token); //uses twitchInsights API to see active bots

    }


}
