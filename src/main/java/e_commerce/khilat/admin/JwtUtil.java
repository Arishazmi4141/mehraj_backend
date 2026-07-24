package e_commerce.khilat.admin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
	
	private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; 
	
	private final SecretKey ALGO_KEY;

    
    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        this.ALGO_KEY = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(ALGO_KEY) // Now it can resolve this variable
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(ALGO_KEY) // Now it can resolve this variable
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractEmail(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}