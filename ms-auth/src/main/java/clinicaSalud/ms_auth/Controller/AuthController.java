package clinicaSalud.ms_auth.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Dto.LoginResponse; // <-- Importamos nuestra nueva cajita
import clinicaSalud.ms_auth.Service.AuthService;

@RestController
@RequestMapping("/api/v1/auth") // que no se me olvideeee
public class AuthController {

    @Autowired
    private AuthService authService;

    // Cambiamos ResponseEntity<String> por ResponseEntity<?> para poder devolver el DTO o el String de error
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Le pedimos al Service que haga la pega
            String tokenGenerado = authService.login(request);
            
            // Armamos la respuesta elegante con el patrón Builder
            LoginResponse response = LoginResponse.builder()
                    .token(tokenGenerado)
                    .username(request.getUsername())
                    .build();
                    
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            // devolvemos 401 si la clave esta mala
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}