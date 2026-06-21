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



@Service // Marca esta clase como capa de lógica de negocio
public class ServicePago {
    @Autowired
    private RepositoryPago pagoRepository;

    @Autowired
    private PacienteClient pacienteClient;

    @Autowired
    private ConsultaClient consultaClient;

    private PagoResponseDTO convertirADto(ModeloPago pago) {
        return PagoResponseDTO.builder()
                .id(pago.getId())
                .idConsulta(pago.getIdConsulta())
                .idPaciente(pago.getIdPaciente())
                .valorConsulta(pago.getValorConsulta())
                .valorTratamiento(pago.getValorTratamiento())
                .montoTotal(pago.getMontoTotal())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .fechaPago(pago.getFechaPago())
                .build();
    }

    public List<PagoResponseDTO> obtenerTodos() {
        try {
            List<ModeloPago> pagos = pagoRepository.findAll();
            return pagos.stream().map(this::convertirADto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener pagos: " + e.getMessage());
        }
    }

    public PagoResponseDTO obtenerPorId(int id) {
        try {
            ModeloPago pago = pagoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pago con ID " + id + " no encontrado"));
            return convertirADto(pago);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar pago: " + e.getMessage());
        }
    }

    public PagoResponseDTO guardarPago(PagoRequestDTO request) {
        try {
            // 1. Verifica si hay paciente
            PacienteResponse paciente = pacienteClient.obtenerPacientePorRut(request.getIdPaciente());
            if (paciente == null) {
                throw new RuntimeException("El paciente no existe");
            }
    
            // Verificar consulta
            List<ConsultasResponse> consultas = consultaClient.obtenerConsultaPorid(request.getIdConsulta());
            if (consultas == null || consultas.isEmpty()) {
                throw new RuntimeException("No se puede pagar, la consulta no existe");
            }
            
            ConsultasResponse consulta = consultas.get(0);

            // Validar consistencia de montos
            if (request.getValorConsulta() != consulta.getValorConsulta() || 
                request.getValorTratamiento() != consulta.getValorTratamiento()) {
                throw new RuntimeException("Inconsistencia, los valores no coinciden con el costo real de la consulta");
            }

            // 2. Validacion de estado (Sincronizado con findByIdConsulta del repositorio)
            Optional<ModeloPago> pagoExistente = pagoRepository.findByIdConsulta(request.getIdConsulta()).stream().findFirst();

            if (pagoExistente.isPresent()) {
                String estadoActual = pagoExistente.get().getEstado();
            
                if (estadoActual.equalsIgnoreCase("Pagado")) {
                    throw new RuntimeException("Ya existe un pago registrado e inmutable para esta consulta.");
                }
        
                if (estadoActual.equalsIgnoreCase("Cancelado")) {
                    throw new RuntimeException("No se puede procesar un pago para una consulta cancelada.");
                }
            }
            
            // 3. Calcular el monto total
            double totalCalculado = request.getValorConsulta() + request.getValorTratamiento();

            // 4. Evaluar el estado que viene en el Request
            String estadoFinal = request.getEstado();
            if (estadoFinal == null || estadoFinal.isEmpty()) {
                estadoFinal = "Pendiente"; 
            }

            // 5. Metodo de pago
            String metodo = request.getMetodoPago().toUpperCase();
            if (!metodo.equals("EFECTIVO") && !metodo.equals("DEBITO") &&
                !metodo.equals("CREDITO") && !metodo.equals("TRANSFERENCIA")) {
                throw new RuntimeException("Metodo de pago no permitido por la clinica");
            }

            // 6. Mapear al modelo
            ModeloPago nuevopago = ModeloPago.builder()
                .idConsulta(request.getIdConsulta())
                .idPaciente(request.getIdPaciente())
                .valorConsulta(request.getValorConsulta())
                .valorTratamiento(request.getValorTratamiento())
                .montoTotal(totalCalculado)
                .metodoPago(request.getMetodoPago())
                .estado(estadoFinal) 
                .fechaPago(LocalDateTime.now())
                .build();

            ModeloPago pagoguardado = pagoRepository.save(nuevopago);

            return convertirADto(pagoguardado);
                
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error fatal al registrar el estado del pago: " + e.getMessage());
        }
    }
    
    // 💡 CORREGIDO: Reescribido usando persistencia limpia de JPA (.save) y DTOs
    public PagoResponseDTO actualizar(int id, PagoRequestDTO request) {
        try {
            ModeloPago pagoExistente = pagoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pago con ID " + id + " no encontrado"));

            pagoExistente.setMetodoPago(request.getMetodoPago());
            if (request.getEstado() != null) {
                pagoExistente.setEstado(request.getEstado());
            }

            ModeloPago pagoActualizado = pagoRepository.save(pagoExistente);
            return convertirADto(pagoActualizado);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar pago: " + e.getMessage());
        }
    }

    public boolean eliminar(int id) {
        try {
            if (pagoRepository.existsById(id)) {
                pagoRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar pago: " + e.getMessage());
        }
    }

    public List<PagoResponseDTO> buscarPorConsulta(int idconsulta) {
        try {
            List<ModeloPago> pagos = pagoRepository.findByIdConsulta(idconsulta);
            return pagos.stream().map(this::convertirADto).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por consulta: " + e.getMessage());
        }
    }

   
}