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
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.*;

/**
 * Created by jeromeheissler on 21/02/2017.
 */
public class PokostopIntegrationTest extends WithServer {

    private String pokostopId;
    private String pokostopId2;
    WSRequestBuilder wsRequestBuilder;
    private String token;

    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);

        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "");

        databaseSetup.createItem("Pokoball");

        pokostopId = databaseSetup.createStop("Pokostop1", new double[]{0.0, 0.0});
        pokostopId2 = databaseSetup.createStop("Pokostop2", new double[]{0.0, 0.0});

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
    public void testTakeItemAtPokostopWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokostop/0";
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testTakeItemAtPokostopWithoutId() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokostop/";
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testTakeItemAtPokostopWithWrongId() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokostop/9999";
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testTakeItemAtPokostop() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokostop/"+pokostopId;
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        assertTrue(node.isArray());
        Iterator<JsonNode> items = node.elements();
        assertTrue(items.hasNext());
        JsonNode item = items.next();
        JsonSchemaValidator.validateItem(item);

    }

    @Test
    public void testTakeItemAtPokostopTwice() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/pokostop/"+pokostopId2;
        JsonNode body = Json.parse("{}");
        WSResponse response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(OK, response.getStatus());

        response = wsRequestBuilder.makePost(url, body, token);
        assertEquals(FORBIDDEN, response.getStatus());
    }
}
