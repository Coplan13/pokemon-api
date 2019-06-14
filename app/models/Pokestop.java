package models;

import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Pokestop {

    @MongoObjectId
    public String _id;

    public String name;
    public String picture;
    public double timer;
    public double tableauCoordinates[];

    public String item_id;

    public Pokestop() {

    }

    public Pokestop(String name, String picture, Double timer, double[] tableauCoordinates, String item_id) {
        this.name = name;
        this.picture = picture;
        this.timer = timer;
        this.tableauCoordinates = tableauCoordinates;
        this.item_id = item_id;
    }
}
