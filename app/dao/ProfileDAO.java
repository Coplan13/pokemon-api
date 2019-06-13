package dao;

import models.Profile;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;


public class ProfileDAO extends ModelDAO<Profile, String>  {

    private MongoCollection profile;

    @Inject
    public ProfileDAO(PlayJongo jongo) {
        super(jongo);

        profile = jongo.getCollection("profile");
    }


    public Profile findByEmail(String email) {
        return profile.findOne(" { email: # } ", email).as(Profile.class);
    }

    public Profile findByUsername(String username) {
        return (profile.findOne(" { username : #} ", username).as(Profile.class));
    }

    @Override
    public Profile findById(String id) {

        return profile.findOne(new ObjectId(id)).as(Profile.class);
    }

    @Override
    public void insert(Profile model) {
        profile.insert(model);
    }

    @Override
    public void update(Profile model) {

    }

    @Override
    public void remove(Profile model) {

    }


}
