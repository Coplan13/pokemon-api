package dao;

import models.Pokemon;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.ArrayList;
import java.util.List;

public class PokemonDAO extends ModelDAO<Pokemon, String>  {

    private MongoCollection pokemon;

    public PokemonDAO(PlayJongo jongo) {
        super(jongo);

        pokemon = jongo.getCollection("pokemon");
    }

    @Override
    public Pokemon findById(String id) {
        return pokemon.findOne(new ObjectId(id)).as(Pokemon.class);
    }

    public ArrayList<Pokemon> allPokemon(){

        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        for(Integer i = 0; i > pokemon.count(); i++){

          //  Pokemon pok = pokemon
        }

        return pokemonList;
    }



    @Override
    public void insert(Pokemon model) {
        pokemon.insert(model);
    }

    @Override
    public void update(Pokemon model) {

    }

    @Override
    public void remove(Pokemon model) {

    }

}
