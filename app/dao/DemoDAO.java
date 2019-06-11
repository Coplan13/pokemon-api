package dao;

import models.Demo;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

public class DemoDAO extends ModelDAO<Demo, String> {

    private MongoCollection demo;

    public DemoDAO(PlayJongo jongo) {
        super(jongo);

        demo = jongo.getCollection("demo");
    }

    @Override
    public Demo findById(String id) {
        return demo.findOne(new ObjectId(id)).as(Demo.class);
    }

    @Override
    public void insert(Demo model) {

    }

    @Override
    public void update(Demo model) {

    }

    @Override
    public void remove(Demo model) {

    }
}
