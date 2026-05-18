package clinicaSalud.ms_auth.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinicaSalud.ms_auth.Model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Magia de Spring: Busca al usuario por su nombre automáticamente
    Optional<Usuario> findByUsername(String username); 
}