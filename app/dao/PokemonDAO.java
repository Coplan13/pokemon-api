package dao;

import models.Pokemon;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;



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

    public Long findAll(){
        return pokemon.count();
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
