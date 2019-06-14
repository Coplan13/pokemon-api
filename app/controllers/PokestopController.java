package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ItemDAO;
import dao.PokestopDAO;
import models.Pokestop;
import org.bson.types.ObjectId;
import play.libs.Json;
import play.mvc.*;
import token.TokenManager;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;

import static play.mvc.Results.*;

public class PokestopController extends Controller {

    @Inject
    private PokestopDAO pokestopDAO;

    @Inject
    private ItemDAO itemDAO;

    @Inject
    private TokenManager tokenManager;

    public Result takeItemAtPokostopWithoutAuthentication(Http.Request request, String id) {

        return tokenManager.getUser(request).map(user -> {

        JsonNode json = request.body().asJson();


        if (!ObjectId.isValid(id)) {
            return notFound();
        }

        ObjectNode item = Json.newObject();
        item.put("_id", pokestopDAO.findById(id).item_id);
        item.put("name", itemDAO.findById(pokestopDAO.findById(id).item_id).name);
        return ok(item);



        }).orElseGet(Results::unauthorized);
    }



}
