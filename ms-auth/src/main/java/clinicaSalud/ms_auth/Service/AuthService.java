package clinicaSalud.ms_auth.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Model.Usuario;
import clinicaSalud.ms_auth.Repository.UsuarioRepository;
import clinicaSalud.ms_auth.Security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(AuthRequest request) {
        // 1. Buscamos al usuario en la BD real
        Optional<Usuario> usuarioOp = repository.findByUsername(request.getUsername());

        // Regla de Negocio 1: Que el usuario exista
        if (usuarioOp.isEmpty()) {
            throw new RuntimeException("Error: El usuario no existe en la clínica.");
        }

        Usuario usuario = usuarioOp.get();

        // Regla de Negocio 2: Que la clave coincida
        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Error: Credenciales incorrectas.");
        }

        // 3. Si pasó las reglas, la fábrica hace el token
        return jwtUtil.generateToken(usuario.getUsername());
    }
}