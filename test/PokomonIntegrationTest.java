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
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.*;

/**
 * Created by jeromeheissler on 21/02/2017.
 */
public class PokomonIntegrationTest extends WithServer {

    private String pokomonId;
    private String pokomonIdTwice;
    private String pokomonCatchedId;

    WSRequestBuilder wsRequestBuilder;
    private String token;
    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);

        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "1234", 0);

        String ApokomonId = databaseSetup.createPokomon("tutafé", 1);

        pokomonId = databaseSetup.createPokomonToCatch(ApokomonId);

        pokomonCatchedId = databaseSetup.associatePokomonToUser(ApokomonId, userId);

        pokomonIdTwice = databaseSetup.createPokomonToCatch(ApokomonId);

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
        config.put("pokomon.cheat.capture-all", true);
        return Helpers.fakeApplication(config);
    }

    @Test
    public void testSeePokomonWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/0";
        WSResponse response = wsRequestBuilder.makeGet(url);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testSeePokomonWithoutId() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testSeePokomonWithWrongId() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/9999";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testSeePokomon() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/"+pokomonId;
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        JsonSchemaValidator.validatePokomon(node);
    }

    @Test
    public void testSeePokomonCatched() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/"+pokomonCatchedId;
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testCatchPokomonWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/0";
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testCatchPokomonWithoutId() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/";
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testCatchPokomonWithWrongId() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/9999";
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testCatchPokomon() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/"+pokomonId;
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        JsonSchemaValidator.validatePokomon(node);
    }

    @Test
    public void testCatchPokomonTwice() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/"+pokomonIdTwice;
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(OK, response.getStatus());

        response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testCatchAndHasMoreCandy() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokomon/"+pokomonId;
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        JsonSchemaValidator.validatePokomon(node);

        url = "http://localhost:" + this.testServer.port() + "/me";
        response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());
        node = Json.parse(response.getBody());

        JsonSchemaValidator.validateProfile(node);
        assertEquals(3, node.get("candy").asInt());
    }
}