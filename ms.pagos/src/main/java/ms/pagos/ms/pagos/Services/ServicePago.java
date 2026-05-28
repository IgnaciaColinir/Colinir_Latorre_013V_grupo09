package ms.pagos.ms.pagos.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.pagos.ms.pagos.Client.ConsultaClient;
import ms.pagos.ms.pagos.Client.PacienteClient;
import ms.pagos.ms.pagos.Modelo.ModeloPago;
import ms.pagos.ms.pagos.Repository.RepositoryPago;
import ms.pagos.ms.pagos.dto.request.PagoRequestDTO;
import ms.pagos.ms.pagos.dto.response.ConsultasResponse;
import ms.pagos.ms.pagos.dto.response.PacienteResponse;
import ms.pagos.ms.pagos.dto.response.PagoResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;


@Service // Marca esta clase como capa de lógica de negocio
public class ServicePago {

    @Autowired
    private RepositoryPago pagoRepository;

    @Autowired
    private PacienteClient pacienteClient;
    
    @Autowired
    private ConsultaClient consultaClient;

    public List<ModeloPago> obtenerTodos() {
        try {
            return pagoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pagos: " + e.getMessage());
        }
    }

    public List<ModeloPago> obtenerPorId(int id) {
        try {
            return pagoRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar pago: " + e.getMessage());
        }
    }

    public PagoResponseDTO guardarPago(PagoRequestDTO request) {
        try{


            //1. verifica si hay paciente
            PacienteResponse paciente = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());

            if(paciente == null){
                throw new RuntimeException("El paciente no existe");
                }
    
            List<ConsultasResponse> consultas = consultaClient.obtenerConsultaPorid(request.getIdConsulta());
            if(consultas == null|| consultas.isEmpty()){
                throw new RuntimeException( "No se puede pagar, la consulta no existe");
            }
            
            ConsultasResponse consulta = consultas.get(0);

            if(request.getValorConsulta() != consulta.getValorConsulta() || 
                request.getValorTratamiento() != consulta.getValorTratamiento()){
                    throw new RuntimeException("Inconsistencia, los valores no coinciden con el costo real de la consulta");
                }


            
            // 2. Validacion de estado
            Optional<ModeloPago> pagoExistente = pagoRepository.findByConsulta(request.getIdConsulta()).stream().findFirst();

            if (pagoExistente.isPresent()) {
                String estadoActual = pagoExistente.get().getEstado();
            
        
                // Si ya está pagado, no permitimos hacer nada más
                if (estadoActual.equalsIgnoreCase("Pagado")) {
                    throw new RuntimeException("Ya existe un pago registrado e inmutable para esta consulta.");
                    }
        
                // Si está cancelado, tampoco deberías poder operarlo de nuevo
                if (estadoActual.equalsIgnoreCase("Cancelado")) {
                    throw new RuntimeException("No se puede procesar un pago para una consulta cancelada.");
                    }
            }
            
            

   
            // 3. Calcular el monto total
            double totalCalculado = request.getValorConsulta() + request.getValorTratamiento();



            // 4. Evaluar el estado que viene en el Request (Evitamos NullPointerException usando "Pagado".equals)
            String estadoFinal = request.getEstado();
            if (estadoFinal == null || estadoFinal.isEmpty()) {
            estadoFinal = "Pendiente"; // Estado por defecto si no envían nada
            }



            //5.Metodo de pago
            String metodo = request.getMetodoPago().toUpperCase();
            if (!metodo.equals("EFECTIVO")&& !metodo.equals("DEBITO")&&
             !metodo.equals("CREDITO") && !metodo.equals("TRANSFERENCIA")){
                throw new RuntimeException("Metodo de pago no permitido por la clinica");
             }

             

            // 4. Mapear al modelo con el estado dinámico
            ModeloPago nuevopago = ModeloPago.builder()
                .idConsulta(request.getIdConsulta())
                .idPaciente(request.getIdPaciente())
                .valorConsulta(request.getValorConsulta())
                .valorTratamiento(request.getValorTratamiento())
                .montoTotal(totalCalculado)
                .metodoPago(request.getMetodoPago())
                .estado(estadoFinal) // <-- Aquí usamos el estado dinámico validado
                .fechaPago(LocalDateTime.now())
                .build();

            ModeloPago pagoguardado = pagoRepository.save(nuevopago);

            // 5. Retornar el ResponseDTO estructurado con tus Builders
            return PagoResponseDTO.builder()
                .id(pagoguardado.getId())
                .idConsulta(pagoguardado.getIdConsulta())
                .idPaciente(pagoguardado.getIdPaciente())
                .valorConsulta(pagoguardado.getValorConsulta())
                .valorTratamiento(pagoguardado.getValorTratamiento())
                .montoTotal(pagoguardado.getMontoTotal())
                .metodoPago(pagoguardado.getMetodoPago())
                .estado(pagoguardado.getEstado())
                .fechaPago(pagoguardado.getFechaPago())
                .build();
                
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
                throw new RuntimeException("Error fatal al registrar el estado del pago: " + e.getMessage());
            }
        
      
    }
    
                

       
    public ModeloPago actualizar(int id, ModeloPago pago) {
        try {
            return pagoRepository.update(id, pago);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar pago: " + e.getMessage());
        }
    }

    public boolean eliminar(int id) {
        try {
            return pagoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar pago: " + e.getMessage());
        }
    }

    public List<ModeloPago> buscarPorConsulta(int idconsulta) {
        try {
            return pagoRepository.findByConsulta(idconsulta);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por consulta: " + e.getMessage());
        }
    }

   
}