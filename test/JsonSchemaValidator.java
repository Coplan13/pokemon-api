import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by jeromeheissler on 17/02/2017.
 */
public final class JsonSchemaValidator {

    public static void validateItemLine(JsonNode node) {
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        assertEquals(Arrays.asList("item", "amount"), keys);

        assertTrue(node.get("amount").isInt());
        validateItem(node.get("item"));
    }

    public static void validateItem(JsonNode node) {
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        assertEquals(Arrays.asList("_id", "name"), keys);

        assertTrue(node.get("_id").isTextual());
        assertTrue(node.get("name").isTextual());
    }

    public static void validateProfile(JsonNode node) {
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        assertEquals(Arrays.asList("_id", "username", "picture", "email", "candy"), keys);

        assertTrue(node.get("_id").isTextual());
        assertTrue(node.get("username").isTextual());
        assertTrue(node.get("picture").isTextual());
        assertTrue(node.get("email").isTextual());
        assertTrue(node.get("candy").isInt());
    }

    public static void validatePokostop(JsonNode node) {
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        if(keys.size() == 4) {
            assertEquals(Arrays.asList("_id", "name", "coordinates", "picture"), keys);
        } else {
            assertEquals(Arrays.asList("_id", "name", "coordinates", "picture", "timer"), keys);
        }

        assertTrue(node.get("_id").isTextual());
        assertTrue(node.get("name").isTextual());
        assertTrue(node.get("coordinates").isArray());
        Iterator<JsonNode> it = node.get("coordinates").elements();
        assertTrue(it.hasNext());
        assertTrue(it.next().isNumber());
        assertTrue(it.hasNext());
        assertTrue(it.next().isNumber());
        assertFalse(it.hasNext());
        assertTrue(node.get("picture").isTextual());

        if(node.has("timer")) {
            assertTrue(node.get("timer").isInt());
        }
    }

    public static void validatePokomon(JsonNode node) {
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        assertEquals(Arrays.asList("_id", "level", "attaque", "defense", "vitesse", "xp", "coordinates", "name", "picture", "number", "captured"), keys);

        assertTrue(node.get("_id").isTextual());
        assertTrue(node.get("name").isTextual());
        assertTrue(node.get("picture").isTextual());
        assertTrue(node.get("number").isInt());
        assertTrue(node.get("level").isInt());
        assertTrue(node.get("attaque").isInt());
        assertTrue(node.get("defense").isInt());
        assertTrue(node.get("vitesse").isInt());
        assertTrue(node.get("xp").isInt());
        Iterator<JsonNode> it = node.get("coordinates").elements();
        assertTrue(it.hasNext());
        assertTrue(it.next().isNumber());
        assertTrue(it.hasNext());
        assertTrue(it.next().isNumber());
        assertFalse(it.hasNext());
        assertTrue(node.get("captured").isBoolean());
    }

}
