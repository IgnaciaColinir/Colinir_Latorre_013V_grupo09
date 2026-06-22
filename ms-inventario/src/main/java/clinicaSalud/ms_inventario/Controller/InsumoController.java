package clinicaSalud.ms_inventario.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_inventario.DTO.InsumoDTO;
import clinicaSalud.ms_inventario.Service.InsumoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/inventario") 
@Tag(name = "Inventario", description = "Operaciones relacionadas con el stock de insumos")
public class InsumoController {

    @Autowired
    private InsumoService service;

    @Operation(summary = "Crear Insumo", description = "Registra un nuevo insumo validando que el stock no sea negativo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Insumo creado exitosamente", 
                     content = @Content(schema = @Schema(implementation = InsumoDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error de validación (ej. Stock negativo)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<InsumoDTO> crearInsumo(@RequestBody InsumoDTO insumoDTO) {
        return ResponseEntity.ok(service.guardar(insumoDTO));
    }

    @Operation(summary = "Obtener Todos", description = "Lista todos los insumos registrados en el inventario")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<InsumoDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }
}