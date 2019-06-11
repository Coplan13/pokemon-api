package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class ControllerTest extends Controller {

    public Result test() {
        return ok("c'est ok");
    }

}
