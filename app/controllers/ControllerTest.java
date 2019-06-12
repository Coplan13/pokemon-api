package controllers;

import dao.*;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;

public class ControllerTest extends Controller {

    private final ProfileDAO profileDAO;
    private final PokemonDAO pokemonDAO;
    private final PokedexDAO pokedexDAO;
    private final ItemDAO itemDAO;
    private final PokestopDAO pokestopDAO;

    @Inject
    public ControllerTest(PlayJongo playJongo) {
        this.profileDAO = new ProfileDAO(playJongo);
        this.pokemonDAO = new PokemonDAO(playJongo);
        this.pokedexDAO = new PokedexDAO(playJongo);
        this.itemDAO = new ItemDAO(playJongo);
        this.pokestopDAO = new PokestopDAO(playJongo);
    }


    public Result test() {

        Profile profile = new Profile();
        profile.email = "heissler.jerome";
        profileDAO.insert(profile);

        Pokemon pokemon = new Pokemon();
        pokemon.nickname = "toto";
        pokemonDAO.insert(pokemon);

        Pokedex pokedex = new Pokedex();
        pokedex.name = "Pikachu";
        pokedexDAO.insert(pokedex);

        Item item = new Item();
        item.name = "Pokeball";
        itemDAO.insert(item);

        Pokestop pokestop = new Pokestop();
        pokestop.name = "Cefim center";
        pokestopDAO.insert(pokestop);




        return ok("c'est ok (controller test)");
    }

}
