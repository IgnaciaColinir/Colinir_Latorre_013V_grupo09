package ms.paciente.ms.paciente.Services;

import java.time.LocalDate;
import java.time.Period;
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


     public List<PacienteResponseDTO> obtenerTodos() {
        try {
            List<ModeloPaciente> pacientes = pancientesRepository.findAll();
            return pacientes.stream().map(paciente -> {
                PacienteResponseDTO dto = new PacienteResponseDTO();
                dto.setRut(paciente.getRut());
                dto.setNombre(paciente.getNombre());
                dto.setApellido(paciente.getApellido());
                dto.setDireccion(paciente.getDireccion());
                dto.setFechaNacimiento(paciente.getFechaNacimiento());
                dto.setTelefono(paciente.getTelefono());
                dto.setEmail(paciente.getEmail());
                dto.setPrevision(paciente.getPrevision());
                dto.setRutTutor(paciente.getRutTutor());
                dto.setNombreTutor(paciente.getNombreTutor());
                return dto;
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pacientes: " + e.getMessage());
        }
    }
    


    public PacienteResponseDTO obtenerPorRut(String rut) {
        try {
            ModeloPaciente paciente = pancientesRepository.findById(rut).orElse(null);
            if (paciente == null) {
                throw new RuntimeException("Paciente con rut " + rut + " no encontrado");
            }
            PacienteResponseDTO response = new PacienteResponseDTO();
            response.setRut(paciente.getRut());
            response.setNombre(paciente.getNombre());
            response.setApellido(paciente.getApellido());
            response.setDireccion(paciente.getDireccion());
            response.setFechaNacimiento(paciente.getFechaNacimiento());
            response.setTelefono(paciente.getTelefono());
            response.setEmail(paciente.getEmail());
            response.setPrevision(paciente.getPrevision());
            response.setRutTutor(paciente.getRutTutor());
            response.setNombreTutor(paciente.getNombreTutor());
            return response;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar paciente: " + e.getMessage());
        }
    }
    
   
    public List<PacienteResponseDTO> obtenerPorPrevision(String prevision) {
        try {
            List<ModeloPaciente> pacientes = pancientesRepository.findByPrevision(prevision);
            
            if (pacientes.isEmpty()) {
                throw new RuntimeException("No se encontraron pacientes con la previsión " + prevision);
            }
            
            return pacientes.stream().map(paciente -> {
                PacienteResponseDTO response = new PacienteResponseDTO();
                response.setRut(paciente.getRut());
                response.setNombre(paciente.getNombre());
                response.setApellido(paciente.getApellido());
                response.setDireccion(paciente.getDireccion());
                response.setFechaNacimiento(paciente.getFechaNacimiento());
                response.setTelefono(paciente.getTelefono());
                response.setEmail(paciente.getEmail());
                response.setPrevision(paciente.getPrevision());
                response.setRutTutor(paciente.getRutTutor());
                response.setNombreTutor(paciente.getNombreTutor());
                return response;
            }).toList();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por previsión: " + e.getMessage());
        }
    }
    

    


    public PacienteResponseDTO registrarPaciente(PacienteRequestDTO request) {
        try {

            // 1. Verificar si el usuario ya existe
            ModeloPaciente existente = pancientesRepository.findById(request.getRut()).orElse(null);
            
            if (existente != null) {
                throw new RuntimeException("El paciente con rut " + request.getRut() + " ya existe");
            }

            int edad = Period.between(request.getFechaNacimiento(), LocalDate.now()).getYears();

            if (edad < 15 && (request.getRutTutor() == null || request.getNombreTutor().isBlank())) {
                throw new RuntimeException("El paciente es menor de edad (menor de 15 años), se requiere un tutor responsable");
            }


            
            // 4. MAPEO: Pasar los datos del Request DTO a la Entidad ModeloPaciente para la BD
            ModeloPaciente pacienteParaGuardar = new ModeloPaciente();
            pacienteParaGuardar.setRut(request.getRut());
            pacienteParaGuardar.setNombre(request.getNombre());
            pacienteParaGuardar.setApellido(request.getApellido());
            pacienteParaGuardar.setDireccion(request.getDireccion());
            pacienteParaGuardar.setTelefono(request.getTelefono());
            pacienteParaGuardar.setEmail(request.getEmail());
            pacienteParaGuardar.setFechaNacimiento(request.getFechaNacimiento());
            pacienteParaGuardar.setPrevision(request.getPrevision());
            pacienteParaGuardar.setRutTutor(request.getRutTutor());
            pacienteParaGuardar.setNombreTutor(request.getNombreTutor());

            // 5. Guardar la entidad en la Base de Datos
            ModeloPaciente pacienteGuardado = pancientesRepository.save(pacienteParaGuardar);

            // 6. MAPEO DE SALIDA: Convertir la entidad guardada al DTO de respuesta que el Controller espera
            PacienteResponseDTO response = new PacienteResponseDTO();
            response.setRut(pacienteGuardado.getRut());
            response.setNombre(pacienteGuardado.getNombre());
            response.setApellido(pacienteGuardado.getApellido());
            response.setDireccion(pacienteGuardado.getDireccion());
            response.setTelefono(pacienteGuardado.getTelefono());
            response.setEmail(pacienteGuardado.getEmail());
            response.setFechaNacimiento(pacienteGuardado.getFechaNacimiento());
            response.setPrevision(pacienteGuardado.getPrevision());
            response.setRutTutor(pacienteGuardado.getRutTutor());
            response.setNombreTutor(pacienteGuardado.getNombreTutor());

            return response;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error fatal e inesperado al registrar el usuario: " + e.getMessage());
        }
    }


    public PacienteResponseDTO actualizar(String rut, PacienteRequestDTO request) {
        try {
            ModeloPaciente pacienteExistente = pancientesRepository.findById(rut).orElseThrow(()-> new RuntimeException("Paciente con rut " + rut + " no encontrado"));

           

            pacienteExistente.setNombre(request.getNombre());
            pacienteExistente.setApellido(request.getApellido());
            pacienteExistente.setDireccion(request.getDireccion());
            pacienteExistente.setTelefono(request.getTelefono());
            pacienteExistente.setEmail(request.getEmail());
            pacienteExistente.setFechaNacimiento(request.getFechaNacimiento());
            pacienteExistente.setPrevision(request.getPrevision());
            pacienteExistente.setRutTutor(request.getRutTutor());
            pacienteExistente.setNombreTutor(request.getNombreTutor());

            ModeloPaciente pacienteActualizado = pancientesRepository.save(pacienteExistente);

            PacienteResponseDTO response = new PacienteResponseDTO();
            response.setRut(pacienteActualizado.getRut());
            response.setNombre(pacienteActualizado.getNombre());
            response.setApellido(pacienteActualizado.getApellido());
            response.setDireccion(pacienteActualizado.getDireccion());
            response.setTelefono(pacienteActualizado.getTelefono());
            response.setEmail(pacienteActualizado.getEmail());
            response.setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
            response.setPrevision(pacienteActualizado.getPrevision());
            response.setRutTutor(pacienteActualizado.getRutTutor());
            response.setNombreTutor(pacienteActualizado.getNombreTutor());

            return response;
        
        } catch (RuntimeException e) {
            throw e;       
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }



   
    public boolean eliminar(String rut) {
        try {
            if (pancientesRepository.existsById(rut)){
                pancientesRepository.deleteById(rut);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar paciente: " + e.getMessage());
        }
    }



    public ContactoPacienteDTO obtenerDatosContacto(String rut) {
    try {
        ModeloPaciente paciente = pancientesRepository.findById(rut)
        .orElseThrow(()-> new RuntimeException("Paciente con rut " + rut + "no encontrado"));
 
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



