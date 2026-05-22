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
    try {
        LocalDate fechaCita = request.getFecha(); 
        LocalTime horaCita = request.getHora();   

        // 2. VALIDACIÓN DE FECHA PASADA: (La hacemos de inmediato para ahorrar procesamiento)
        LocalDateTime fechaHoraCita = LocalDateTime.of(fechaCita, horaCita);        
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede agendar una cita en una fecha u hora pasada.");
        }

        // 3. CONSULTA AL REPOSITORIO: Ahora sí le pasamos 'fechaCita' que ya es un LocalDate
        List<ModelAgenda> citasdelDia = agendaRepository.findByProfesionalAndFecha(
                request.getIdProfesional(),
                fechaCita // <-- CORRECCIÓN: Usamos la variable LocalDate parseada arriba
        );

        // 4. VALIDACIÓN DE DISPONIBILIDAD: Comparamos usando el objeto horaCita
        boolean estaOcupado = citasdelDia.stream()
                .anyMatch(cita -> cita.getHora().equals(horaCita)); // <-- CORRECCIÓN: equals con LocalTime

        if (estaOcupado) {
            throw new RuntimeException("El profesional ya tiene una cita reservada a esa hora");
        }

        // 2. Validar que la fecha y hora de la cita no sea en el pasado

        if (LocalDateTime.of(request.getFecha(), request.getHora()).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede agendar una cita en una fecha u hora pasada.");
        }

        // 3. LLAMADA OPENFEIGN: Verificar que el paciente exista mediante Feign
        var paciente = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());
        if (paciente == null) {
            throw new RuntimeException("El paciente no está autorizado para agendar o no existe.");
        }

        // Nota: Pasamos 'fechaCita' y 'horaCita' porque el Modelo ya maneja tipos LocalDate/LocalTime
        ModelAgenda citaNueva = ModelAgenda.builder()
                .id(request.getId())
                .fecha(request.getFecha()) 
                .hora(request.getHora())   
                .idProfesional(request.getIdProfesional())
                .idPaciente(request.getIdPaciente())
                .estado(request.getEstado() != null ? request.getEstado() : "RESERVADA") // Estado por defecto si viene null
                .build();

        // 5. PERSISTENCIA
        ModelAgenda guardado = agendaRepository.save(citaNueva);

        // 6. RETORNO: Mapeo hacia el ResponseDTO
        // Nota: Volvemos a usar .toString() si tu DTO final espera un String de texto
        return AgendaResponseDTO.builder()
                .id(guardado.getId())
                .fecha(guardado.getFecha())
                .hora(guardado.getHora())
                .idProfesional(guardado.getIdProfesional())
                .idPaciente(guardado.getIdPaciente())
                .estado(guardado.getEstado())
                .build();

    } catch (RuntimeException e) {
        // Deja pasar directamente tus mensajes controlados de negocio
        throw e;
    } catch (Exception e) {
        // Captura cualquier falla de red de Feign o caídas de la base de datos
        throw new RuntimeException("Error fatal e inesperado al registrar la cita en la agenda: " + e.getMessage());
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