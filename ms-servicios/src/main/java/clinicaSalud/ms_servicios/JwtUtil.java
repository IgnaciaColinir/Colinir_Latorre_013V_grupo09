package clinicaSalud.ms_servicios.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    // OJO: Esta misma clave secreta tiene muchos caracteres.. 
    private final String SECRET_KEY = "ClinicaSaludSecretKeyParaJsonWebTokenMuySegura123!";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Método para revisar si el token es válido
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Método para sacar el nombre del usuario que viene adentro del token
    public String extraerUsuario(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}