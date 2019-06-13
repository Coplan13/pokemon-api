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
        if( profile != null)
        {
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


        Profile profile = this.profileDAO.findByEmail(email);
        if( profile != null)
        {
            return forbidden();
        }
        profile = profileDAO.findByUsername(username);
        if( profile != null)
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
