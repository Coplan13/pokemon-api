package controllers;

import dao.PokemonDAO;
import dao.ProfileDAO;
import play.mvc.Controller;
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


    public Result allPokemon() throws Exception {


        return ok("collection pokemon");

    }






}
