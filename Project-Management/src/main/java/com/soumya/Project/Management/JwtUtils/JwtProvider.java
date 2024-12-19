package com.soumya.Project.Management.JwtUtils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

public class JwtProvider {
    private static final Key KEY = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());

    public static String generateToken(Authentication auth){
        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email",auth.getName())
                .signWith(KEY)
                .compact();
        return token;
    }
    public static String getEmailFromJwt(String token){
        try {
            token = token.substring(7);
            Claims claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
            return String.valueOf(claims.get("email"));
        }
        catch (ExpiredJwtException e){
            throw new JwtException("Token expired");
        }catch (MalformedJwtException e){
            throw new JwtException("Token is malformed");
        }catch (JwtException e){
            throw new JwtException("Invalid token");
        }
    }
}
