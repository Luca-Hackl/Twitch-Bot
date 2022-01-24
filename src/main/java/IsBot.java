import com.github.twitch4j.TwitchClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class IsBot {

    public static ArrayList<String> biggestStreamer(String user, JSON reader) throws IOException, JSONException {

        JSONObject json = reader.readJsonFromUrl("https://tmi.twitch.tv/group/user/" + user + "/chatters");
        JSONObject test = json.getJSONObject("chatters");
        JSONArray chatters = test.getJSONArray("viewers");

        ArrayList <String> list = new ArrayList();
        for(int i=0; i < chatters.length(); i++) {
            list.add(chatters.getString(i));
        }
        return list;


    }

    public static void finalBotCheck(ArrayList <String> allViewers, ArrayList <String> myViewers, TwitchClient twitchClient) {

        for (int i = 0; i < myViewers.size(); i++){
            String user = myViewers.get(i);
            if (allViewers.contains(user)) {
                twitchClient.getChat().sendMessage("wikwak3", "/ban{" + user + "}");
            }
        }



    }

}
