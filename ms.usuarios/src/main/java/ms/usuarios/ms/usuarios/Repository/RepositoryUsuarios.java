package ms.usuarios.ms.usuarios.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ms.usuarios.ms.usuarios.Model.ModeloUsuario;


public interface RepositoryUsuarios extends JpaRepository<ModeloUsuario, String> {
    Optional<ModeloUsuario> findByEmail(String email);
    List<ModeloUsuario> findByCargo(String cargo);
} 
    

  

