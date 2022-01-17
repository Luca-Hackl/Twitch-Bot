import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.StreamList;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;


public class Main
{

    public static void main (String[] args) throws IOException {

        Dotenv dotenv = null;
        dotenv = Dotenv.configure().load();
        String token;
                System.getenv("SHELL");
                token = dotenv.get("token");

        OAuth2Credential credential = new OAuth2Credential("twitch", token);

        TwitchClient twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .withEnableHelix(true)
            .withEnableTMI(true)
            .withChatAccount(credential)
            .build();


        //twitchClient.getChat().sendMessage("wikwak3", "Hey!");

        JSON reader = new JSON();

        reader.chatterRead();

        StreamList resultList = twitchClient.getHelix().getStreams(token, null, null, 5, null, null, null, null).execute();
        resultList.getStreams().forEach(stream -> {
            System.out.println("Name:" + stream.getUserLogin());

            try {
                reader.biggestStreamer(stream.getUserLogin(), twitchClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }


}
