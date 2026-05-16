package ms.paciente.ms.paciente.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.paciente.ms.paciente.Model.ModeloPaciente;
import ms.paciente.ms.paciente.Repository.RepositoryPacientes;
import ms.paciente.ms.paciente.dto.request.PacienteRequestDTO;
import ms.paciente.ms.paciente.dto.response.PacienteResponseDTO;

@Service
public class ServicesPaciente {

    @Autowired
    private RepositoryPacientes pancientesRepository;

    public List<ModeloPaciente> obtenerTodos() {
        try {
            return pancientesRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pacientes: " + e.getMessage());
        }
    }
    

    public List <ModeloPaciente> obtenerPorRut(String rut) {
        try {
            return pancientesRepository.findByRut(rut);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar paciente: " + e.getMessage());
        }
    }
    
    public List <ModeloPaciente> obtenerPorPrevision(String prevision) {
        try {
            return pancientesRepository.findByPrevision(prevision);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar paciente: " + e.getMessage());
        }
    }

    public PacienteResponseDTO guardar(PacienteRequestDTO request) {

        ModeloPaciente paciente = ModeloPaciente.builder()


                .tratamiento(request.getTratamiento())
                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .direccion(request.getDireccion())
                .telefono((request.getTelefono()))
                .email((request.getEmail()))
                .valorTratamiento((request.getValorTratamiento()))
                .build();

        ModeloPaciente guardado = pancientesRepository.save(paciente);

        return PacienteResponseDTO.builder()
                .tratamiento(guardado.getTratamiento())
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .apellido(guardado.getApellido())
                .direccion(guardado.getDireccion())
                .telefono((guardado.getTelefono()))
                .email((guardado.getEmail()))
                .valorTratamiento((guardado.getValorTratamiento()))
                .build();
    }
    
    public ModeloPaciente actualizar(String rut, ModeloPaciente pacienteActualizado) {
            try {
                return pancientesRepository.update(rut, pacienteActualizado);
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar paciente: " + e.getMessage());
            }
    }

    public boolean eliminar(String rut) {
            try {
                return pancientesRepository.deleteByRut(rut);
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar paciente: " + e.getMessage());
            }
    }

    public List<ModeloPaciente> buscarPorTratamiento(String tratamiento) {
        try {
            return pancientesRepository.findbyTratamiento(tratamiento);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por tratamiento: " + e.getMessage());
        }
    }       

}
