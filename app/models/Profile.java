package models;

import org.jongo.marshall.jackson.oid.MongoObjectId;



public class Profile {

    @MongoObjectId
    public String _id;

    public String login;
    public String email;
    public String password;
    public String picture;

    public Profile() {

    }

    public Profile(  String login, String email, String password, String picture){
        this.email = email;
        this.login = login;
        this.password = password;
        this.picture = picture;
    }

}
