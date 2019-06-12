package models;

import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Item {

    @MongoObjectId
    public String _id;

    public String name;

    public Item() {

    }

    public Item(String name) {
        this.name = name;
    }
}
