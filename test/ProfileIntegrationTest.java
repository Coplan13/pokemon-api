import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WSResponse;
import play.test.WithServer;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.*;

/**
 * Created by jeromeheissler on 21/02/2017.
 */
public class ProfileIntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    private String token;
    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);
        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "");
        databaseSetup.createUser("doublon", "heissler.jerome@gmail.com", "");

        token = JWT.create()
                .withIssuer("auth0")
                .withClaim("_id", userId)
                .sign(Algorithm.HMAC256(app.config().getString("play.http.secret.key")));
    }

    @After
    public void tearDownDatabase() {
        app.injector().instanceOf(PlayJongo.class).getDatabase().dropDatabase();
    }

    @Test
    public void testGetProfileWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        WSResponse response = wsRequestBuilder.makeGet(url);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testGetProfileWithAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());
        JsonNode node = Json.parse(response.getBody());

        JsonSchemaValidator.validateProfile(node);
    }

    @Test
    public void testUpdateProfileWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        WSResponse response = wsRequestBuilder.makePost(url, Json.parse("{}"));
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testUpdateProfileWithoutBody() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        WSResponse response = wsRequestBuilder.makePost(url, Json.parse("{}"), token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testUpdateProfileWithMissingPicture() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome+new@nowly.co\", \"username\": \"jeromeheissler\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testUpdateProfileWithMissingUsername() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        JsonNode jsonNode = Json.parse("{\"email\": \"jerome+new@nowly.co\", \"picture\": \"http://xxxx.com/xxx.jpg\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testUpdateProfileWithMissingEmail() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        JsonNode jsonNode = Json.parse("{\"username\": \"jeromeheissler\", \"picture\": \"http://xxxx.com/xxx.jpg\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testUpdateProfileWithEmailAlreadyUsed() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        JsonNode jsonNode = Json.parse("{\"username\": \"jeromeheisslernew\", \"email\": \"heissler.jerome@gmail.com\", \"picture\": \"http://xxxx.com/xxx.jpg\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(FORBIDDEN, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        assertEquals(Arrays.asList("emailExist", "usernameExist"), keys);
        assertTrue(node.get("emailExist").isBoolean());
        assertTrue(node.get("usernameExist").isBoolean());

        assertTrue(node.get("emailExist").asBoolean());
        assertFalse(node.get("usernameExist").asBoolean());
    }

    @Test
    public void testUpdateProfileWithUsernameAlreadyUsed() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        JsonNode jsonNode = Json.parse("{\"username\": \"doublon\", \"email\": \"jerome@nowly.co\", \"picture\": \"http://xxxx.com/xxx.jpg\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(FORBIDDEN, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        assertEquals(Arrays.asList("emailExist", "usernameExist"), keys);
        assertTrue(node.get("emailExist").isBoolean());
        assertTrue(node.get("usernameExist").isBoolean());

        assertFalse(node.get("emailExist").asBoolean());
        assertTrue(node.get("usernameExist").asBoolean());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me";
        JsonNode jsonNode = Json.parse("{\"username\": \"jeromeheisslernew\", \"email\": \"jerome+new@nowly.co\", \"picture\": \"http://xxxx.com/xxx.jpg\"}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        JsonSchemaValidator.validateProfile(node);
    }

}
