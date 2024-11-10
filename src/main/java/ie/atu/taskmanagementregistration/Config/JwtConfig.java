package ie.atu.taskmanagementregistration.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.security.Key;
import java.util.Date;

@Configuration
public class JwtConfig {

    private final Environment environment;

    public JwtConfig(Environment environment) {
        this.environment = environment;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .signWith(signingKey())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }

    private Key signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(environment.getProperty("jwt.secret"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
