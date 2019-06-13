import models.*;
import org.bson.types.ObjectId;
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
        return "";
    }

    public void associateUserAndItem(String userId, String itemId) {
        
    }

    public String createStop(String name, double[] coordinates) {
        return "";
    }

    public String createPokomon(String name, int number) {
        return "";
    }

    public String createPokomon(String name, int number, int candyRequired, int pokomonNumber) {
        return "";
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
