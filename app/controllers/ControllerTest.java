package controllers;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.*;
import models.*;
import play.api.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import token.TokenManager;
import uk.co.panaxiom.playjongo.PlayJongo;


import javax.inject.Inject;

public class ControllerTest extends Controller {

    private final ProfileDAO profileDAO;

    @Inject
    public ControllerTest(PlayJongo playJongo) {
        this.profileDAO = new ProfileDAO(playJongo);
    }



    public Result login(Http.Request request) {

        JsonNode json = request.body().asJson();

        String email = json.findPath("email").toString();


        Profile profile = this.profileDAO.findByEmail(email);
        if( profile != null)
        {
            TokenManager tokenManager = new TokenManager();

            String token = tokenManager.generateToken(email);

            return ok("Le token : "+token);
        }
        else
        {
            return notFound("Not found, l'email n'existe pas");
        }

      //  DynamicForm requestData = Form.form().bindFromRequest();
      //  String email = requestData.get("email");



      //  System.out.println("BODY:::"+json.toString());

        /*
        if( profileDAO.findByEmail("bouh2") == null)
        {
            return notFound("Pas d'email");
        }
*/



    }

    public Result test() {

        Profile profile = new Profile();
        profile.email = "heissler.jerome";
        profile.password = "blabla";
        profile.login = "zite";
        profileDAO.insert(profile);

        return ok("c'est ok");
    }

}
