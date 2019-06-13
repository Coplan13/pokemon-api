package token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.typesafe.config.Config;
import dao.ProfileDAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import models.Profile;
import play.mvc.Http;

import javax.inject.Inject;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

public class TokenManager {

    private static String token;
    private static Key key = MacProvider.generateKey();


    @Inject
    private Config config;

    @Inject
    private ProfileDAO profileDAO;

    public String getToken(){

        if(token != "")
        {
            return token;
        }
        else
        {
            return "";
        }


    }

    public String generateToken(String userId)
    {

        Date dateNow = new Date();
        Date expires = new Date(dateNow.getTime() + 86400000);

        token = JWT.create()
                .withIssuer("auth0")
                .withClaim("_id", userId)
                .withExpiresAt(expires)
                .sign(Algorithm.HMAC256(config.getString("play.http.secret.key")));

        return token;
    }

    public Optional<Profile> getUser(Http.Request request)
    {
        return request.header("Authorization").flatMap(authorization -> {
            String token = authorization.replace("Bearer ", "");
            try {
                DecodedJWT jwt = JWT.decode(token);
                String userId = jwt.getClaim("_id").asString();
                return Optional.ofNullable(profileDAO.findById(userId));
            } catch (JWTDecodeException exception){
                return Optional.empty();
            }
        });
    }





}
