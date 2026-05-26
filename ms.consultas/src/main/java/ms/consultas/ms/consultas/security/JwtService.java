package ms.consultas.ms.consultas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    private static final String SECRET_KEY = "MiClaveSuperSecretaParaLaClinicaDelDuocQueTieneQueSerLargaParaQueSeaSegura123456789";

    

    private SecretKey getKey() {
        
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    
    public Claims obtenerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        
    }

    public String obtenerUsername(String token) {
        try {
            Claims claims = obtenerTodosLosClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean tokenValido(String token) {
        try {
            Claims claims = obtenerTodosLosClaims(token);
            
            java.util.Date expiration = claims.getExpiration();
            
            if (expiration != null && expiration.before(new java.util.Date())) {
                return false; // El token expiró
            }
            
            return claims.getSubject() != null;
        } catch (Exception e) {
            return false; // El token es inválido, alterado o no se puede parsear
        }
    }
}