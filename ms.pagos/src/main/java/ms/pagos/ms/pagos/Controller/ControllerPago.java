package ms.pagos.ms.pagos.Controller;

import jakarta.validation.Valid;
import ms.pagos.ms.pagos.Services.ServicePago;
import ms.pagos.ms.pagos.dto.request.PagoRequestDTO;
import ms.pagos.ms.pagos.dto.response.PagoResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/pagos") // Ruta base del controlador
@Tag(name="Pagos", description = "Endpoints para la gestion financiera")
public class ControllerPago {

    @Autowired
    private ServicePago pagoService;

    @GetMapping
    @Operation(summary = "Obtener todos los pagos", description = "Retorna un listado completo con el historial de todos los pagos registrados en la clínica")
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de pagos obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = PagoResponseDTO.class))
        )
    )
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID", description = "Busca y retorna los detalles de un pago específico a través de su identificador único")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Pago localizado de manera exitosa",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "El ID de pago especificado no existe en el sistema", content = @Content)
    })
    // 💡 CORREGIDO: Retorna un único PagoResponseDTO (en vez de una lista o variable pokemon)
    public ResponseEntity<PagoResponseDTO> obtenerPorId(@PathVariable int id) {
        PagoResponseDTO pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo pago", description = "Procesa el pago de una consulta médica validando la existencia del paciente y de la consulta vía OpenFeign, además de verificar la consistencia de los montos")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Pago registrado e ingresado con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoResponseDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Error de validación (Paciente/Consulta no existen, montos inconsistentes o método de pago inválido)", content = @Content)
    })
    public ResponseEntity<PagoResponseDTO> guardar(@Valid @RequestBody PagoRequestDTO request) {
        PagoResponseDTO nuevo = pagoService.guardarPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago existente", description = "Modifica los datos mutables de una transacción o actualiza su estado")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Pago actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún pago registrado bajo el ID proporcionado", content = @Content)
    })
    // 💡 CORREGIDO: Recibe PagoRequestDTO y retorna PagoResponseDTO en concordancia con el Service
    public ResponseEntity<PagoResponseDTO> actualizar(@PathVariable int id, @Valid @RequestBody PagoRequestDTO request) {
        PagoResponseDTO actualizado = pagoService.actualizar(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro de pago", description = "Remueve físicamente la boleta o comprobante de pago de la base de datos a partir de su ID")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Registro de pago eliminado correctamente del sistema",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(responseCode = "404", description = "El ID del pago no existe en los registros", content = @Content)
    })
    // 💡 CORREGIDO: Se explicita ResponseEntity<String> y se limpia el mensaje confuso sobre el RUT
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        boolean eliminado = pagoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Eliminado con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se borró nada porque el ID " + id + " no coincidió exactamente.");
        }
    }

    @GetMapping("/consulta/{consulta}")
    @Operation(summary = "Buscar pagos por ID de consulta", description = "Filtra e identifica si una consulta médica específica posee transacciones registradas")
    @ApiResponse(
        responseCode = "200", 
        description = "Historial de pagos asociados a la consulta médica obtenido",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = PagoResponseDTO.class))
        )
    )
    public ResponseEntity<List<PagoResponseDTO>> buscarPorTipo(
            @Parameter(description = "ID correlativo de la consulta médica", required = true, example = "45")
            @PathVariable int consulta) {
        return ResponseEntity.ok(pagoService.buscarPorConsulta(consulta));
    }
  
}