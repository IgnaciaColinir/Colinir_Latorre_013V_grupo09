package clinicaSalud.ms_auth.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Trae el secreto y el tiempo de expiración desde application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Método maestro que fabrica el Token
    public String generateToken(String username) {
        // Encripta nuestra clave secreta
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(username) // A quién le pertenece el token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación (hoy)
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de muerte (en 1 hora)
                .signWith(key, SignatureAlgorithm.HS256) // Firma de seguridad
                .compact(); // Arma el string final
    }
}