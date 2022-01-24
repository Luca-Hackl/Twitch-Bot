import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.StreamList;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public class Main
{

    public static void main (String[] args) throws IOException, SQLException {

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

        try {
            ArrayList <String> listOfBots = reader.biggestStreamer("wikwak3");
            ArrayList <String> unchecked = SQLsetup.botcheck(listOfBots, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //twitchClient.getChat().sendMessage("wikwak3", "Hey!");
        compare(twitchClient, token, reader);

    }

    public static void compare (TwitchClient twitchClient, String token, JSON reader){
        StreamList resultList = twitchClient.getHelix().getStreams(token, null, null, 1, null, null, null, null).execute();
        resultList.getStreams().forEach(stream -> {
            try {
                ArrayList <String> biggestStreamerChatters = reader.biggestStreamer(stream.getUserLogin());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}
