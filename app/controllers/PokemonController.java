package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.PokemonDAO;
import dao.ProfileDAO;
import models.Pokemon;
import org.jongo.MongoCollection;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
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

    public Result allPokemon() {



        Long collectionPokemon = pokemonDAO.findAll();


        System.out.println("long "+ collectionPokemon);


        return ok("collection pokemon");

    }






}
