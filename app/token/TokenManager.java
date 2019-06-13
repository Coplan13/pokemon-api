package token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;

public class TokenManager {

    private static String token;
    private static Key key;

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

    public String generateToken(String email)
    {

        Date dateNow = new Date();
        Date expires = new Date(dateNow.getTime() + 86400000);

        key = MacProvider.generateKey();

        token = Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS256, key).setExpiration(expires).compact();

        return token;

    }

    public String getEmail(String token)
    {
        String email = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();

        return email;
    }





}
