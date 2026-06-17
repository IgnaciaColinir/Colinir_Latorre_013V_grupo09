package clinicaSalud.ms_fichas.Controller;

import clinicaSalud.ms_fichas.DTO.FichaDTO;
import clinicaSalud.ms_fichas.Service.FichaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fichas")
@Tag(name = "Fichas Médicas", description = "Operaciones sobre el historial médico")
public class FichaController {

    @Autowired
    private FichaService service;

    @Operation(summary = "Crear Ficha", description = "Registra una nueva ficha médica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ficha creada exitosamente", 
                     content = @Content(schema = @Schema(implementation = FichaDTO.class)))
    })
    @PostMapping
    public ResponseEntity<FichaDTO> crearFicha(@RequestBody FichaDTO fichaDTO) {
        return ResponseEntity.ok(service.guardar(fichaDTO));
    }

    @Operation(summary = "Buscar Fichas por RUT", description = "Trae todas las fichas de un paciente. Integra datos de ms-paciente vía Feign.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fichas encontradas"),
        @ApiResponse(responseCode = "404", description = "El paciente no tiene fichas registradas")
    })
    @GetMapping("/paciente/{rut}")
    public ResponseEntity<List<FichaDTO>> obtenerPorRut(
            @Parameter(description = "RUT del paciente", required = true) @PathVariable String rut) {
        
        List<FichaDTO> fichas = service.obtenerPorRut(rut);
        if (fichas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fichas);
    }
}