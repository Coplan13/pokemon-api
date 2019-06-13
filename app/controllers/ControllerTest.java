package controllers;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.*;
import models.*;
import play.api.Play;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;


import scala.util.parsing.json.JSON;
import scala.util.parsing.json.JSONObject;
import token.TokenManager;
import uk.co.panaxiom.playjongo.PlayJongo;


import javax.inject.Inject;
import java.awt.*;

public class ControllerTest extends Controller {

    private final ProfileDAO profileDAO;

    @Inject
    public ControllerTest(PlayJongo playJongo) {
        this.profileDAO = new ProfileDAO(playJongo);
    }

    public Result map(Http.Request request)
    {
       String tok =  request.getHeaders().get("Beamer").toString();

        return ok(tok);
    }


    public Result login(Http.Request request) {

        JsonNode json = request.body().asJson();

        String email = json.findPath("email").toString();


        Profile profile = this.profileDAO.findByEmail(email);
        if( profile != null)
        {
            TokenManager tokenManager = new TokenManager();

            String token = tokenManager.generateToken(email);

            ObjectNode profil = Json.newObject();
            profil.put("_id",profile._id);
            profil.put("username",profile.login);
            profil.put("picture",profile.picture);
            profil.put("email",profile.email);
            profil.put("candy",0);

            ObjectNode result = Json.newObject();

            result.put("profil",profil);


            result.put("token", token);


            return ok(result);
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
