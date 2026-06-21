package ms.agenda.agenda.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ms.agenda.agenda.Model.ModelAgenda;


@Repository // Marca esta clase como repositorio (acceso a datos)
public interface RepositoryAgenda extends JpaRepository<ModelAgenda, Integer>{
    
    List<ModelAgenda> findByIdProfesionalAndFecha(String idProfesional, LocalDate fecha);
    
    List<ModelAgenda> findByIdPaciente(String idPaciente);

}
