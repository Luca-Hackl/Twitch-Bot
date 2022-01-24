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

        IsBot comparer = new IsBot();

        JSON reader = new JSON();
        ArrayList<String> unchecked = null;
        try {
            ArrayList <String> listOfBots = comparer.biggestStreamer("wikwak3", reader);
            unchecked = SQLsetup.normalUserCheck(listOfBots, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList <String> viewersBigStreams = compare(twitchClient, token, comparer, reader);

        comparer.finalBotCheck(viewersBigStreams, unchecked, twitchClient);

    }

    public static ArrayList <String> compare (TwitchClient twitchClient, String token, IsBot comparer, JSON reader){
        ArrayList <String> viewersBigStreams = new ArrayList<>();

        StreamList resultList = twitchClient.getHelix().getStreams(token, null, null, 1, null, null, null, null).execute();
        resultList.getStreams().forEach(stream -> {
            try {
                ArrayList <String> biggestStreamerChatters = comparer.biggestStreamer(stream.getUserLogin(), reader);
                viewersBigStreams.addAll(biggestStreamerChatters);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return viewersBigStreams;
    }

}
