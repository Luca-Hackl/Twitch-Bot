import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.StreamList;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


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

        IsBot comparer = new IsBot();

        JSON reader = new JSON();
        ArrayList<String> unchecked = null;

        try {
            ArrayList <String> listOfBots = IsBot.biggestStreamer("wikwak3", reader);
            unchecked = SQLsetup.normalUserCheck(listOfBots, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList <String> viewersBigStreams = compare(twitchClient, token, comparer, reader);
        assert unchecked != null;
        IsBot.finalBotCheck(viewersBigStreams, unchecked, twitchClient);
        

        while(true){
            JSONObject twitchinsights = JSON.readJsonFromUrl("https://api.twitchinsights.net/v1/bots/online");
            JSONArray activeBots = twitchinsights.getJSONArray("bots");
            for (Object onlineBots : activeBots){
                String bots = String.valueOf(onlineBots.toString().split(",")[0]);
                String name = bots.substring(2, bots.length() - 1);
                if (!BotDatabase.alreadyBanned(connection, name)){
                    twitchClient.getChat().sendMessage("wikwak3", "/ban " + name);
                    BotDatabase.addsBannedBots(connection, name);
                }
            }

            TimeUnit.MINUTES.sleep(1);
        }

    }

    private static ArrayList <String> compare (TwitchClient twitchClient, String token, IsBot comparer, JSON reader){
        ArrayList <String> viewersBigStreams = new ArrayList<>();
        ArrayList <String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("de");

        StreamList resultList = twitchClient.getHelix().getStreams(token, null, null, 20, null, languages, null, null).execute();
        resultList.getStreams().forEach(stream -> {
            try {
                ArrayList <String> biggestStreamerChatters = IsBot.biggestStreamer(stream.getUserLogin(), reader);
                viewersBigStreams.addAll(biggestStreamerChatters);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return viewersBigStreams;
    }

}
