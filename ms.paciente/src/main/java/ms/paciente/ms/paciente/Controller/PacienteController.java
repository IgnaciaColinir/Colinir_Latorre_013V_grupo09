package ms.paciente.ms.paciente.Controller;

import jakarta.validation.Valid;
import ms.paciente.ms.paciente.Model.ModeloPaciente;
import ms.paciente.ms.paciente.Services.ServicesPaciente;
import ms.paciente.ms.paciente.dto.request.PacienteRequestDTO;
import ms.paciente.ms.paciente.dto.response.PacienteResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/pacientes") // Ruta base del controlador
public class PacienteController {

@Autowired
private  ServicesPaciente pacienteServices;
    @GetMapping // Endpoint GET para obtener todos
    public ResponseEntity<List<ModeloPaciente>> obtenerTodos() {
        return ResponseEntity.ok(pacienteServices.obtenerTodos());
    }

    //recuerden que la url aqui seria /api/v1/pacientes/1
   @GetMapping("/{rut}")
    public ResponseEntity<?> obtenerPorRut(@PathVariable String rut) {
            List<ModeloPaciente> paciente = pacienteServices.obtenerPorRut(rut);
            return ResponseEntity.ok(paciente);
    }
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> guardar(
            @Valid @RequestBody PacienteRequestDTO request
    ) {
        PacienteResponseDTO nuevo = pacienteServices.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{Rut}")// Endpoint PUT para actualizar
    public ResponseEntity<?> actualizar(@PathVariable String rut, @Valid @RequestBody ModeloPaciente paciente) {
        ModeloPaciente actualizado = pacienteServices.actualizar(rut, paciente);
            return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{rut}")// Endpoint DELETE
    public ResponseEntity<?> eliminar(@PathVariable String rut) {
        return ResponseEntity.ok("Eliminado");
    }

    @GetMapping("/tratamiento/{tratamiento}")// Endpoint GET por tipo
    public ResponseEntity<?> buscarPorTratamento(@PathVariable String tratamiento) {
        try {
            return ResponseEntity.ok(pacienteServices.buscarPorTratamiento(tratamiento));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar por tratamiento: " + e.getMessage());
        }
    }

    
}