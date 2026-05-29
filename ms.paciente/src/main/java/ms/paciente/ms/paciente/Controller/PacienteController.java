package ms.paciente.ms.paciente.Controller;

import jakarta.validation.Valid;
import ms.paciente.ms.paciente.Model.ModeloPaciente;
import ms.paciente.ms.paciente.Services.ServicesPaciente;
import ms.paciente.ms.paciente.dto.request.PacienteRequestDTO;
import ms.paciente.ms.paciente.dto.response.ContactoPacienteDTO;
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
   @GetMapping("/rut/{rut}")
    public ResponseEntity<ModeloPaciente> obtenerPorRut(@PathVariable String rut) {
        List<ModeloPaciente> pacientes = pacienteServices.obtenerPorRut(rut);
    
        if (pacientes.isEmpty()) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no existe
        }
        
        return ResponseEntity.ok(pacientes.get(0));
    }

    @GetMapping("/prevision/{prevision}")
    public ResponseEntity<?> obtenerPorPrevision(@PathVariable String prevision) {
            List<ModeloPaciente> paciente = pacienteServices.obtenerPorPrevision(prevision);
            return ResponseEntity.ok(paciente);
    }


    @PostMapping
    public ResponseEntity<PacienteResponseDTO> guardarPaciente(
            @Valid @RequestBody PacienteRequestDTO request
    ) {
        PacienteResponseDTO nuevo = pacienteServices.registrarPaciente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{rut}")// Endpoint PUT para actualizar
    public ResponseEntity<?> actualizar(@PathVariable String rut, @Valid @RequestBody ModeloPaciente paciente) {
        ModeloPaciente actualizado = pacienteServices.actualizar(rut, paciente);
            return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{rut}")// Endpoint DELETE
    public ResponseEntity<?> eliminar(@PathVariable String rut) {
        boolean eliminado = pacienteServices.eliminar(rut);
    
        if (eliminado) {
            return ResponseEntity.ok("Eliminado con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No se borró nada porque el RUT no coincidió exactamente.");
        }
    }


    @GetMapping("/contact/{rut}") // Endpoint GET para obtener datos de contacto
    public ResponseEntity<ContactoPacienteDTO> obtenerDatosContacto(@PathVariable String rut) {
        ContactoPacienteDTO contacto = pacienteServices.obtenerDatosContacto(rut);
        return ResponseEntity.ok(contacto);
    }
}