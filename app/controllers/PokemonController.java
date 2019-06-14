package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.PokemonDAO;
import dao.ProfileDAO;
import org.bson.types.ObjectId;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import token.TokenManager;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;

public class PokemonController extends Controller {

    private final PokemonDAO pokemonDAO;
    private final ProfileDAO profileDAO;

    @Inject
    public PokemonController(PlayJongo playJongo) {
        this.pokemonDAO = new PokemonDAO(playJongo);
        this.profileDAO = new ProfileDAO(playJongo);
    }

    @Inject
    private TokenManager tokenManager;




    public Result allPokemon(Http.Request request, String id) {

        return tokenManager.getUser(request).map(user -> {

            JsonNode json = request.body().asJson();


            if (!ObjectId.isValid(id)) {
                return notFound();
            }



            return null;


        }).orElseGet(Results::unauthorized);
    }






}
