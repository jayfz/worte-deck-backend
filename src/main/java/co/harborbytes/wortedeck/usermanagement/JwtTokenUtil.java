package co.harborbytes.wortedeck.usermanagement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private final Long expiration;
    private final SecretKey key;

    public JwtTokenUtil(@Value("${jwt.secret}") final String secret, @Value("${jwt.expiration}") final Long expiration) {
        this.expiration = expiration;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
    }

    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String createToken(final Map<String, Object> extraClaims, final String subject) {
        final Instant now = Instant.now();
        final Date issuedAt = Date.from(now);
        final Date expirationDate = Date.from(now.plus(expiration, ChronoUnit.HOURS));

        return Jwts.builder()
                .claims(extraClaims)
                .issuer("Worte Deck Inc.")
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(key, SignatureAlgorithm.ES512)
                .compact();
    }


    public String getUsernameFromToken(final String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(final String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    private boolean isTokenExpired(final String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(Date.from(Instant.now()));
    }

    private Claims getClaimsFromToken(final String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
