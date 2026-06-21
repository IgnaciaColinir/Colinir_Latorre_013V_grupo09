package clinicaSalud.ms_profesionales.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import clinicaSalud.ms_profesionales.Model.ModeloProfesional;
import clinicaSalud.ms_profesionales.Repository.RepositoryProfesionales;
import clinicaSalud.ms_profesionales.dto.request.ProfesionalRequestDTO;
import clinicaSalud.ms_profesionales.dto.response.ProfesionalResponseDTO;

@Service
public class ServicesProfesional {

    @Autowired
    private RepositoryProfesionales profesionalesRepository;

    private ProfesionalResponseDTO convertirADTO(ModeloProfesional modelo) {
        return ProfesionalResponseDTO.builder()
                .rut(modelo.getRut())
                .nombre(modelo.getNombre())
                .apellido(modelo.getApellido())
                .especialidad(modelo.getEspecialidad())
                .email(modelo.getEmail())
                .telefono(modelo.getTelefono())
                .build();
    }

    public List<ProfesionalResponseDTO> obtenerTodos() {
        return profesionalesRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProfesionalResponseDTO obtenerPorRut(String rut) {
        ModeloProfesional profesional = profesionalesRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("Error: Profesional no encontrado con el RUT: " + rut));
        return convertirADTO(profesional);
    }

    public List<ProfesionalResponseDTO> buscarPorEspecialidad(String especialidad) {
        return profesionalesRepository.findByEspecialidad(especialidad).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public ProfesionalResponseDTO guardar(ProfesionalRequestDTO request) {
        // Regla de Negocio: No permitir duplicados de RUT
        if (profesionalesRepository.existsByRut(request.getRut())) {
            throw new IllegalArgumentException("Error: Ya existe un profesional registrado con el RUT: " + request.getRut());
        }

        ModeloProfesional profesional = ModeloProfesional.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .especialidad(request.getEspecialidad())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .build();

        ModeloProfesional guardado = profesionalesRepository.save(profesional);
        return convertirADTO(guardado);
    }
    
    public ProfesionalResponseDTO actualizar(String rut, ProfesionalRequestDTO profesionalActualizado) {
        ModeloProfesional profesionalOriginal = profesionalesRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("Profesional no encontrado con el RUT: " + rut));
        
        profesionalOriginal.setNombre(profesionalActualizado.getNombre());
        profesionalOriginal.setApellido(profesionalActualizado.getApellido());
        profesionalOriginal.setEspecialidad(profesionalActualizado.getEspecialidad());
        profesionalOriginal.setEmail(profesionalActualizado.getEmail());
        profesionalOriginal.setTelefono(profesionalActualizado.getTelefono());
        
        ModeloProfesional guardado = profesionalesRepository.save(profesionalOriginal);
        return convertirADTO(guardado);
    }

    public void eliminar(String rut) {
        ModeloProfesional profesional = profesionalesRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("Error al eliminar: Profesional no encontrado"));
        
        profesionalesRepository.delete(profesional);
    }
}