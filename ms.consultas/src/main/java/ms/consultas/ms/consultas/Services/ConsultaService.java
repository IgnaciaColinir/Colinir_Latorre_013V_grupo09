package ms.consultas.ms.consultas.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.consultas.ms.consultas.Modelo.ModeloConsulta;
import ms.consultas.ms.consultas.Repository.ConsultaRepository;
import ms.consultas.ms.consultas.client.PacienteClient;
import ms.consultas.ms.consultas.client.ProfesionalClient;
import ms.consultas.ms.consultas.dto.request.ConsultasRequestDTO;
import ms.consultas.ms.consultas.dto.response.ConsultasResponseDTO;
import ms.consultas.ms.consultas.dto.response.PacienteResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service // Marca esta clase como capa de lógica de negocio
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired 
    private PacienteClient pacienteClient;
    @Autowired
    private ProfesionalClient profesionalClient;

    public List<ModeloConsulta> obtenerTodos() {
        try {
            return consultaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener consultas: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> obtenerPorId(int id) {
        try {
            return consultaRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar consulta: " + e.getMessage());
        }
    }

public ConsultasResponseDTO guardarConsulta(ConsultasRequestDTO request) {
    try {
        // 1. No pueden haber consultas con fechas futuras
        LocalDate fecha = request.getFechaConsulta();
        LocalTime hora = request.getHoraConsulta();

        if (LocalDateTime.of(fecha, hora).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("La fecha y hora de la consulta no pueden ser futuras.");
        }

        // 2. Sin valores negativos
        if (request.getValorConsulta() < 0 || request.getValorTratamiento() < 0) {
            throw new RuntimeException("Los valores de la consulta o tratamiento no pueden ser negativos.");
        }

        // Verificar que el paciente exista y capturar su nombre
        String nombrePacienteCompleto;
        PacienteResponse paciente = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());

        if (paciente == null) {
            throw new RuntimeException("El paciente no existe");
        }

        nombrePacienteCompleto = paciente.getNombre() + " " + paciente.getApellido();

        ModeloConsulta consulta = ModeloConsulta.builder()
                .nomPaciente(nombrePacienteCompleto) // Guardado histórico inmutable
                .nomMedico(request.getNomMedico())
                .fechaConsulta(fecha) // Pasamos el objeto LocalDate
                .horaConsulta(hora)   // Pasamos el objeto LocalTime
                .diagnostico(request.getDiagnostico())
                .valorConsulta(request.getValorConsulta())
                .valorTratamiento(request.getValorTratamiento())
                .build();

        // 5. PERSISTENCIA
        ModeloConsulta guardado = consultaRepository.save(consulta);

        // 6. RETORNO: Mapeo hacia el ResponseDTO
        return ConsultasResponseDTO.builder()
                .id(guardado.getId())
                .nomPaciente(guardado.getNomPaciente())
                .nomMedico(guardado.getNomMedico())
                .fechaConsulta(guardado.getFechaConsulta())
                .horaConsulta(guardado.getHoraConsulta())
                .diagnostico(guardado.getDiagnostico())
                .valorConsulta(guardado.getValorConsulta())
                .valorTratamiento(guardado.getValorTratamiento())
                .build();

    } catch (RuntimeException e) {
        // Deja pasar directo tus excepciones manuales de negocio (fecha futura, paciente no existe, etc)
        throw e;
    } catch (Exception e) {
        // Atrapa errores de red de Feign, base de datos caída o fallas técnicas del servidor
        throw new RuntimeException("Error fatal e inesperado al registrar la consulta: " + e.getMessage());
    }
}    
    public ModeloConsulta actualizar(int id, ModeloConsulta consultaActualizada) {
        try {
            return consultaRepository.update(id, consultaActualizada);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar consulta: " + e.getMessage());
        }
    }

    public boolean eliminarbyId(int id) {
        try {
            return consultaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar consulta: " + e.getMessage());
        }
    }

    public boolean eliminarbyPaciente(String paciente) {
        try {
            return consultaRepository.deleteByPaciente(paciente);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar consulta: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> buscarPorPaciente(String nomPaciente) {
        try {
            return consultaRepository.findByPaciente(nomPaciente);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por paciente: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> buscarPorFecha(String fechaConsulta) {
        try {
            return consultaRepository.findByDate(fechaConsulta);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por fecha: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> buscarPorDiagnostico(String diagnostico) {
        try {
            return consultaRepository.findByDiagnostico(diagnostico);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por diagnostico: " + e.getMessage());
        }
    }

    
}