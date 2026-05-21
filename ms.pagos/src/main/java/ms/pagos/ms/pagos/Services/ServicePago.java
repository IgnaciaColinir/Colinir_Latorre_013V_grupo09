package ms.pagos.ms.pagos.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.pagos.ms.pagos.Modelo.ModeloPago;
import ms.pagos.ms.pagos.Repository.RepositoryPago;
import ms.pagos.ms.pagos.dto.request.PagoRequestDTO;
import ms.pagos.ms.pagos.dto.response.PagoResponseDTO;

import java.util.List;
import java.util.Optional;


@Service // Marca esta clase como capa de lógica de negocio
public class ServicePago {

    @Autowired
    private RepositoryPago pagoRepository;

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

    public PagoResponseDTO guardar(PagoRequestDTO request) {
    // 1. Validaciones previas al guardado
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

    try {
        // 2. Calcular el monto total
        double totalCalculado = request.getValorConsulta() + request.getValorTratamiento();

        // 3. Evaluar el estado que viene en el Request (Evitamos NullPointerException usando "Pagado".equals)
        String estadoFinal = request.getEstado();
        if (estadoFinal == null || estadoFinal.isEmpty()) {
            estadoFinal = "Pendiente"; // Estado por defecto si no envían nada
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
                .fechaPago(request.getFechaPago())
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