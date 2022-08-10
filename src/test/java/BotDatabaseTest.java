import java.sql.Connection;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class BotDatabaseTest {

    @InjectMocks
    private BotDatabase connection;
    @Mock
    private Connection mockConnection;
    @Mock private Statement mockStatement;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMockDBConnection() throws Exception {
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
        boolean value = connection.alreadyBanned("lurxx");
        Assert.assertEquals(value, true);
        Mockito.verify(mockConnection.createStatement(), Mockito.times(1));
    }

}