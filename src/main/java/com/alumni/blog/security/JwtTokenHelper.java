package com.alumni.blog.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {

    public static final long JWT_TOKEN_VALIDITY = 86400L;

    private static final String SECRET_KEY = "aVeryStrongSecretKeyThatIsAtLeast32BytesLong!";
    private static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // Generate a key for HS256

//
//    public JwtTokenHelper() {
//        // Initialize the key using the secret string
//        this.key = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), SignatureAlgorithm.HS512.getJcaName());
//    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)  // Use the generated key for validation
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate token using the user details
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)  // Sign with the generated key
                .compact();
    }

    // Validate the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
