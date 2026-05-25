package clinicaSalud.ms_auth.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Dto.LoginResponse;
import clinicaSalud.ms_auth.Service.AuthService;
import lombok.extern.slf4j.Slf4j; 

@Slf4j // demosle color
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Log nivel INFO para saber quién está intentando entrar
        log.info("Recibiendo petición de login para el usuario: {}", request.getUsername());
        
        try {
            String tokenGenerado = authService.login(request);
            
            LoginResponse response = LoginResponse.builder()
                    .token(tokenGenerado)
                    .username(request.getUsername())
                    .build();
                    
            // Log de éxito
            log.info("Login exitoso. Token generado para el usuario: {}", request.getUsername());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            // Log nivel ERROR para registrar la falla de seguridad
            log.error("Fallo en el intento de login: {}", e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}