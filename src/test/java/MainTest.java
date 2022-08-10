import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("Sets up connection and twitch tokens")
    void main() throws Exception {

        BotDatabaseTest test = new BotDatabaseTest();
        test.testMockDBConnection();
    }
}