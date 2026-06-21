package ms.consultas.ms.consultas.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ms.consultas.ms.consultas.Modelo.ModeloConsulta;
import ms.consultas.ms.consultas.Repository.ConsultaRepository;
import ms.consultas.ms.consultas.client.PacienteClient;
import ms.consultas.ms.consultas.client.ProfesionalClient;
import ms.consultas.ms.consultas.dto.request.ConsultasRequestDTO;
import ms.consultas.ms.consultas.dto.response.ConsultasResponseDTO;
import ms.consultas.ms.consultas.dto.response.PacienteResponse;
import ms.consultas.ms.consultas.dto.response.ProfesionalResponse;

import java.util.List;


@Service // Marca esta clase como capa de lógica de negocio
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired 
    private PacienteClient pacienteClient;
    @Autowired
    private ProfesionalClient profesionalClient;

   
    public List<ConsultasResponseDTO> obtenerTodos() {
        try {
            List<ModeloConsulta> consultas = consultaRepository.findAll();
            return consultas.stream().map(consulta -> {
            ConsultasResponseDTO dto = new ConsultasResponseDTO();
            dto.setId(consulta.getId());
            dto.setIdPaciente(consulta.getIdPaciente());
            dto.setNomPaciente(consulta.getNomPaciente());
            dto.setIdMedico(consulta.getIdMedico());
            dto.setNomMedico(consulta.getNomMedico());
            dto.setFechaConsulta(consulta.getFechaConsulta());
            dto.setHoraConsulta(consulta.getHoraConsulta());
            dto.setDiagnostico(consulta.getDiagnostico());
            dto.setValorConsulta(consulta.getValorConsulta());
            dto.setValorTratamiento(consulta.getValorTratamiento());
            
            
            return dto;
        }).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener consultas: " + e.getMessage());
        }
    }

 

    public ConsultasResponseDTO obtenerPorId(Integer id) {
        try {
            ModeloConsulta consulta = consultaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consulta con ID " + id + " no encontrada"));
            ConsultasResponseDTO response = new ConsultasResponseDTO();
            response.setId(consulta.getId());
            response.setIdPaciente(consulta.getIdPaciente());
            response.setNomPaciente(consulta.getNomPaciente());
            response.setIdMedico(consulta.getIdMedico());
            response.setNomMedico(consulta.getNomMedico());
            response.setFechaConsulta(consulta.getFechaConsulta());
            response.setHoraConsulta(consulta.getHoraConsulta());
            response.setDiagnostico(consulta.getDiagnostico());
            response.setValorConsulta(consulta.getValorConsulta());
            response.setValorTratamiento(consulta.getValorTratamiento());
            return response;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar consulta: " + e.getMessage());
        }
    }



    public ConsultasResponseDTO guardar(ConsultasRequestDTO request) {
        try {

            // 1. Verificar si el usuario ya existe
            boolean ocupado = consultaRepository.existsByFechaConsultaAndHoraConsultaAndIdMedico(
                request.getFechaConsulta(),
                request.getHoraConsulta(),
                request.getIdMedico()
            );

            if (ocupado) {
                throw new RuntimeException("Ya existe una consulta programada para esa fecha y hora.");
            }

            String nombreCompletoPaciente;
            String nombreCompletoMedico;
            try {
                PacienteResponse paciente = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());
                nombreCompletoPaciente = paciente.getNombre() + " " + paciente.getApellido();
            } catch (Exception e) {
                throw new RuntimeException("Error: El paciente con RUT " + request.getIdPaciente() + " no existe o el servicio no está disponible.");
            }

            try {
                ProfesionalResponse medico = profesionalClient.obtenerProfesionalPorRut(request.getIdMedico());
                nombreCompletoMedico = medico.getNombre() + " " + medico.getApellido();
            } catch (Exception e) {
                throw new RuntimeException("Error: El médico con RUT " + request.getIdMedico() + " no existe o el servicio no está disponible.");
            }

            
            // 4. MAPEO: Pasar los datos del Request DTO a la Entidad ModeloUsuario para la BD
            ModeloConsulta consultaParaGuardar = new ModeloConsulta();
            consultaParaGuardar.setIdPaciente(request.getIdPaciente());
            consultaParaGuardar.setNomPaciente(nombreCompletoPaciente);
            consultaParaGuardar.setIdMedico(request.getIdMedico());
            consultaParaGuardar.setNomMedico(nombreCompletoMedico);
            consultaParaGuardar.setFechaConsulta(request.getFechaConsulta());
            consultaParaGuardar.setHoraConsulta(request.getHoraConsulta());
            consultaParaGuardar.setDiagnostico(request.getDiagnostico());
            consultaParaGuardar.setValorConsulta(request.getValorConsulta());
            consultaParaGuardar.setValorTratamiento(request.getValorTratamiento());

            ModeloConsulta consultaGuardada = consultaRepository.save(consultaParaGuardar);

            ConsultasResponseDTO response = new ConsultasResponseDTO();
            response.setId(consultaGuardada.getId());
            response.setIdPaciente(consultaGuardada.getIdPaciente());
            response.setNomPaciente(consultaGuardada.getNomPaciente());
            response.setNomMedico(consultaGuardada.getNomMedico());
            response.setFechaConsulta(consultaGuardada.getFechaConsulta());
            response.setHoraConsulta(consultaGuardada.getHoraConsulta());
            response.setDiagnostico(consultaGuardada.getDiagnostico());
            response.setValorConsulta(consultaGuardada.getValorConsulta());
            response.setValorTratamiento(consultaGuardada.getValorTratamiento());

            return response;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error fatal e inesperado al registrar la consulta: " + e.getMessage());
        }
    }
    


    public ConsultasResponseDTO actualizar(Integer id, ConsultasRequestDTO request) {
        try {
            ModeloConsulta consultaExistente = consultaRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Consulta con id " + id + " no encontrada"));


            if (!consultaExistente.getFechaConsulta().equals(request.getFechaConsulta())||
                !consultaExistente.getHoraConsulta().equals(request.getHoraConsulta())||
                !consultaExistente.getIdMedico().equals(request.getIdMedico())) {
                    boolean ocupado = consultaRepository.existsByFechaConsultaAndHoraConsultaAndIdMedico(
                        request.getFechaConsulta(),
                        request.getHoraConsulta(),
                        request.getIdMedico()
                    );

                    if (ocupado){
                            throw new RuntimeException("Ya existe una consulta programada para esa fecha y hora.");
                    }
            }


            consultaExistente.setIdPaciente(request.getIdPaciente());
            consultaExistente.setNomPaciente(request.getNomPaciente());
            consultaExistente.setIdMedico(request.getIdMedico());
            consultaExistente.setNomMedico(request.getNomMedico());
            consultaExistente.setFechaConsulta(request.getFechaConsulta());
            consultaExistente.setHoraConsulta(request.getHoraConsulta());
            consultaExistente.setDiagnostico(request.getDiagnostico());
            consultaExistente.setValorConsulta(request.getValorConsulta());
            consultaExistente.setValorTratamiento(request.getValorTratamiento());

            ModeloConsulta consultaActualizada = consultaRepository.save(consultaExistente);

            ConsultasResponseDTO response = new ConsultasResponseDTO();
            response.setId(consultaActualizada.getId());
            response.setIdPaciente(consultaActualizada.getIdPaciente());
            response.setNomPaciente(consultaActualizada.getNomPaciente());
            response.setIdMedico(consultaActualizada.getIdMedico());
            response.setNomMedico(consultaActualizada.getNomMedico());
            response.setFechaConsulta(consultaActualizada.getFechaConsulta());
            response.setHoraConsulta(consultaActualizada.getHoraConsulta());
            response.setDiagnostico(consultaActualizada.getDiagnostico());
            response.setValorConsulta(consultaActualizada.getValorConsulta());
            response.setValorTratamiento(consultaActualizada.getValorTratamiento());

            return response;
        
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar consulta: " + e.getMessage());
        }
    }
       
   

    public boolean eliminar(Integer id) {
        try {
            if (consultaRepository.existsById(id)){
                consultaRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar consulta: " + e.getMessage());
        }
    }

    public List<ConsultasResponseDTO> obtenerPorIdPaciente(String idPaciente) {
        try {
            List<ModeloConsulta> consultas = consultaRepository.findByIdPaciente(idPaciente);
            
            if (consultas.isEmpty()) {
                throw new RuntimeException("No se encontraron consultas para el paciente con RUT " + idPaciente);
            }
            
            return consultas.stream().map(consulta -> {
                ConsultasResponseDTO response = new ConsultasResponseDTO();
                response.setId(consulta.getId());
                response.setIdPaciente(consulta.getIdPaciente());
                response.setNomPaciente(consulta.getNomPaciente());
                response.setIdMedico(consulta.getIdMedico());
                response.setNomMedico(consulta.getNomMedico());
                response.setFechaConsulta(consulta.getFechaConsulta());
                response.setHoraConsulta(consulta.getHoraConsulta());
                response.setDiagnostico(consulta.getDiagnostico());
                response.setValorConsulta(consulta.getValorConsulta());
                response.setValorTratamiento(consulta.getValorTratamiento());
                return response;
            }).toList();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar consultas del paciente: " + e.getMessage());
        }
    }

    

  

    public List<ConsultasResponseDTO> obtenerPorDiagnostico(String diagnostico) { // ◄ Cambiado el retorno a List
        try {
            List<ModeloConsulta> consultas = consultaRepository.findByDiagnostico(diagnostico);

            if (consultas.isEmpty()) {
                throw new RuntimeException("No se encontraron consultas con el diagnóstico: " + diagnostico);
            }

            return consultas.stream().map(consulta -> {
                ConsultasResponseDTO response = new ConsultasResponseDTO();
                response.setId(consulta.getId());
                response.setIdPaciente(consulta.getIdPaciente());
                response.setNomPaciente(consulta.getNomPaciente());
                response.setIdMedico(consulta.getIdMedico());
                response.setNomMedico(consulta.getNomMedico());
                response.setFechaConsulta(consulta.getFechaConsulta());
                response.setHoraConsulta(consulta.getHoraConsulta());
                response.setDiagnostico(consulta.getDiagnostico());
                response.setValorConsulta(consulta.getValorConsulta());
                response.setValorTratamiento(consulta.getValorTratamiento());
                return response;
            }).toList(); 

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar consultas por diagnóstico: " + e.getMessage());
        }
    }


    public List<ConsultasResponseDTO> obtenerConsultasPorEstado(String estado) {
        try {
            // Buscamos la lista de entidades en el repositorio
            List<ModeloConsulta> consultas = consultaRepository.findByEstadoIgnoreCase(estado);
            
            // Las transformamos a DTOs de salida
            return consultas.stream().map(consulta -> {
                ConsultasResponseDTO dto = new ConsultasResponseDTO();
                dto.setId(consulta.getId());
                dto.setIdPaciente(consulta.getIdPaciente());
                dto.setNomPaciente(consulta.getNomPaciente());
                dto.setIdMedico(consulta.getIdMedico());
                dto.setNomMedico(consulta.getNomMedico());
                dto.setFechaConsulta(consulta.getFechaConsulta());
                dto.setHoraConsulta(consulta.getHoraConsulta());
                dto.setDiagnostico(consulta.getDiagnostico());
                dto.setValorConsulta(consulta.getValorConsulta());
                dto.setValorTratamiento(consulta.getValorTratamiento());
                return dto;
            }).toList();
            
        } catch (Exception e) {            
            throw new RuntimeException("Error al obtener consultas por estado: " + e.getMessage());
        }

    }
    

    
}