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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j; 

@Slf4j 
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Controlador para iniciar sesión y registrar usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y devuelve un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        log.info("Recibiendo petición de login para: {}", request.getUsername());
        try {
            String tokenGenerado = authService.login(request);
            LoginResponse response = LoginResponse.builder()
                    .token(tokenGenerado)
                    .username(request.getUsername())
                    .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Fallo de login: {}", e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @Operation(summary = "Registrar", description = "Crea un nuevo usuario en la BD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Registrado correctamente", content = @Content),
        @ApiResponse(responseCode = "400", description = "Usuario ya existe", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody AuthRequest request) {
        try {
            String respuesta = authService.registrar(request);
            return ResponseEntity.status(201).body(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}