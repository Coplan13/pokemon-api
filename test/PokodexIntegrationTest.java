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
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNAUTHORIZED;

/**
 * Created by jeromeheissler on 23/02/2017.
 */
public class PokodexIntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    private String token;
    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);

        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "1234", 1);

        String pokomonId = databaseSetup.createPokomon("tutafé", 1);
        String pokomonId1 = databaseSetup.createPokomon("Chichi", 2);

        databaseSetup.associatePokomonToUser(pokomonId, userId);

        databaseSetup.markPokomonAsSeenBy(userId, pokomonId);
        databaseSetup.markPokomonAsSeenBy(userId, pokomonId1);

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
    public void testPokodexWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokodex";
        WSResponse response = wsRequestBuilder.makeGet(url);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testPokodex() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokodex";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        Iterator<JsonNode> pokomons = node.elements();
        assertTrue(pokomons.hasNext());
        JsonNode poko = pokomons.next();

        List<String> keys = new ArrayList<>();
        poko.fieldNames().forEachRemaining(keys::add);
        assertEquals(Arrays.asList("number", "name", "picture", "parent_number", "candy_required", "captured"), keys);

        assertTrue(poko.get("name").isTextual());
        assertTrue(poko.get("picture").isTextual());
        assertTrue(poko.get("number").isInt());
        assertTrue(poko.get("captured").isBoolean());

        if(poko.get("number").asInt() == 1)
            assertTrue(poko.get("captured").asBoolean());
        else
            assertFalse(poko.get("captured").asBoolean());
        assertTrue(pokomons.hasNext());
        poko = pokomons.next();
        if(poko.get("number").asInt() == 1)
            assertTrue(poko.get("captured").asBoolean());
        else
            assertFalse(poko.get("captured").asBoolean());

        assertFalse(pokomons.hasNext());

    }

}
