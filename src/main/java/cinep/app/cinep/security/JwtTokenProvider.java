package cinep.app.cinep.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JwtTokenProvider {

    private JwtConfig jwtConfig;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        return doGenerateToken(new HashMap<>(), userDetails.getUsername());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return getUsernameFromToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String createTokenForTests(String username) {
        return doGenerateToken(new HashMap<>(), username);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long currentTimeMs = System.currentTimeMillis();
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(currentTimeMs))
                .setExpiration(new Date(currentTimeMs + jwtConfig.getDurationMs()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();
    }

}
