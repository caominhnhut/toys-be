package com.momo.toys.be.factory;

import java.util.Date;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class TokenHelper{

    @Value("${jwt.expiresIn}")
    private long expiresIn;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, HttpServletRequest request){
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (SignatureException e) {
            request.setAttribute("SignatureException", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            request.setAttribute("MalformedJwtException", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            request.setAttribute("UnsupportedJwtException", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            request.setAttribute("IllegalArgumentException", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            request.setAttribute("ExpiredJwtException", e.getMessage());
            return false;
        }
    }

    /**
     * while creating the token -
     * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     * compaction of the JWT to a URL-safe string
     */
    public String generateToken(String username){
        return Jwts.builder().setIssuer(issuer).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expiresIn * 1000)).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public long getExpiresIn(){
        return expiresIn;
    }
}
