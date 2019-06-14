import models.*;
import uk.co.panaxiom.playjongo.PlayJongo;

/**
 * Created by jeromeheissler on 02/03/2017.
 */
public class DatabaseSetup {

    private PlayJongo jongo;

    public DatabaseSetup(PlayJongo jongo) {
        this.jongo = jongo;
    }


    public String createUser(String username, String email, String password){
        return createUser(username, email, password, 0);
    }
    public String createUser(String username, String email, String password, int candy){
        Profile newUser = new Profile( username , email, password, "");
        jongo.getCollection("profile").insert(newUser);
        return newUser._id;
    }

    public String createItem(String pokoball) {
        Item newItem = new Item(pokoball);
        jongo.getCollection("item").insert(newItem);
        return newItem._id;
    }

    public void associateUserAndItem(String userId, String itemId) {
        
    }

    public String createStop(String name, double[] coordinates) {
        Pokestop newPokestop = new Pokestop(name,"", 0.0, coordinates, "");
        jongo.getCollection("pokestop").insert(newPokestop);
        return newPokestop._id;
    }

    public String createPokomon(String name, int number) {
        Pokemon newPokemon = new Pokemon(name, number);
        jongo.getCollection("pokemon").insert(newPokemon);
        return newPokemon._id;
    }

    public String createPokomon(String name, int number, int candyRequired, int pokomonNumber) {
        Pokemon newPokemon = new Pokemon(name, number);
        jongo.getCollection("pokemon").insert(newPokemon);
        return newPokemon._id ;
    }

    public int getPokomonNumber(String id) {
        return -1;
    }

    public String associatePokomonToUser(String pokomonId, String userId) {
        return "";
    }

    public void markPokomonAsSeenBy(String userId, String pokomonId) {
        
    }

    public String createPokomonToCatch(String pokomonId) {
        return createPokomonToCatch(pokomonId, new double[]{0.0, 0.0});
    }
    public String createPokomonToCatch(String pokomonId, double[] position) {
        return "";
    }

}
