package ms.agenda.agenda.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.agenda.agenda.Model.ModelAgenda;
import ms.agenda.agenda.Repository.RepositoryAgenda;
import ms.agenda.agenda.dto.request.AgendaRequestDTO;
import ms.agenda.agenda.dto.response.AgendaResponseDTO;

import java.util.List;


@Service // Marca esta clase como capa de lógica de negocio
public class ServiceAgenda {

    @Autowired
    private RepositoryAgenda agendaRepository;

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
        List<ModelAgenda> citasdelDia = agendaRepository.findByProfesionalAndFecha(
            request.getIdProfesional(),
            request.getFecha()
        );

        boolean estaOcupado= citasdelDia.stream()
            .anyMatch(cita -> cita.getHora().equals(request.getHora()));
        
        if(estaOcupado){
            throw new RuntimeException("El profesional ya tiene una cita reservada a esa hora");
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