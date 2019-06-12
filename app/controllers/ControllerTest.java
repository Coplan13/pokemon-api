package controllers;

import dao.ProfileDAO;
import models.Profile;
import play.mvc.Controller;
import play.mvc.Result;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;

public class ControllerTest extends Controller {

    private final ProfileDAO profileDAO;

    @Inject
    public ControllerTest(PlayJongo playJongo) {
        this.profileDAO = new ProfileDAO(playJongo);
    }

    public Result test() {

        Profile profile = new Profile();
        profile.email = "heissler.jerome";
        profileDAO.insert(profile);

        return ok("c'est ok");
    }

}
