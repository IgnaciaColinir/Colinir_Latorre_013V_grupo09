package clinicaSalud.ms_auth.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Dto.LoginResponse;
import clinicaSalud.ms_auth.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j; 

@Slf4j 
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para iniciar sesión y registrar usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y devuelve un token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Recibiendo petición de login para: {}", request.getUsername());
        String tokenGenerado = authService.login(request);
        
        LoginResponse response = LoginResponse.builder()
                .token(tokenGenerado)
                .username(request.getUsername())
                .build();
                
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar", description = "Crea un nuevo usuario en la base de datos")
    @PostMapping("/register")
    public ResponseEntity<String> registrar(@Valid @RequestBody AuthRequest request) {
        String respuesta = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}