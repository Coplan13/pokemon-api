import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;
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
public class SignupIntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    @Before
    public void setupDatabase() {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);
        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "");
    }

    @After
    public void tearDownDatabase() {
        app.injector().instanceOf(PlayJongo.class).getDatabase().dropDatabase();
    }

    @Test
    public void testSignupWithMissingAllParameters() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/signup";
        JsonNode jsonNode = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testSignupWithMissingEmailParameters() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/signup";
        JsonNode jsonNode = Json.parse("{\"password\": \"\", \"username\": \"\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testSignupWithMissingPasswordParameters() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/signup";
        JsonNode jsonNode = Json.parse("{\"email\": \"\", \"username\": \"\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testSignupWithMissingUsernameParameters() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/signup";
        JsonNode jsonNode = Json.parse("{\"email\": \"\", \"password\": \"\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testSignupWithEmailAlreadyUsed() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/signup";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome@nowly.co\", \"password\": \"\", \"username\": \"\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(FORBIDDEN, response.getStatus());
    }

    @Test
    public void testSignupWithUsernameAlreadyUsed() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/signup";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome+new@nowly.co\", \"password\": \"\", \"username\": \"jeromeheissler\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode);
        assertEquals(FORBIDDEN, response.getStatus());
    }

    @Test
    public void testSignupSucceed() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/signup";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome+new@nowly.co\", \"password\": \"0000\", \"username\": \"jeromeheisslernew\"}");
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
