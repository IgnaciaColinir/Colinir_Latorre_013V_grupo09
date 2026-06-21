package ms.consultas.ms.consultas.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ms.consultas.ms.consultas.Modelo.ModeloConsulta;



public interface  ConsultaRepository extends JpaRepository<ModeloConsulta, Integer> {    
    boolean existsByFechaConsultaAndHoraConsultaAndIdMedico(LocalDate fechaConsulta, LocalTime horaConsulta, 
        String idMedico
    );
    List<ModeloConsulta> findByIdPaciente(String idPaciente);
    List<ModeloConsulta> findByDiagnostico(String diagnostico);
    List<ModeloConsulta> findByEstadoIgnoreCase(String estado);
}
