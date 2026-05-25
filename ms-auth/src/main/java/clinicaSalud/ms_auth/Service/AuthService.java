package clinicaSalud.ms_auth.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Model.Usuario;
import clinicaSalud.ms_auth.Repository.UsuarioRepository;
import clinicaSalud.ms_auth.Security.JwtUtil;
import lombok.extern.slf4j.Slf4j; 

@Slf4j // Le damos el corte al Service también
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(AuthRequest request) {
        log.info("Consultando credenciales en la base de datos...");
        
        Optional<Usuario> usuarioOp = repository.findByUsername(request.getUsername());

        if (usuarioOp.isEmpty()) {
            // Alerta de seguridad (WARN)
            log.warn("Alerta: Intento de login con un usuario inexistente ({})", request.getUsername());
            throw new RuntimeException("Error: El usuario no existe en la clínica.");
        }

        Usuario usuario = usuarioOp.get();

        if (!usuario.getPassword().equals(request.getPassword())) {
            // Alerta de seguridad (WARN)
            log.warn("Alerta: Contraseña incorrecta para el usuario ({})", request.getUsername());
            throw new RuntimeException("Error: Credenciales incorrectas.");
        }

        log.info("Credenciales validadas correctamente. Fabricando JWT...");
        return jwtUtil.generateToken(usuario.getUsername());
    }
    // Método para registrar un nuevo usuario en la BD de Auth
    public String registrar(AuthRequest request) {
        log.info("Intentando registrar nuevo usuario: {}", request.getUsername());
        
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("El usuario {} ya existe en la base de datos de Auth", request.getUsername());
            throw new RuntimeException("Error: El nombre de usuario ya está ocupado.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setPassword(request.getPassword()); // En la vida real iría encriptada, pero pal Duoc está joya
        
        repository.save(nuevoUsuario);
        log.info("Usuario {} registrado con éxito en ms-auth", request.getUsername());
        
        return "Usuario registrado correctamente en el sistema de seguridad.";
    }
}