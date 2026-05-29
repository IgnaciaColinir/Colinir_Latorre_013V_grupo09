package ms.paciente.ms.paciente.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.paciente.ms.paciente.Model.ModeloPaciente;
import ms.paciente.ms.paciente.Repository.RepositoryPacientes;
import ms.paciente.ms.paciente.dto.request.PacienteRequestDTO;
import ms.paciente.ms.paciente.dto.response.ContactoPacienteDTO;
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

    public PacienteResponseDTO registrarPaciente(PacienteRequestDTO request) {
    try {

        // 1. No pueden haber 2 pacientes con el mismo rut
        ModeloPaciente existente = pancientesRepository.findByRut(request.getRut())
                .stream().findFirst().orElse(null);
        
        if (existente != null) {
            throw new RuntimeException("El paciente con rut " + request.getRut() + " ya existe");
        }

        // 2. Validación de Tutor Responsable
        int ageNacimiento = request.getFechaNacimiento().getYear();
        int ageActual = java.time.LocalDate.now().getYear();
        int edad = ageActual - ageNacimiento;

        if (edad < 15 && (request.getRutTutor() == null || request.getNombreTutor().isBlank())) {
            throw new RuntimeException("El paciente es menor de edad (menor de 15 años), se requiere un tutor responsable");
        }

        ModeloPaciente paciente = ModeloPaciente.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .direccion(request.getDireccion())
                .fechaNacimiento(request.getFechaNacimiento())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .prevision(request.getPrevision())
                .rutTutor(request.getRutTutor())
                .nombreTutor(request.getNombreTutor())
                .build();

        ModeloPaciente guardado = pancientesRepository.save(paciente);

        return PacienteResponseDTO.builder()
                .rut(guardado.getRut())
                .nombre(guardado.getNombre())
                .apellido(guardado.getApellido())
                .direccion(guardado.getDireccion())
                .telefono(guardado.getTelefono())
                .email(guardado.getEmail())
                .fechaNacimiento(guardado.getFechaNacimiento())
                .prevision(guardado.getPrevision())
                .rutTutor(guardado.getRutTutor())
                .nombreTutor(guardado.getNombreTutor())
                .build();

    } catch (RuntimeException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Error fatal e inesperado al registrar el paciente: " + e.getMessage());
    }
}
    public ModeloPaciente actualizar(String rut, ModeloPaciente pacienteActualizado) {
            try { 
               
                return pancientesRepository.update(rut, pacienteActualizado);
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar paciente (rut no encontrado): " + e.getMessage());
            }
    }

    public boolean eliminar(String rut) {
            try {
                return pancientesRepository.deleteByRut(rut);
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar paciente: " + e.getMessage());
            }
    }

    public ContactoPacienteDTO obtenerDatosContacto(String rut) {
    try {
        List<ModeloPaciente> listaPacientes = obtenerPorRut(rut);
        
        // Si la lista está vacía, lanzamos el error manualmente
        if (listaPacientes.isEmpty()) {
            throw new RuntimeException("Paciente con RUT " + rut + " no encontrado");
        }
        
        // Si no está vacía, sacamos con seguridad el paciente en la posición 0
        ModeloPaciente paciente = listaPacientes.get(0);
        
        return ContactoPacienteDTO.builder()
                .telefono(paciente.getTelefono())
                .email(paciente.getEmail())
                .build();
                
    } catch (RuntimeException e) {
        throw e; 
    } catch (Exception e) {
        throw new RuntimeException("Error al obtener datos de contacto: " + e.getMessage());
    }
}


}



