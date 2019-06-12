package dao;

import models.Pokedex;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

public class PokedexDAO extends ModelDAO<Pokedex, String>  {

    private MongoCollection pokedex;

    public PokedexDAO(PlayJongo jongo) {
        super(jongo);

        pokedex = jongo.getCollection("pokedex");
    }

    @Override
    public Pokedex findById(String id) {
        return pokedex.findOne(new ObjectId(id)).as(Pokedex.class);
    }

    @Override
    public void insert(Pokedex model) {
        pokedex.insert(model);
    }

    @Override
    public void update(Pokedex model) {

    }

    @Override
    public void remove(Pokedex model) {

    }

}
