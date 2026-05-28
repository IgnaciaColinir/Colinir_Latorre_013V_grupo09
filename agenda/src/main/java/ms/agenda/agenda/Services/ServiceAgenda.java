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

    public List<ModelAgenda> obtenerTodas() {
        try {
            return agendaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener citas: " + e.getMessage());
        }
    }

    public List<ModelAgenda> obtenerPorId(int id) {
        try {
            return agendaRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cita: " + e.getMessage());
        }
    }

public AgendaResponseDTO guardarCita(AgendaRequestDTO request) {
    
// 1. PRIMER PASO: LLAMADA OPENFEIGN CON CAPTURA DE ERROR EXPLÍCITA
        PacienteResponse pacientes;
        try {
            pacientes = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());
        } catch (Exception e) {
            // Si OpenFeign falla (401, 404, etc.), este throw corta el método e informa a Postman
            throw new IllegalArgumentException("ERROR REAL DE FEIGN: " + e.getMessage());
        }

        if (pacientes == null) {
            throw new IllegalArgumentException("El paciente no está autorizado para agendar o no existe.");
        }

        // 2. EXTRACCIÓN Y VALIDACIÓN DE FECHA PASADA
        LocalDate fechaCita = request.getFecha(); 
        LocalTime horaCita = request.getHora();   

        LocalDateTime fechaHoraCita = LocalDateTime.of(fechaCita, horaCita);        
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede agendar una cita en una fecha u hora pasada.");
        }

        // 3. CONSULTA DE DISPONIBILIDAD EN EL REPOSITORIO
        List<ModelAgenda> citasdelDia = agendaRepository.findByProfesionalAndFecha(
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

            // 7. RESPUESTA MAPEADA AL DTO DE SALIDA
            return AgendaResponseDTO.builder()
                    .id(guardado.getId())
                    .fecha(guardado.getFecha())
                    .hora(guardado.getHora())
                    .idProfesional(guardado.getIdProfesional())
                    .idPaciente(guardado.getIdPaciente())
                    .estado(guardado.getEstado())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error en la base de datos de agenda al guardar: " + e.getMessage());
        }

}
    
    public ModelAgenda actualizar(int id, ModelAgenda citaActualizada) {
        try {
            return agendaRepository.update(id, citaActualizada);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar cita: " + e.getMessage());
        }
    }

    public boolean eliminar(int id) {
        try {
            return agendaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar cita: " + e.getMessage());
        }
    }

    public List<ModelAgenda> obtenerPorPaciente(String idPaciente) {
        try {
            return agendaRepository.findByIdPaciente(idPaciente);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por paciente: " + e.getMessage());
        }
    }

    public List<ModelAgenda> obtenerCitasDisponibles(String idProfesional, LocalDate fecha) {
        try {
            return agendaRepository.findByProfesionalAndFecha(idProfesional, fecha);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener citas disponibles: " + e.getMessage());
        }
    }
}