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

    // Método traductor para no repetir código
    private ProfesionalResponseDTO convertirADTO(ModeloProfesional modelo) {
        return ProfesionalResponseDTO.builder()
                .rut(modelo.getRut())
                .nombre(modelo.getNombre())
                .apellido(modelo.getApellido())
                .especialidad(modelo.getEspecialidad())
                .email(modelo.getEmail())
                .telefono(modelo.getTelefono())
                .valorConsulta(modelo.getValorConsulta())
                .build();
    }

    public List<ProfesionalResponseDTO> obtenerTodos() {
        return profesionalesRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProfesionalResponseDTO obtenerPorRut(String rut) {
        Optional<ModeloProfesional> profesional = profesionalesRepository.findById(rut);
        if (profesional.isEmpty()) {
            throw new RuntimeException("Error: Profesional no encontrado con el RUT: " + rut);
        }
        return convertirADTO(profesional.get());
    }

    public List<ProfesionalResponseDTO> buscarPorEspecialidad(String especialidad) {
        return profesionalesRepository.findByEspecialidad(especialidad).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public ProfesionalResponseDTO guardar(ProfesionalRequestDTO request) {
        ModeloProfesional profesional = ModeloProfesional.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .especialidad(request.getEspecialidad())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .valorConsulta(request.getValorConsulta())
                .build();

        ModeloProfesional guardado = profesionalesRepository.save(profesional);
        return convertirADTO(guardado);
    }
    
    public ProfesionalResponseDTO actualizar(String rut, ProfesionalRequestDTO profesionalActualizado) {
        Optional<ModeloProfesional> existente = profesionalesRepository.findById(rut);
        
        if (existente.isEmpty()) {
            throw new RuntimeException("Profesional no encontrado con el RUT: " + rut);
        }
        
        ModeloProfesional profesionalOriginal = existente.get();
        profesionalOriginal.setNombre(profesionalActualizado.getNombre());
        profesionalOriginal.setApellido(profesionalActualizado.getApellido());
        profesionalOriginal.setEspecialidad(profesionalActualizado.getEspecialidad());
        profesionalOriginal.setEmail(profesionalActualizado.getEmail());
        profesionalOriginal.setTelefono(profesionalActualizado.getTelefono());
        profesionalOriginal.setValorConsulta(profesionalActualizado.getValorConsulta());
        
        ModeloProfesional guardado = profesionalesRepository.save(profesionalOriginal);
        return convertirADTO(guardado);
    }

    public void eliminar(String rut) {
        if (!profesionalesRepository.existsById(rut)) {
            throw new RuntimeException("Error al eliminar: Profesional no encontrado");
        }
        profesionalesRepository.deleteById(rut);
    }
}