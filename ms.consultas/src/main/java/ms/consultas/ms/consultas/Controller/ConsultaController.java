package ms.consultas.ms.consultas.Controller;

import jakarta.validation.Valid;
import ms.consultas.ms.consultas.Services.ConsultaService;
import ms.consultas.ms.consultas.dto.request.ConsultasRequestDTO;
import ms.consultas.ms.consultas.dto.response.ConsultasResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/consultas") // Ruta base del controlador
@Tag(name = "Consultas", description = "Endpoints para gestionar consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    
    
    @GetMapping // Endpoint GET para obtener todos
    @Operation(
        summary = "Obtener todas las consultas",
        description = "Retorna todas las consultas registradas en el sistema"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de consultas obtenidas exitosamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema( schema = @Schema(implementation = ConsultasResponseDTO.class)
            )    
        )
    )
    public ResponseEntity<List<ConsultasResponseDTO>> obtenerTodos() {
        List <ConsultasResponseDTO> consultas = consultaService.obtenerTodos();
        return ResponseEntity.ok(consultas);
    }




    //recuerden que la url aqui seria /api/v1/pokemones/1
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener consulta por id",
        description = "Retorna la consulta registrada en el sistema con el id"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Consulta obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ConsultasResponseDTO.class)
        )
    )
    public ResponseEntity<ConsultasResponseDTO> obtenerPorId(@PathVariable Integer id) {
            ConsultasResponseDTO consulta = consultaService.obtenerPorId(id);
            return ResponseEntity.ok(consulta);
    }


   /*------------------------------------------------------------------------------------------------------- */


    @PostMapping
    @Operation(
        summary = "Crear una nueva consulta",
        description = "Crea una nueva consulta con la información proporcionada en el cuerpo de la solicitud"
    )
    @ApiResponses({ // ◄ Agrupamos con @ApiResponses en plural
        @ApiResponse(
            responseCode = "201",
            description = "Consulta creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ConsultasResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida (El médico ya está ocupado a esa hora o los RUTs no existen)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public ResponseEntity<ConsultasResponseDTO> guardarConsulta(@Valid @RequestBody ConsultasRequestDTO request
    ) {
        ConsultasResponseDTO nuevo = consultaService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }



   /*------------------------------------------------------------------------------------------------------- */


    @PutMapping("/{id}")// Endpoint PUT para actualizar
    @Operation(
        summary = "Actualizar una consulta",
        description = "Actualiza la información de una consulta existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Consulta actualizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ConsultasResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación o el médico se encuentra ocupado en la nueva fecha/hora",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "La consulta con el ID proporcionado no existe",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ConsultasResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ConsultasRequestDTO request) {
        ConsultasResponseDTO actualizado = consultaService.actualizar(id, request);
            return ResponseEntity.ok(actualizado);
    }


    /*--------------------------------------------------------------------------------------------- */
    @GetMapping("/paciente/{idPaciente}")
    @Operation(
        summary = "Obtener consultas por RUT del paciente",
        description = "Retorna el historial completo de consultas médicas asociadas al RUT de un paciente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Historial de consultas obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                // 💡 IMPORTANTE: Avisamos a Swagger que viene un arreglo/lista de consultas
                array = @ArraySchema(schema = @Schema(implementation = ConsultasResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron consultas para el paciente con el RUT proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<List<ConsultasResponseDTO>> obtenerPorIdPaciente(@PathVariable String idPaciente) { // ◄ Cambiado a List<>
        // 1. Llamamos al servicio que ahora devuelve la lista corregida
        List<ConsultasResponseDTO> consultas = consultaService.obtenerPorIdPaciente(idPaciente);
        
        // 2. Si el servicio devolvió una lista vacía (aunque el service ya controla la excepción, esto es una buena doble capa)
        if (consultas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        // 3. Respondemos la lista completa con un estado 200 OK
        return ResponseEntity.ok(consultas);
    }


    /*--------------------------------------------------------------------------------------------------- */

   @DeleteMapping("/{id}")
   @Operation(
        summary = "Eliminar una consulta por id",
        description = "Elimina una consulta existente del sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Consulta eliminada exitosamente",
            content = @Content(mediaType = "text/plain", 
            schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Consulta no encontrada en el sistema",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminarPorId(@PathVariable Integer id) {
       boolean eliminado = consultaService.eliminar(id);
    
        if (eliminado) {
            return ResponseEntity.ok("Eliminada con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Consulta con id " + id + " no encontrado");
                }
    }






   /*------------------------------------------------------------------------------------------------------- */

    @GetMapping("/diagnostico/{diagnostico}")
    @Operation(
        summary = "Obtener consulta por diagnostico",
        description = "Retorna la información de la cosnulta asociada al diagnostico proporcionado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Consulta encontrada con éxito",
            content = @Content(mediaType = "application/json", 
            array = @ArraySchema(schema = @Schema(implementation = ConsultasResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El diagnostico no corresponde a ningúna cosnulta registrada",
            content = @Content
        )
    })
    public ResponseEntity<List<ConsultasResponseDTO>> obtenerPorDiagnostico(@PathVariable String diagnostico) {
        List<ConsultasResponseDTO> consultas = consultaService.obtenerPorDiagnostico(diagnostico);
        
        if (consultas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok(consultas);
    }



    
   /*------------------------------------------------------------------------------------------------------- */


    @GetMapping("/estado/{estado}")
    @Operation(
        summary = "Obtener consulta por estado",
        description = "Retorna la información de la cosnulta asociada al estado proporcionado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Consulta encontrada con éxito",
            content = @Content(
                mediaType = "application/json", 
                array = @ArraySchema(schema = @Schema(implementation = ConsultasResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El estado no corresponde a ningúna consulta registrada",
            content = @Content
        )
    })
    public ResponseEntity<List<ConsultasResponseDTO>> obtenerPorEstado(@PathVariable String estado) {
        List<ConsultasResponseDTO> consultas = consultaService.obtenerConsultasPorEstado(estado);
        
        if (consultas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        return ResponseEntity.ok(consultas);
    }


}