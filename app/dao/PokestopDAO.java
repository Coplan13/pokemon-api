package dao;

import models.Pokestop;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

public class PokestopDAO extends ModelDAO<Pokestop, String>  {

    private MongoCollection pokestop;

    public PokestopDAO(PlayJongo jongo) {
        super(jongo);

        pokestop = jongo.getCollection("pokestop");
    }

    @Override
    public Pokestop findById(String id) {
        return pokestop.findOne(new ObjectId(id)).as(Pokestop.class);
    }

    @Override
    public void insert(Pokestop model) {
        pokestop.insert(model);
    }

    @Override
    public void update(Pokestop model) {

    }

    @Override
    public void remove(Pokestop model) {

    }

}
