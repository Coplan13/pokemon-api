import com.fasterxml.jackson.databind.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WSResponse;
import play.test.WithServer;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.*;

/**
 * Created by jeromeheissler on 17/02/2017.
 */
public class LoginIntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    @Before
    public void setupDatabase() {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);

        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "1234");
    }

    @After
    public void tearDownDatabase() {
        app.injector().instanceOf(PlayJongo.class).getDatabase().dropDatabase();
    }

    @Test
    public void testLoginWithMissingAllParameters() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/login";
        JsonNode jsonNode = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testLoginWithMissingEmailParameters() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/login";
        JsonNode jsonNode = Json.parse("{\"password\": \"\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testLoginWithMissingPasswordParameters() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/login";
        JsonNode jsonNode = Json.parse("{\"email\": \"\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testLoginWithUnknownEmail() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/login";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome@moi.moi\", \"password\": \"\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/login";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome@nowly.co\", \"password\": \"0000\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(FORBIDDEN, response.getStatus());
    }

    @Test
    public void testLoginSucceed() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/login";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome@nowly.co\", \"password\": \"1234\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(OK, response.getStatus());
        JsonNode node = Json.parse(response.getBody());
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);

        assertEquals(Arrays.asList("profile", "token"), keys);
        assertTrue(node.get("token").isTextual());
        JsonSchemaValidator.validateProfile(node.get("profile"));
    }

}
