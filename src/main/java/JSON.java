import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.tmi.domain.Chatters;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static void chatterRead() throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("https://tmi.twitch.tv/group/user/wikwak3/chatters");
        JSONObject test = json.getJSONObject("chatters");
        JSONArray chatters = test.getJSONArray("viewers");
        System.out.println(chatters);
    }

    public static void biggestStreamer(String user, TwitchClient client) throws IOException, JSONException{
        System.out.println(user);

        JSONObject json = readJsonFromUrl("https://tmi.twitch.tv/group/user/" + user + "/chatters");
        JSONObject test = json.getJSONObject("chatters");
        JSONArray chatters = test.getJSONArray("viewers");
        System.out.println(chatters);

    }
}
