package models;

import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Pokestop {

    @MongoObjectId
    public String _id;

    public String name;
    public String picture;
    public Float timer;
    public Float tableauCoordinates[];

    public Pokestop() {

    }

    public Pokestop(String name, String picture, Float timer, Float[] tableauCoordinates) {
        this.name = name;
        this.picture = picture;
        this.timer = timer;
        this.tableauCoordinates = tableauCoordinates;
    }
}
