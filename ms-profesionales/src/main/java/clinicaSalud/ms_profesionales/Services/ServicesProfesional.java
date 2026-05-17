package clinicaSalud.ms_profesionales.Services;

import java.util.List;
import java.util.Optional;
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

    public List<ModeloProfesional> obtenerTodos() {
        return profesionalesRepository.findAll();
    }

    public Optional<ModeloProfesional> obtenerPorRut(String rut) {
        return profesionalesRepository.findById(rut);
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

        return ProfesionalResponseDTO.builder()
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .apellido(guardado.getApellido())
                .especialidad(guardado.getEspecialidad())
                .email(guardado.getEmail())
                .telefono(guardado.getTelefono())
                .valorConsulta(guardado.getValorConsulta())
                .build();
    }
    
    public ModeloProfesional actualizar(String rut, ModeloProfesional profesionalActualizado) {
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
        
        return profesionalesRepository.save(profesionalOriginal);
    }

    public boolean eliminar(String rut) {
        if (profesionalesRepository.existsById(rut)) {
            profesionalesRepository.deleteById(rut);
            return true;
        }
        return false;
    }

    public List<ModeloProfesional> buscarPorEspecialidad(String especialidad) {
        return profesionalesRepository.findByEspecialidad(especialidad);
    }       
}