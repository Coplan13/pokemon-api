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
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNAUTHORIZED;

/**
 * Created by jeromeheissler on 20/02/2017.
 */
public class MapIntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    private String token;
    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);

        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "1234");
        databaseSetup.createStop("pokostop1", new double[]{0.0, 0.0});
        databaseSetup.createStop("pokostop2", new double[]{0.0, 0.0});
        databaseSetup.createPokomon("tutafé", 1);

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
    public void testMapWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/map?lat=0&lng=0";
        WSResponse response = wsRequestBuilder.makeGet(url);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testMapWithoutQueryString() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/map";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testMapWithoutQueryStringLat() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/map?lng=0";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testMapWithoutQueryStringLng() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/map?lat=0";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testMap() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/map?lat=0&lng=0";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);

        assertEquals(Arrays.asList("pokomons", "pokostops"), keys);
        assertTrue(node.get("pokomons").isArray());
        assertTrue(node.get("pokostops").isArray());

        Iterator<JsonNode> pokomons = node.get("pokomons").elements();
        assertTrue(pokomons.hasNext());
        JsonNode poko = pokomons.next();
        JsonSchemaValidator.validatePokomon(poko);
        assertFalse(pokomons.hasNext());

        Iterator<JsonNode> pokostops = node.get("pokostops").elements();
        assertTrue(pokostops.hasNext());
        JsonNode stop = pokostops.next();
        JsonSchemaValidator.validatePokostop(stop);

        assertTrue(pokostops.hasNext());
        stop = pokostops.next();
        JsonSchemaValidator.validatePokostop(stop);

        assertFalse(pokostops.hasNext());
    }
}
