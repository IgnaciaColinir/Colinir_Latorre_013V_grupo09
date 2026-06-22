package clinicaSalud.ms_profesionales.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import clinicaSalud.ms_profesionales.Services.ServicesProfesional;
import clinicaSalud.ms_profesionales.dto.request.ProfesionalRequestDTO;
import clinicaSalud.ms_profesionales.dto.response.ProfesionalResponseDTO;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/profesionales")
@Tag(name = "Profesionales", description = "Operaciones relacionadas con los médicos y staff")
public class ProfesionalController {

    @Autowired
    private ServicesProfesional servicesProfesional;

    @Operation(summary = "Crear Profesional", description = "Registra a un nuevo médico validando que el RUT no esté duplicado")
    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> guardarProfesional(@Valid @RequestBody ProfesionalRequestDTO request) {
        return new ResponseEntity<>(servicesProfesional.guardar(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener Todos", description = "Lista todo el personal médico de la clínica")
    @GetMapping
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(servicesProfesional.obtenerTodos());
    }

    @Operation(summary = "Buscar por RUT", description = "Encuentra a un médico específico por su RUT")
    @GetMapping("/rut/{rut}")
    public ResponseEntity<ProfesionalResponseDTO> obtenerPorRut(
            @Parameter(description = "RUT del médico", required = true) @PathVariable String rut) {
        return ResponseEntity.ok(servicesProfesional.obtenerPorRut(rut));
    }

    @Operation(summary = "Buscar por Especialidad", description = "Trae a todos los médicos que compartan una especialidad")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerPorEspecialidad(
            @Parameter(description = "Nombre de la especialidad", required = true) @PathVariable String especialidad) {
        return ResponseEntity.ok(servicesProfesional.buscarPorEspecialidad(especialidad));
    }

    @Operation(summary = "Actualizar Profesional", description = "Actualiza los datos de un médico usando su RUT")
    @PutMapping("/{rut}")
    public ResponseEntity<ProfesionalResponseDTO> actualizarProfesional(
            @Parameter(description = "RUT del médico", required = true) @PathVariable String rut, 
            @Valid @RequestBody ProfesionalRequestDTO profesionalActualizado) {
        return ResponseEntity.ok(servicesProfesional.actualizar(rut, profesionalActualizado));
    }

    @Operation(summary = "Eliminar Profesional", description = "Borra el registro de un médico del sistema")
    @DeleteMapping("/{rut}")
    public ResponseEntity<String> eliminarProfesional(
            @Parameter(description = "RUT a eliminar", required = true) @PathVariable String rut) {
        servicesProfesional.eliminar(rut);
        return ResponseEntity.ok("Profesional eliminado correctamente");
    }
}