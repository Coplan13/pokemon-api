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
import static org.junit.Assert.fail;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNAUTHORIZED;

/**
 * Created by jeromeheissler on 23/02/2017.
 */
public class MyPokomonIntegrationTest extends WithServer {

    WSRequestBuilder wsRequestBuilder;
    private String token;
    private String token2;
    @Before
    public void setupDatabase() throws UnsupportedEncodingException {
        wsRequestBuilder = new WSRequestBuilder(this.testServer.port());

        PlayJongo jongo = app.injector().instanceOf(PlayJongo.class);
        DatabaseSetup databaseSetup = new DatabaseSetup(jongo);
        String userId = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "1234", 1);
        String user1Id = databaseSetup.createUser("jeromeheissler", "jerome@nowly.co", "1234", 0);

        String parentId = databaseSetup.createPokomon("tutafé", 1);

        String pokomonId = databaseSetup.createPokomon("tutafa2", 2, 1, databaseSetup.getPokomonNumber(parentId));

        databaseSetup.associatePokomonToUser(pokomonId, userId);
        databaseSetup.associatePokomonToUser(pokomonId, user1Id);

        token = JWT.create()
                .withIssuer("auth0")
                .withClaim("_id", userId)
                .sign(Algorithm.HMAC256(app.config().getString("play.http.secret.key")));

        token2 = JWT.create()
                .withIssuer("auth0")
                .withClaim("_id", user1Id)
                .sign(Algorithm.HMAC256(app.config().getString("play.http.secret.key")));
    }

    @After
    public void tearDownDatabase() {
        app.injector().instanceOf(PlayJongo.class).getDatabase().dropDatabase();
    }

    @Test
    public void testMyPokomonWithoutAuthentication() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokomon";
        WSResponse response = wsRequestBuilder.makeGet(url);
        assertEquals(UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testMyPokomon() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokomon";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        Iterator<JsonNode> pokomons = node.elements();
        while(pokomons.hasNext()) {
            JsonNode poko = pokomons.next();
            JsonSchemaValidator.validatePokomon(poko);
        }
    }

    @Test
    public void testMyPokomonOneNotFound() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokomon/444";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testMyPokomonOne() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokomon";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        Iterator<JsonNode> pokomons = node.elements();
        if(pokomons.hasNext()) {
            JsonNode onePokomon = pokomons.next();

            url = "http://localhost:" + this.testServer.port() + "/me/pokomon/" + onePokomon.get("_id").asText();
            response = wsRequestBuilder.makeGet(url, token);
            assertEquals(OK, response.getStatus());

            JsonNode pokomon = Json.parse(response.getBody());
            JsonSchemaValidator.validatePokomon(pokomon);
        } else {
            fail("no pokomon to test");
        }
    }

    @Test
    public void testMyPokomonEvolveNotExist() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokomon/444";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(NOT_FOUND, response.getStatus());
    }

    @Test
    public void testMyPokomonEvolveOneWithoutCandy() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokomon";
        WSResponse response = wsRequestBuilder.makeGet(url, token2);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        Iterator<JsonNode> pokomons = node.elements();
        if(pokomons.hasNext()) {
            JsonNode onePokomon = pokomons.next();

            url = "http://localhost:" + this.testServer.port() + "/me/pokomon/" + onePokomon.get("_id").asText()+"/evolve";
            response = wsRequestBuilder.makePost(url, Json.toJson("{}"), token2);
            assertEquals(UNAUTHORIZED, response.getStatus());
        }else {
            fail("no pokomon to test");
        }
    }

    @Test
    public void testMyPokomonEvolveOne() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/me/pokomon";
        WSResponse response = wsRequestBuilder.makeGet(url, token);
        assertEquals(OK, response.getStatus());

        JsonNode node = Json.parse(response.getBody());
        Iterator<JsonNode> pokomons = node.elements();
        if(pokomons.hasNext()) {
            JsonNode onePokomon = pokomons.next();

            url = "http://localhost:" + this.testServer.port() + "/me/pokomon/" + onePokomon.get("_id").asText()+"/evolve";
            response = wsRequestBuilder.makePost(url, Json.toJson("{}"), token);
            assertEquals(OK, response.getStatus());

            JsonNode pokomon = Json.parse(response.getBody());
            JsonSchemaValidator.validatePokomon(pokomon);
        }else {
            fail("no pokomon to test");
        }
    }

}
