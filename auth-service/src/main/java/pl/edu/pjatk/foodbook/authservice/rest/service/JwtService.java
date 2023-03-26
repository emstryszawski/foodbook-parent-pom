package pl.edu.pjatk.foodbook.authservice.rest.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.NewRequestUser;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String PUBLIC_KEY;

    private final int TOKEN_EXPIRATION_TIME;

    @Autowired
    public JwtService(
        @Value("${token.key}") String publicKey,
        @Value("${token.expiration_time}") int tokenExpirationTime) {
        PUBLIC_KEY = publicKey;
        TOKEN_EXPIRATION_TIME = tokenExpirationTime;
    }

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    public String generateToken(NewRequestUser requestUser) {
        return generateToken(new HashMap<>(), requestUser);
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, NewRequestUser requestUser) {
        return Jwts.builder()
                   .setClaims(extraClaims)
                   .setSubject(requestUser.getUsername())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                   .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSignInKey())
                   .build()
                   .parseClaimsJws(jwtToken)
                   .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(PUBLIC_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
