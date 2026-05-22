package ms.pagos.ms.pagos.Controller;

import jakarta.validation.Valid;
import ms.pagos.ms.pagos.Modelo.ModeloPago;
import ms.pagos.ms.pagos.Services.ServicePago;
import ms.pagos.ms.pagos.dto.request.PagoRequestDTO;
import ms.pagos.ms.pagos.dto.response.PagoResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/pagos") // Ruta base del controlador
public class ControllerPago {

@Autowired
private ServicePago pagoService;
    @GetMapping // Endpoint GET para obtener todos
    public ResponseEntity<List<ModeloPago>> obtenerTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    //recuerden que la url aqui seria /api/v1/pagos/1
   @GetMapping("/pagos/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
            List<ModeloPago> pokemon = pagoService.obtenerPorId(id);
            return ResponseEntity.ok(pokemon);
    }
    @PostMapping
    public ResponseEntity<PagoResponseDTO> guardar(
            @Valid @RequestBody PagoRequestDTO request
    ) {
        PagoResponseDTO nuevo = pagoService.guardarPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")// Endpoint PUT para actualizar
    public ResponseEntity<?> actualizar(@PathVariable int id, @Valid @RequestBody ModeloPago pago) {
        ModeloPago actualizado = pagoService.actualizar(id, pago);
            return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")// Endpoint DELETE
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        return ResponseEntity.ok("Eliminado");
    }

    @GetMapping("/consulta/{consulta}")// Endpoint GET por tipo
    public ResponseEntity<?> buscarPorTipo(@PathVariable int consulta) {
        try {
            return ResponseEntity.ok(pagoService.buscarPorConsulta(consulta));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar por tipo: " + e.getMessage());
        }
    }

  
}