package ms.agenda.agenda.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.agenda.agenda.Model.ModelAgenda;
import ms.agenda.agenda.Repository.RepositoryAgenda;
import ms.agenda.agenda.client.PacienteClient;
import ms.agenda.agenda.client.ProfesionalClient;
import ms.agenda.agenda.dto.request.AgendaRequestDTO;
import ms.agenda.agenda.dto.response.AgendaResponseDTO;

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
        //1. Validar que el paciente no tenga otra cita a la misma hora con otro profesional
        List<ModelAgenda> citasdelDia = agendaRepository.findByProfesionalAndFecha(
            request.getIdProfesional(),
            request.getFecha()
        );

        boolean estaOcupado= citasdelDia.stream()
            .anyMatch(cita -> cita.getHora().equals(request.getHora()));
        
        if(estaOcupado){
            throw new RuntimeException("El profesional ya tiene una cita reservada a esa hora");
        }
        //2. Validar que la fecha y hora de la cita no sea en el pasado
        LocalDate fechaCita = LocalDate.parse(request.getFecha()); // Convierte "2026-05-25"
        LocalTime horaCita = LocalTime.parse(request.getHora());   // Convierte "14:30:00"

        LocalDateTime fechaHoraCita = LocalDateTime.of(fechaCita, horaCita);        
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede agendar una cita en una fecha u hora pasada.");
        }
        //3. Validar que el paciente esté activo mediante Feign
        try {
            var paciente = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());
            if (paciente == null ) {
                throw new RuntimeException("El paciente no está autorizado para agendar.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el paciente mediante Feign: " + e.getMessage());
        }


        ModelAgenda citaNueva = ModelAgenda.builder()
                .id(request.getId())
                .fecha(request.getFecha())
                .hora(request.getHora())
                .idProfesional(request.getIdProfesional())
                .idPaciente(request.getIdPaciente())
                .estado(request.getEstado())
                .build();

        ModelAgenda guardado = agendaRepository.save(citaNueva);

        return AgendaResponseDTO.builder()
                .id(guardado.getId())
                .fecha(guardado.getFecha())
                .hora(guardado.getHora())
                .idProfesional(guardado.getIdProfesional())
                .idPaciente(guardado.getIdPaciente())
                .estado(guardado.getEstado())
                .build();
        
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

    public List<ModelAgenda> obtenerCitasDisponibles(String idProfesional, String fecha) {
        try {
            return agendaRepository.findByProfesionalAndFecha(idProfesional, fecha);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener citas disponibles: " + e.getMessage());
        }
    }
}