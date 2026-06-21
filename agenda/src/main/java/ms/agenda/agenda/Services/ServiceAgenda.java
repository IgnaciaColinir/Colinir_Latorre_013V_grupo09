package ms.agenda.agenda.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.agenda.agenda.Model.ModelAgenda;
import ms.agenda.agenda.Repository.RepositoryAgenda;
import ms.agenda.agenda.client.PacienteClient;
import ms.agenda.agenda.client.ProfesionalClient;
import ms.agenda.agenda.dto.request.AgendaRequestDTO;
import ms.agenda.agenda.dto.response.AgendaResponseDTO;
import ms.agenda.agenda.dto.response.PacienteResponse;
import ms.agenda.agenda.dto.response.ProfesionalResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service // Marca esta clase como capa de lógica de negocio
public class ServiceAgenda {

    @Autowired
    private RepositoryAgenda agendaRepository;

    @Autowired
    private PacienteClient pacienteClient;

    @Autowired
    private ProfesionalClient profesionalClient;

    private AgendaResponseDTO convertirADto(ModelAgenda agenda) {
        return AgendaResponseDTO.builder()
                .id(agenda.getId())
                .fecha(agenda.getFecha())
                .hora(agenda.getHora())
                .idProfesional(agenda.getIdProfesional())
                .idPaciente(agenda.getIdPaciente())
                .estado(agenda.getEstado())
                .build();
    }

    // 💡 CORREGIDO: Ahora retorna una lista de DTOs
    public List<AgendaResponseDTO> obtenerTodas() {
        try {
            List<ModelAgenda> citas = agendaRepository.findAll();
            return citas.stream().map(this::convertirADto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener citas: " + e.getMessage());
        }
    }

    // 💡 CORREGIDO: Retorna un único DTO y maneja la excepción si el id no existe
    public AgendaResponseDTO obtenerPorId(int id) {
        try {
            ModelAgenda cita = agendaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cita con ID " + id + " no encontrada"));
            return convertirADto(cita);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cita: " + e.getMessage());
        }
    }

    public AgendaResponseDTO guardarCita(AgendaRequestDTO request) {
        // 1. LLAMADA OPENFEIGN CON CAPTURA DE ERROR EXPLÍCITA
        PacienteResponse pacientes;
        try {
            pacientes = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());
        } catch (Exception e) {
            throw new IllegalArgumentException("ERROR REAL DE FEIGN: " + e.getMessage());
        }

        if (pacientes == null) {
            throw new IllegalArgumentException("El paciente no está autorizado para agendar o no existe.");
        }

        try {
        // Suponiendo que tu ProfesionalClient tiene un método similar para buscar por RUT o ID
        ProfesionalResponse profesional = profesionalClient.obtenerProfesionalPorRut(request.getIdProfesional());
        if (profesional == null) {
            throw new IllegalArgumentException("El profesional médico no existe en el sistema.");
        }
        } catch (Exception e) {
            throw new IllegalArgumentException("ERROR CRÍTICO EN MICROSERVICIO USUARIOS/PROFESIONALES: " + e.getMessage());
        }

        // 2. EXTRACCIÓN Y VALIDACIÓN DE FECHA PASADA
        LocalDate fechaCita = request.getFecha(); 
        LocalTime horaCita = request.getHora();   

        LocalDateTime fechaHoraCita = LocalDateTime.of(fechaCita, horaCita);        
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede agendar una cita en una fecha u hora pasada.");
        }

        // 3. CONSULTA DE DISPONIBILIDAD EN EL REPOSITORIO
        List<ModelAgenda> citasdelDia = agendaRepository.findByIdProfesionalAndFecha(
                request.getIdProfesional(),
                fechaCita 
        );

        // 4. COMPROBAR SI EL HORARIO ESTÁ OCUPADO
        boolean estaOcupado = citasdelDia.stream()
                .anyMatch(cita -> cita.getHora().equals(horaCita));

        if (estaOcupado) {
            throw new IllegalArgumentException("El profesional ya tiene una cita reservada a esa hora");
        }

        // 5. CONSTRUCCIÓN DE LA NUEVA ENTIDAD
        ModelAgenda citaNueva = ModelAgenda.builder()
                .id(request.getId())
                .fecha(request.getFecha()) 
                .hora(request.getHora())   
                .idProfesional(request.getIdProfesional())
                .idPaciente(request.getIdPaciente())
                .estado(request.getEstado() != null ? request.getEstado() : "RESERVADA")
                .build();

        // 6. PERSISTENCIA EN BASE DE DATOS
        try {
            ModelAgenda guardado = agendaRepository.save(citaNueva);
            return convertirADto(guardado);
        } catch (Exception e) {
            throw new RuntimeException("Error en la base de datos de agenda al guardar: " + e.getMessage());
        }
    }
    
    // 💡 CORREGIDO: Se reescribió para usar la persistencia nativa de JPA (.save)
    public AgendaResponseDTO actualizar(int id, AgendaRequestDTO request) {
        try {
            ModelAgenda citaExistente = agendaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cita con ID " + id + " no encontrada"));

            // Actualizamos los campos pertinentes de la entidad con los datos del RequestDTO
            citaExistente.setFecha(request.getFecha());
            citaExistente.setHora(request.getHora());
            citaExistente.setIdProfesional(request.getIdProfesional());
            citaExistente.setIdPaciente(request.getIdPaciente());
            if (request.getEstado() != null) {
                citaExistente.setEstado(request.getEstado());
            }

            ModelAgenda citaActualizada = agendaRepository.save(citaExistente);
            return convertirADto(citaActualizada);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cita: " + e.getMessage());
        }
    }

    // 💡 CORREGIDO: Comprobación con .existsById() para poder retornar un booleano real
    public boolean eliminar(int id) {
        try {
            if (agendaRepository.existsById(id)) {
                agendaRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar cita: " + e.getMessage());
        }
    }

    // 💡 CORREGIDO: Retorna una lista mapeada a DTOs
    public List<AgendaResponseDTO> obtenerPorPaciente(String idPaciente) {
        try {
            List<ModelAgenda> citas = agendaRepository.findByIdPaciente(idPaciente);
            return citas.stream().map(this::convertirADto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por paciente: " + e.getMessage());
        }
    }

    // 💡 CORREGIDO: Retorna una lista mapeada a DTOs
    public List<AgendaResponseDTO> obtenerCitasDisponibles(String idProfesional, LocalDate fecha) {
        try {
            List<ModelAgenda> citas = agendaRepository.findByIdProfesionalAndFecha(idProfesional, fecha);
            return citas.stream().map(this::convertirADto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener citas disponibles: " + e.getMessage());
        }
    }
}