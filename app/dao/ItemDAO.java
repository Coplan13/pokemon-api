package dao;

import models.Item;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

public class ItemDAO extends ModelDAO<Item, String>  {

    private MongoCollection item;

    public ItemDAO(PlayJongo jongo) {
        super(jongo);

        item = jongo.getCollection("item");
    }

    @Override
    public Item findById(String id) {
        return item.findOne(new ObjectId(id)).as(Item.class);
    }

    @Override
    public void insert(Item model) {
        item.insert(model);
    }

    @Override
    public void update(Item model) {

    }

    @Override
    public void remove(Item model) {

    }

}
