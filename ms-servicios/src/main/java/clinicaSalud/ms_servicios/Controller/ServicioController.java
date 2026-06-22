package clinicaSalud.ms_servicios.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import clinicaSalud.ms_servicios.DTO.ServicioDTO;
import clinicaSalud.ms_servicios.Service.ServicioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/servicios")
@Tag(name = "Catálogo de Servicios", description = "Operaciones del catálogo de prestaciones")
public class ServicioController {

    @Autowired
    private ServicioService service; 

    @Operation(summary = "Crear Servicio", description = "Agrega una nueva prestación validando que el precio no sea negativo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Servicio creado exitosamente", 
                     content = @Content(schema = @Schema(implementation = ServicioDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación (ej. Precio negativo)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ServicioDTO> crear(@Valid @RequestBody ServicioDTO servicioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(servicioDTO));
    }

    @Operation(summary = "Listar Todos", description = "Obtiene todo el catálogo de servicios")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listar() {
        return ResponseEntity.ok(service.listarTodo());
    }

    @Operation(summary = "Obtener por ID", description = "Busca un servicio específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio encontrado"),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> obtener(
            @Parameter(description = "ID del servicio a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}