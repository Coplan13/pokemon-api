package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import token.TokenManager;

import javax.inject.Inject;

public class MapController extends Controller {


    @Inject
    private TokenManager tokenManager;


    public Result map(Http.Request request)
    {

        return tokenManager.getUser(request).map(user -> {


            return ok(user.email);

        }).orElseGet(Results::unauthorized);
    }

}
