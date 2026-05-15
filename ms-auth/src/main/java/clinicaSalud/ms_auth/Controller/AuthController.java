package clinicaSalud.ms_auth.Controller;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        
        // Simulamos la validación en base de datos.
        // Si el usuario es admin y la clave 1234, lo dejamos pasar.
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            
            // Le pedimos a la fábrica que genere el token
            String token = jwtUtil.generateToken(request.getUsername());
            
            // Devolvemos el token con un código HTTP 200 (OK)
            return ResponseEntity.ok(token);
        }
        
        // Si se equivoca, le tiramos un error HTTP 401 (No autorizado)
        return ResponseEntity.status(401).body("Credenciales incorrectas, rey.");
    }
}