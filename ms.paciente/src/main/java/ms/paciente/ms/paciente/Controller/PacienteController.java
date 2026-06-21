package ms.paciente.ms.paciente.Controller;

import jakarta.validation.Valid;
import ms.paciente.ms.paciente.Services.ServicesPaciente;
import ms.paciente.ms.paciente.dto.request.PacienteRequestDTO;
import ms.paciente.ms.paciente.dto.response.ContactoPacienteDTO;
import ms.paciente.ms.paciente.dto.response.PacienteResponseDTO;

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
@RequestMapping("/api/v1/pacientes") // Ruta base del controlador
@Tag(name = "Pacientes", description = "Endpoints para gestionar pacientes, incluyendo operaciones CRUD y consulta de datos de contacto")
public class PacienteController {

    @Autowired
    private  ServicesPaciente pacienteServices;

    @GetMapping // Endpoint GET para obtener todos
    @Operation(
        summary = "Obtener todos los pacientes", 
        description = "Retorna una lista de todos los pacientes registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de pacientes obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = PacienteResponseDTO.class))
        )
    )
    public ResponseEntity<List<PacienteResponseDTO>> obtenerTodos() {
        List<PacienteResponseDTO> pacientes = pacienteServices.obtenerTodos();
        return ResponseEntity.ok(pacientes);
    }


   /*--------------------------------------------------------------------------------------- */
   @GetMapping("/rut/{rut}")
   @Operation(
       summary = "Obtener paciente por rut",
       description = "Retorna el paciente asociado al rut proporcionado"    
   )
   @ApiResponse(
       responseCode = "200",
       description = "Paciente encontrado exitosamente",
       content = @Content(
           mediaType = "application/json",
           schema = @Schema(implementation = PacienteResponseDTO.class)
       )
    )
    public ResponseEntity<PacienteResponseDTO> obtenerPorRut(@PathVariable String rut) {
        PacienteResponseDTO pacientes = pacienteServices.obtenerPorRut(rut);
            
        return ResponseEntity.ok(pacientes);
    }



    /*---------------------------------------------------------------------------------- */
    @GetMapping("/prevision/{prevision}")
    @Operation(
        summary = "Obtener pacientes por previsión",
        description = "Retorna una lista de pacientes asociados a la previsión proporcionada"    
    )
    @ApiResponse(
        responseCode = "200",
        description = "Pacientes encontrados exitosamente",
        content = @Content(
            mediaType = "application/json",
            // 💡 CORREGIDO: Mapeado como ArraySchema para colecciones de salida
            array = @ArraySchema(schema = @Schema(implementation = PacienteResponseDTO.class))
        )
    )
    public ResponseEntity<List<PacienteResponseDTO>> obtenerPorPrevision(@PathVariable String prevision) {
        List<PacienteResponseDTO> pacientes = pacienteServices.obtenerPorPrevision(prevision);
        return ResponseEntity.ok(pacientes);
    }



    @PostMapping
    @Operation(
        summary = "Crear un nuevo paciente",
        description = "Crea un nuevo paciente con la información proporcionada en el cuerpo de la solicitud"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Paciente creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PacienteResponseDTO.class)
            )
    )
    
    public ResponseEntity<PacienteResponseDTO> guardarPaciente(@Valid @RequestBody PacienteRequestDTO request) {
        PacienteResponseDTO nuevo = pacienteServices.registrarPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }


/*-----------------------------------------------------------------------------------------------*/


    @PutMapping("/{rut}")// Endpoint PUT para actualizar
    @Operation(
        summary = "Actualizar paciente por RUT",
        description = "Actualiza la información del paciente asociado al RUT proporcionado con los datos en el cuerpo de la solicitud"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Paciente actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PacienteResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida, por ejemplo, si el nuevo email ya está asociado a otro paciente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Paciente no encontrado con el RUT proporcionado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public ResponseEntity<PacienteResponseDTO> actualizar(@PathVariable String rut, @Valid @RequestBody PacienteRequestDTO request) {
        PacienteResponseDTO actualizado = pacienteServices.actualizar(rut, request);
            return ResponseEntity.ok(actualizado);
    }


    /*-------------------------------------------------------------------------------------------------------------- */
    @DeleteMapping("/{rut}")// Endpoint DELETE
    @Operation(
        summary = "Eliminar paciente por RUT",
        description = "Elimina el paciente asociado al RUT proporcionado del sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Paciente eliminado exitosamente",
            content = @Content(mediaType = "text/plain", 
            schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Paciente no encontrado en el sistema",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminar(@PathVariable String rut) {
        boolean eliminado = pacienteServices.eliminar(rut);
    
        if (eliminado) {
            return ResponseEntity.ok("Eliminado con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Eliminacion fallida, el RUT no coincide.");
        }
    }

    /*-------------------------------------------------------------------------------------------------------------- */


    @GetMapping("/contact/{rut}") // Endpoint GET para obtener datos de contacto
    @Operation(
        summary = "Obtener datos de contacto del paciente por RUT",
        description = "Retorna el teléfono y email del paciente asociado al RUT proporcionado"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Datos de contacto obtenidos exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ContactoPacienteDTO.class)
        )
    )
    public ResponseEntity<ContactoPacienteDTO> obtenerDatosContacto(@PathVariable String rut) {
        ContactoPacienteDTO contacto = pacienteServices.obtenerDatosContacto(rut);
        return ResponseEntity.ok(contacto);
    }
}