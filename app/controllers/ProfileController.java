package controllers;

import dao.ProfileDAO;
import play.mvc.Controller;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;

public class ProfileController extends Controller {

    private final ProfileDAO profileDAO;

    @Inject
    public ProfileController(PlayJongo playJongo) {
        this.profileDAO = new ProfileDAO(playJongo);
    }
}
