package clinicaSalud.ms_profesionales.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinicaSalud.ms_profesionales.Model.ModeloProfesional;

@Repository
public interface RepositoryProfesionales extends JpaRepository<ModeloProfesional, String> {
    
    List<ModeloProfesional> findByEspecialidad(String especialidad);
}