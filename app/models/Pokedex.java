package models;

import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Pokedex {

    @MongoObjectId
    public String _id;

    public String name;
    public String picture;
    public Integer parentNumber;
    public Integer number;

    public Pokedex() {

    }

    public Pokedex(String name, String picture, Integer parentNumber, Integer number) {
        this.name = name;
        this.picture = picture;
        this.parentNumber = parentNumber;
        this.number = number;
    }
}
