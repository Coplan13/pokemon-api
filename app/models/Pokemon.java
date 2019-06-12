package models;

import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Pokemon {

    @MongoObjectId
    public String _id;

    public String nickname;
    public Integer level;
    public Integer xp;
    public Boolean captured;
    public Integer attaque;
    public Integer defense;
    public Integer vitesse;
    public Float tableauCoordinates[];


    public String pokedex_id;
    public String profile_id;

    public Pokemon() {

    }

    public Pokemon(String nickname, Integer level, Integer xp, Boolean captured, Integer attaque, Integer defense, Integer vitesse, Float[] tableauCoordinates, String pokedex_id, String profile_id) {
        this.nickname = nickname;
        this.level = level;
        this.xp = xp;
        this.captured = captured;
        this.attaque = attaque;
        this.defense = defense;
        this.vitesse = vitesse;
        this.tableauCoordinates = tableauCoordinates;
        this.pokedex_id = pokedex_id;
        this.profile_id = profile_id;
    }
}
