package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.PokestopDAO;
import models.Pokestop;
import play.mvc.Http;
import play.mvc.Result;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;

import static play.mvc.Results.*;

public class PokestopController {

    private final PokestopDAO pokestopDAO;

    @Inject
    public PokestopController(PlayJongo playJongo) {
        this.pokestopDAO = new PokestopDAO(playJongo);
    }

    public Result takeItemAtPokostopWithoutAuthentication(Http.Request request, String id) {

        JsonNode json = request.body().asJson();

        Pokestop pokestop = this.pokestopDAO.findById(id);


        if (true) {
            return unauthorized();

        }

        return null;
    }

}
