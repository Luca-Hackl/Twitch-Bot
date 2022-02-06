import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.StreamList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class IsBot {

    private static JSONObject json;

    public static ArrayList<String> biggestStreamer(String user) throws IOException, JSONException {

        JSONObject json = JSON.readJsonFromUrl("https://tmi.twitch.tv/group/user/" + user + "/chatters");
        JSONObject test = json.getJSONObject("chatters");
        JSONArray chatters = test.getJSONArray("viewers");

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < chatters.length(); i++) {
            list.add(chatters.getString(i));    //adds all users of streamer to Array
        }
        return list;

    }

    public static void finalBotCheck(ArrayList<String> allViewers, ArrayList<String> myViewers, TwitchClient twitchClient) {

        for (String user : myViewers) {
            if (allViewers.contains(user)) {
                twitchClient.getChat().sendMessage("wikwak3", "/ban " + user);  //bans bots via comparison with biggest streamers
            }
        }
    }


    public static ArrayList<String> compare(TwitchClient twitchClient, String token) {
        ArrayList<String> viewersBigStreams = new ArrayList<>();
        ArrayList<String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("de");

        StreamList resultList = twitchClient.getHelix().getStreams(token, null, null, 99, null, languages, null, null).execute(); //gets biggest streamers
        resultList.getStreams().forEach(stream -> {
            try {
                ArrayList<String> biggestStreamerChatters = IsBot.biggestStreamer(stream.getUserLogin());
                viewersBigStreams.addAll(biggestStreamerChatters);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return viewersBigStreams;
    }

}
