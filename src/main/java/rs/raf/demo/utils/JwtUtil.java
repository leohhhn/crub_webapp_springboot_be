package rs.raf.demo.utils;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    Dotenv dotenv = Dotenv.load();

    private final String SECRET_KEY = dotenv.get("JWT_SECRET");

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean hasPermission(String jwt, String permission) {
        Claims perm = this.extractAllClaims(jwt);
        return perm.get(permission).equals(1);
    }

    public String generateToken(String username,
                                Integer pc, Integer pr, Integer pu, Integer pd,
                                Integer pmc, Integer pmd, Integer pmstart, Integer pmstop,
                                Integer pmr, Integer pmsearch) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("p_create", pr);
        claims.put("p_read", pc);
        claims.put("p_update", pu);
        claims.put("p_delete", pd);

        claims.put("pm_create", pmc);
        claims.put("pm_destroy", pmd);
        claims.put("pm_start", pmstart);
        claims.put("pm_stop", pmstop);
        claims.put("pm_restart", pmr);
        claims.put("pm_search", pmsearch);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
