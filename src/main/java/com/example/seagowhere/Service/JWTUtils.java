package com.example.seagowhere.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {

    private SecretKey secretKey;
    public static final long EXPIRATION_TIME = 86400000; // 24 hours == 86400000 milliseconds

    // generate a message authentication code (MAC) using the secret key in combination with the SHA-256 hash function.
    public JWTUtils() {
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // takes in the user's details to generate the JWT token with an expiration duration of 24 hours
    public String generateToken(UserDetails userDetails, String firstName, String lastName, String email, String number){
        Map<String, Object> claims = new HashMap<>();

        // Add custom claims for firstName, lastName, and email
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);
        claims.put("email", email);
        claims.put("number", number);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // takes in the claims (aka payload, e.g. expirationTime) and the user's details to generate a refresh token
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // check if the token used is valid
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // returns the username (email) using the passed-in token
    // the token is the payload containing user information (e.g. email, expirationTime)
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    // Extract email from the token
    public String extractEmail(String token) {
        return extractClaims(token, claims -> claims.get("email", String.class));
    }

    // Extract firstName from the token
    public String extractFirstName(String token) {
        return extractClaims(token, claims -> claims.get("firstName", String.class));
    }

    // Extract lastName from the token
    public String extractLastName(String token) {
        return extractClaims(token, claims -> claims.get("lastName", String.class));
    }

    // Extract number from the token
    public String extractNumber(String token) {
        return extractClaims(token, claims -> claims.get("number", String.class));
    }

    // this generic method, represented by <T> returns a generic type as well T
    // returns the claims (payload) from a JWT (JSON Web Token)
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload());
    }

    // returns whether the token is expired by comparing the token's expiration against the current date
    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
