package dao;

import models.Pokemon;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
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

    public ArrayList<Pokemon> allPokemon(String profileId){

        ArrayList<Pokemon> pokemonList = new ArrayList<>();
        MongoCursor<Pokemon> result = pokemon.find("{profile_id: #}", profileId).as(Pokemon.class);
        result.forEach(pokemonList::add);

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
