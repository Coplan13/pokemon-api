import org.junit.*;

import play.libs.ws.WSResponse;
import play.test.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class IntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    @Before
    public void initWSRequestBuilder() {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());
    }

    @Test
    public void testHomeWithServer() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/";
        WSResponse response = wsRequestBuilder.makeGet(url);
        assertEquals(OK, response.getStatus());
    }

}
