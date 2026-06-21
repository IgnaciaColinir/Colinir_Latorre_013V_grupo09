package ms.paciente.ms.paciente.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ms.paciente.ms.paciente.Model.ModeloPaciente;


public interface RepositoryPacientes extends JpaRepository<ModeloPaciente, String> {
    List<ModeloPaciente> findByPrevision(String prevision);
}
