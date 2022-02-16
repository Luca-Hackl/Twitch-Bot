import com.github.twitch4j.TwitchClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StartChecking { //checker

    private final BotDatabase conni;

    public StartChecking(BotDatabase conni) {
            this.conni = conni;
    }

    public void twitchInsightsCheck(TwitchClient twitchClient, String token) throws SQLException, IOException, InterruptedException {
        int counter = 0;

        JSON jsonFromUrl = new JSON("https://api.twitchinsights.net/v1/bots/online");

        while (true) {
            JSONObject twitchinsights = jsonFromUrl.readJsonFromUrl();
            JSONArray activeBots = twitchinsights.getJSONArray("bots");
            for (Object onlineBots : activeBots) {
                String bots = String.valueOf(onlineBots.toString().split(",")[0]);  //gets name of bot
                String name = bots.substring(2, bots.length() - 1);
                if (!conni.alreadyBanned(name)) {  //if the bot is not already banned, ban it
                    twitchClient.getChat().sendMessage("wikwak3", "/ban " + name);
                    conni.addsBannedBots(name);
                }
            }
            TimeUnit.MINUTES.sleep(5);
            counter++;
            if (counter == 6) {    //after 30 minutes check for bots via comparison with biggest streamers
                compareCheck(twitchClient, token);
                counter = 0;
            }
        }
    }

    private void compareCheck(TwitchClient twitchClient, String token) {

        ArrayList<String> unchecked = null;
        try {
            ArrayList<String> listOfBots = IsBot.biggestStreamer("wikwak3");   //gets users that currently watch own channel
            unchecked = this.conni.normalUserCheck(listOfBots);                  //checks if name is in database
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> viewersBigStreams = IsBot.compare(twitchClient, token);
        assert unchecked != null;
        IsBot.finalBotCheck(viewersBigStreams, unchecked, twitchClient);

    }
}
