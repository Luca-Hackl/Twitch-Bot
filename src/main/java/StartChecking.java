import com.github.twitch4j.TwitchClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StartChecking {

    public static void twitchinsightscheck(TwitchClient twitchClient, Connection connection, JSON reader, String token) throws SQLException, IOException, InterruptedException {
        int counter = 0;

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
            TimeUnit.MINUTES.sleep(5);
            counter++;
            if (counter==6){
                compareCheck(reader, connection, twitchClient, token);
                counter=0;
            }
        }
    }

    private static void compareCheck(JSON reader, Connection connection, TwitchClient twitchClient, String token){

        ArrayList<String> unchecked = null;
        try {
            ArrayList <String> listOfBots = IsBot.biggestStreamer("wikwak3", reader);
            unchecked = SQLsetup.normalUserCheck(listOfBots, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IsBot comparer = new IsBot();

        ArrayList <String> viewersBigStreams = IsBot.compare(twitchClient, token, comparer, reader);
        assert unchecked != null;
        IsBot.finalBotCheck(viewersBigStreams, unchecked, twitchClient);

    }
}
