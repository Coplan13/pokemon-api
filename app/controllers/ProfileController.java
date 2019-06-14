package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ProfileDAO;
import models.Profile;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import token.TokenManager;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;

public class ProfileController extends Controller {

    @Inject
    private ProfileDAO profileDAO;
    @Inject
    private TokenManager tokenManager;


    public Result profil(Http.Request request) {



        return tokenManager.getUser(request).map(user -> {

            JsonNode json = request.body().asJson();

            if(!json.has("email") || !json.has("username") || !json.has("picture")) {
                return badRequest();
            }

            String email = json.get("email").asText();
            String username = json.get("username").asText();
            String picture = json.get("picture").asText();

            System.out.println(email+" : "+username);


                boolean emailBoolean = false;
                boolean usernameBoolean = false;
                if(this.profileDAO.findByEmail(email) != null && email != user.email )
                {
                    emailBoolean = true;
                }

                if(this.profileDAO.findByUsername(username) != null && username != user.login)
                {
                    usernameBoolean = true;
                }

                ObjectNode profil = Json.newObject();
                profil.put("emailExist", emailBoolean);
                profil.put("usernameExist", usernameBoolean);




                user.email = email;
                user.login = username;
                user.picture = picture;


                profileDAO.update(user);

                if (usernameBoolean == true || emailBoolean == true )
                {
                    return forbidden(profil);
                }


              //  String token = tokenManager.generateToken(profile._id);

            ObjectNode profil2 = Json.newObject();
            profil2.put("_id", user._id);
            profil2.put("username", user.login);
            profil2.put("picture", user.picture);
            profil2.put("email", user.email);
            profil2.put("candy", 0);
                return ok(profil2);


        }).orElseGet(Results::unauthorized);

    }

    public Result getProfil(Http.Request request) {



        return tokenManager.getUser(request).map(user -> {

            ObjectNode profil2 = Json.newObject();
            profil2.put("_id", user._id);
            profil2.put("username", user.login);
            profil2.put("picture", user.picture);
            profil2.put("email", user.email);
            profil2.put("candy", 0);
            return ok(profil2);
        }).orElseGet(Results::unauthorized);

    }

}
