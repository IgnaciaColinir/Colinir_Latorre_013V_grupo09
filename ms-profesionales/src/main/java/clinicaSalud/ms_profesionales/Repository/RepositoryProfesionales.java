package clinicaSalud.ms_profesionales.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import clinicaSalud.ms_profesionales.Model.ModeloProfesional;

@Repository
public interface RepositoryProfesionales extends ListCrudRepository<ModeloProfesional, Long> {
    
    List<ModeloProfesional> findByEspecialidad(String especialidad);
    
    Optional<ModeloProfesional> findByRut(String rut);
    
    boolean existsByRut(String rut);
}