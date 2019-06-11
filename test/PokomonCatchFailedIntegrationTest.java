import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.libs.Json;
import play.libs.ws.WSResponse;
import play.test.Helpers;
import play.test.WithServer;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.FORBIDDEN;

/**
 * Created by jeromeheissler on 28/02/2017.
 */
public class PokomonCatchFailedIntegrationTest extends WithServer {

    private String pokomonIdFailed;

    WSRequestBuilder wsRequestBuilder;
    private String token;
    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);
        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "1234", 1);

        pokomonIdFailed = databaseSetup.createPokomonToCatch(new ObjectId().toString());

        token = JWT.create()
                .withIssuer("auth0")
                .withClaim("_id", userId)
                .sign(Algorithm.HMAC256(app.config().getString("play.http.secret.key")));
    }

    @After
    public void tearDownDatabase() {
        app.injector().instanceOf(PlayJongo.class).getDatabase().dropDatabase();
    }

    protected Application provideApplication() {
        Map<String, Object> config = new HashMap<>();
        config.put("pokomon.cheat.capture-none", true);
        return Helpers.fakeApplication(config);
    }

    @Test
    public void testCatchPokomonFailed() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/"+pokomonIdFailed;
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(FORBIDDEN, response.getStatus());
    }

}
