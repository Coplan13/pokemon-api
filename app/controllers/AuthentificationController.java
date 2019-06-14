package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.*;
import models.*;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import token.TokenManager;


import javax.inject.Inject;

public class AuthentificationController extends Controller {

    @Inject
    private ProfileDAO profileDAO;

    @Inject
    private TokenManager tokenManager;

    public Result login(Http.Request request) {

        JsonNode json = request.body().asJson();

        String email = json.findPath("email").asText();
        System.out.println(email);

        Profile profile = this.profileDAO.findByEmail(email);
        System.out.println(profile);







        if(profile ==null)
        {
            if (!json.findPath("email").asText().equals("") && this.profileDAO.findByEmail(email) == null )
            {
                return notFound();
            }
            if(json.findPath("password").asText().equals("")|| json.findPath("email").asText().equals("")){
                return badRequest();
            }


        }




        if( profile != null)
        {
            if(profile.email.equals("")){
                return notFound();
            }
            if(!profile.password.equals(json.findPath("password").asText())){
                System.out.println(profile.password + json.findPath("password").asText());
                return forbidden();
        }


            String token = tokenManager.generateToken(profile._id);
            ObjectNode profil = Json.newObject();
            profil.put("_id",profile._id);
            profil.put("username",profile.login);
            profil.put("picture",profile.picture);
            profil.put("email",profile.email);
            profil.put("candy",0);

            ObjectNode result = Json.newObject();

            result.put("profile",profil);


            result.put("token", token);


            return ok(result);
        }
        else
        {


            return notFound();
        }


    }

    public Result signup(Http.Request request) {
        JsonNode json = request.body().asJson();

        String email = json.findPath("email").asText();
        String username = json.findPath("username").asText();
        String password = json.findPath("password").asText();

        if(!json.has("email") || !json.has("username") || !json.has("password")) {
            return badRequest();
        }

        Profile profile = this.profileDAO.findByEmail(email);
        if( profile != null)
        {
            return forbidden();
        }
       Profile profile2 = profileDAO.findByUsername(username);
        if( profile2 != null)
        {
            return forbidden();
        }

        profile = new Profile();
        profile.email = email;
        profile.login = username;
        profile.password = password;
        profile.picture = "";

        profileDAO.insert(profile);

        String token = tokenManager.generateToken(profile._id);

        ObjectNode profil = Json.newObject();
        profil.put("_id",profile._id);
        profil.put("username",profile.login);
        profil.put("picture",profile.picture);
        profil.put("email",profile.email);
        profil.put("candy",0);

        ObjectNode result = Json.newObject();

        result.put("profile",profil);


        result.put("token", token);


        return ok(result);
    }

}
