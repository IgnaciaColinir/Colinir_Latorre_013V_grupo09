package clinicaSalud.ms_auth.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import clinicaSalud.ms_auth.Model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username); 
}