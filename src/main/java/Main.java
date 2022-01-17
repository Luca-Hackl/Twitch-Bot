import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.UserList;

import io.github.cdimascio.dotenv.Dotenv;
//import io.github.cdimascio.dotenv.DotenvException;

import java.io.*;
import java.util.Arrays;


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
            .withChatAccount(credential)
            .build();

        //twitchClient.getChat().sendMessage("wikwak3", "Hey!");

        JSON reader = new JSON();

        reader.chatterRead();

        StreamList resultList = twitchClient.getHelix().getStreams(token, null, null, 5, null, null, null, null).execute();
        resultList.getStreams().forEach(stream -> {
            System.out.println("ID: " + stream.getId() + " - Title: " + stream.getTitle());
        });

        UserList resultList2 = twitchClient.getHelix().getUsers(token, Arrays.asList("451745803"), null).execute();
        resultList2.getUsers().forEach(user -> {
            System.out.println("USER:" + user);

            try {
                reader.biggestStreamer(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }


}
