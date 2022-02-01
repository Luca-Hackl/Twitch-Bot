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
                String bots = String.valueOf(onlineBots.toString().split(",")[0]);  //gets name of bot
                String name = bots.substring(2, bots.length() - 1);
                if (!BotDatabase.alreadyBanned(connection, name)){  //if the bot is not already banned, ban it
                    twitchClient.getChat().sendMessage("wikwak3", "/ban " + name);
                    BotDatabase.addsBannedBots(connection, name);
                }
            }
            TimeUnit.MINUTES.sleep(5);
            counter++;
            if (counter==6){    //after 30 minutes check for bots via comparison with biggest streamers
                compareCheck(reader, connection, twitchClient, token);
                counter=0;
            }
        }
    }

    private static void compareCheck(JSON reader, Connection connection, TwitchClient twitchClient, String token){

        ArrayList<String> unchecked = null;
        try {
            ArrayList <String> listOfBots = IsBot.biggestStreamer("wikwak3", reader);   //gets users that currently watch own channel
            unchecked = SQLsetup.normalUserCheck(listOfBots, connection);                   //checks if name is in database
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList <String> viewersBigStreams = IsBot.compare(twitchClient, token, reader);
        assert unchecked != null;
        IsBot.finalBotCheck(viewersBigStreams, unchecked, twitchClient);

    }
}
