import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WSResponse;
import play.test.WithServer;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.*;
import static play.mvc.Http.Status.OK;

/**
 * Created by jeromeheissler on 23/02/2017.
 */
public class BagIntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    private String token;
    private String itemId;
    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);

        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "");

        itemId = databaseSetup.createItem("Pokoball");
        databaseSetup.associateUserAndItem(userId, itemId);

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
    public void testGetBagWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/bag";
        WSResponse response = wsRequestBuilder.makeGet(url);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testGetBagWithAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/bag";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());
        JsonNode node = Json.parse(response.getBody());

        Iterator<JsonNode> itemLines = node.elements();
        assertTrue(itemLines.hasNext());
        JsonNode item = itemLines.next();
        JsonSchemaValidator.validateItemLine(item);
    }

    @Test
    public void testUpdateBagWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/bag";
        WSResponse response = wsRequestBuilder.makePost(url, Json.parse("{}"));
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testUpdateBagWithoutBody() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/bag";
        WSResponse response = wsRequestBuilder.makePost(url, Json.parse("{}"), token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testUpdateBagWithoutAmount() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/bag";
        JsonNode jsonNode = Json.parse("{\"item\": {\"_id\": \""+itemId+"\", \"name\": \"pokoball\"}}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testUpdateBagWithoutItem() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/bag";
        JsonNode jsonNode = Json.parse("{\"amount\": 1}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testUpdateBag() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/bag";
        JsonNode jsonNode = Json.parse("{\"item\": {\"_id\": \""+itemId+"\", \"name\": \"pokoball\"}, \"amount\": 1}");
        WSResponse response = wsRequestBuilder.makePost(url, jsonNode, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        Iterator<JsonNode> itemLines = node.elements();
        assertTrue(itemLines.hasNext());
        JsonNode item = itemLines.next();
        JsonSchemaValidator.validateItemLine(item);
    }

}