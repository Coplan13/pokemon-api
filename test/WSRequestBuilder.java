import com.fasterxml.jackson.databind.JsonNode;
import play.test.WSTestClient;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;

/**
 * Created by jeromeheissler on 17/02/2017.
 */
public final class WSRequestBuilder {

    private int port;
    public WSRequestBuilder(int port) {
        this.port = port;
    }

    public WSResponse makeGet(String url) throws Exception {
        // See https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (WSClient ws = WSTestClient.newClient(port)) {
            CompletionStage<WSResponse> stage = ws.url(url).get();
            return stage.toCompletableFuture().get();
        }
    }
    public WSResponse makeGet(String url, String token) throws Exception {
        // See https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (WSClient ws = WSTestClient.newClient(port)) {
            CompletionStage<WSResponse> stage = ws.url(url).addHeader("Authorization", "Bearer "+token).get();
            return stage.toCompletableFuture().get();
        }
    }

    public WSResponse makePost(String url, JsonNode jsonNode) throws Exception {
        // See https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (WSClient ws = WSTestClient.newClient(port)) {
            CompletionStage<WSResponse> stage = ws.url(url).post(jsonNode);
            return stage.toCompletableFuture().get();
        }
    }
    public WSResponse makePost(String url, JsonNode jsonNode, String token) throws Exception {
        // See https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (WSClient ws = WSTestClient.newClient(port)) {
            CompletionStage<WSResponse> stage = ws.url(url).addHeader("Authorization", "Bearer "+token).post(jsonNode);
            return stage.toCompletableFuture().get();
        }
    }

}
