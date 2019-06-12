package models;

public class Profile_Pokestop {

    public Float timer;
    public String profile_id;
    public String pokestop_id;

    public Profile_Pokestop() {

    }

    public Profile_Pokestop(Float timer, String profile_id, String pokestop_id) {
        this.timer = timer;
        this.profile_id = profile_id;
        this.pokestop_id = pokestop_id;
    }
}
