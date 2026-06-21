package ms.agenda.agenda.Controller;

import jakarta.validation.Valid;
import ms.agenda.agenda.Model.ModelAgenda;
import ms.agenda.agenda.Services.ServiceAgenda;
import ms.agenda.agenda.dto.request.AgendaRequestDTO;
import ms.agenda.agenda.dto.response.AgendaResponseDTO;

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

import java.time.LocalDate;
import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/agenda") // Ruta base del controlador
@Tag(name= "Agenda", description = "Endpoints para la gestión de citas médicas y disponibilidad de profesionales")
public class ControllerAgenda {



    @Autowired
    private ServiceAgenda agendaService;

    @GetMapping("/test-vivo")
    @Operation(summary = "Verificar estado del servidor", description = "Endpoint de prueba para confirmar que el microservicio está arriba")
    @ApiResponse(responseCode = "200", description = "Servidor activo")
    public ResponseEntity<String> probarSiEstaVivo() {
        return ResponseEntity.ok("¡SÍ, EL SERVIDOR NUEVO ESTÁ CORRIENDO AQUÍ!");
    }

    @GetMapping 
    @Operation(summary = "Obtener todas las citas", description = "Retorna un listado con el historial de todas las citas registradas")
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de citas obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = AgendaResponseDTO.class))
        )
    )
    public ResponseEntity<List<AgendaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(agendaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID", description = "Retorna los detalles de una cita médica específica utilizando su identificador único")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Cita encontrada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgendaResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "La cita con el ID proporcionado no existe", content = @Content)
    })
    public ResponseEntity<AgendaResponseDTO> obtenerPorId(@PathVariable int id) {
        AgendaResponseDTO cita = agendaService.obtenerPorId(id);
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    @Operation(summary = "Reservar una nueva cita", description = "Registra una cita validando la existencia del paciente y profesional vía OpenFeign, además de controlar cruces de horarios")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Cita reservada de manera exitosa",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgendaResponseDTO.class))
        ),
        @ApiResponse(responseCode = "400", description = "Error de validación (Fecha pasada, horario ocupado o usuarios no encontrados)", content = @Content)
    })
    public ResponseEntity<AgendaResponseDTO> guardarCita(@Valid @RequestBody AgendaRequestDTO request) {
        AgendaResponseDTO nuevaCita = agendaService.guardarCita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cita existente", description = "Modifica los datos o el estado de una cita médica mediante su ID")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Cita actualizada de manera exitosa",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AgendaResponseDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "No se encontró la cita para actualizar", content = @Content)
    })
    public ResponseEntity<AgendaResponseDTO> actualizar(@PathVariable int id, @Valid @RequestBody AgendaRequestDTO request) {
        AgendaResponseDTO actualizada = agendaService.actualizar(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar o cancelar una cita", description = "Remueve permanentemente una cita médica del sistema a partir de su ID")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Cita eliminada correctamente",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(responseCode = "404", description = "El ID de la cita no existe en los registros", content = @Content)
    })
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        boolean eliminado = agendaService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Eliminado de la lista");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo eliminar: El ID " + id + " no se encontró en la lista.");
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    @Operation(summary = "Obtener citas por paciente", description = "Retorna el listado de todas las citas agendadas por el RUT de un paciente")
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de citas asociadas al paciente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = AgendaResponseDTO.class))
        )
    )
    public ResponseEntity<List<AgendaResponseDTO>> buscarPorPaciente(@PathVariable String idPaciente) {
        return ResponseEntity.ok(agendaService.obtenerPorPaciente(idPaciente));
    }

    @GetMapping("/disponibilidad")
    @Operation(summary = "Consultar disponibilidad de un profesional", description = "Filtra y retorna las citas ocupadas de un médico en una fecha específica")
    @ApiResponse(
        responseCode = "200", 
        description = "Listado de horarios ocupados/disponibles obtenido correctamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = AgendaResponseDTO.class))
        )
    )
    public ResponseEntity<List<AgendaResponseDTO>> citasDisponibles(
            @Parameter(description = "RUT del profesional médico", required = true) 
            @RequestParam String idProfesional, 
            
            @Parameter(description = "Fecha a consultar (yyyy-MM-dd)", required = true) 
            @RequestParam LocalDate fecha) {
       return ResponseEntity.ok(agendaService.obtenerCitasDisponibles(idProfesional, fecha));
    }
}