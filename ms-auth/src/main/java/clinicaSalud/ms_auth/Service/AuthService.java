package clinicaSalud.ms_auth.Service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Model.Usuario;
import clinicaSalud.ms_auth.Repository.UsuarioRepository;
import clinicaSalud.ms_auth.Security.JwtUtil;
import lombok.extern.slf4j.Slf4j; 

@Slf4j 
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(AuthRequest request) {
        log.info("Consultando credenciales para usuario: {}", request.getUsername());
        
        Usuario usuario = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Alerta: Intento de login con usuario inexistente ({})", request.getUsername());
                    return new IllegalArgumentException("Error: Credenciales incorrectas.");
                });

        if (!usuario.getPassword().equals(request.getPassword())) {
            log.warn("Alerta: Contraseña incorrecta para el usuario ({})", request.getUsername());
            throw new IllegalArgumentException("Error: Credenciales incorrectas.");
        }

        log.info("Credenciales validadas. Generando JWT...");
        return jwtUtil.generateToken(usuario.getUsername());
    }

    public String registrar(AuthRequest request) {
        log.info("Intentando registrar nuevo usuario: {}", request.getUsername());
        
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("El usuario {} ya existe", request.getUsername());
            throw new IllegalArgumentException("Error: El nombre de usuario ya está ocupado.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setPassword(request.getPassword());
        nuevoUsuario.setRol("PACIENTE"); // Rol por defecto
        
        repository.save(nuevoUsuario);
        log.info("Usuario {} registrado con éxito", request.getUsername());
        
        return "Usuario registrado correctamente.";
    }
}