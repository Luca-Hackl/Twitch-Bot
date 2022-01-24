import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

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

    public static void ownChanel() throws IOException, JSONException {

        JSONObject json = readJsonFromUrl("https://tmi.twitch.tv/group/user/wikwak3/chatters");
        JSONObject test = json.getJSONObject("chatters");
        JSONArray chatters = test.getJSONArray("viewers");

    }

    public static ArrayList<String> biggestStreamer(String user) throws IOException, JSONException{

        JSONObject json = readJsonFromUrl("https://tmi.twitch.tv/group/user/" + user + "/chatters");
        JSONObject test = json.getJSONObject("chatters");
        JSONArray chatters = test.getJSONArray("viewers");

        ArrayList <String> list = new ArrayList<String>();
        for(int i=0; i < chatters.length(); i++) {
            list.add(chatters.getString(i));
        }

        return list;


    }
}
