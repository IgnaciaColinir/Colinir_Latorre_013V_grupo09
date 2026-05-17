package clinicaSalud.ms_profesionales.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_profesionales.Model.ModeloProfesional;
import clinicaSalud.ms_profesionales.Services.ServicesProfesional;
import clinicaSalud.ms_profesionales.dto.request.ProfesionalRequestDTO;
import clinicaSalud.ms_profesionales.dto.response.ProfesionalResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    @Autowired
    private ServicesProfesional servicesProfesional;

    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> guardarProfesional(@Valid @RequestBody ProfesionalRequestDTO request) {
        ProfesionalResponseDTO respuesta = servicesProfesional.guardar(request);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ModeloProfesional>> obtenerTodos() {
        return ResponseEntity.ok(servicesProfesional.obtenerTodos());
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<ModeloProfesional> obtenerPorRut(@PathVariable String rut) {
        Optional<ModeloProfesional> profesional = servicesProfesional.obtenerPorRut(rut);
        return profesional.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<ModeloProfesional>> obtenerPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(servicesProfesional.buscarPorEspecialidad(especialidad));
    }

    @PutMapping("/{rut}")
    public ResponseEntity<ModeloProfesional> actualizarProfesional(@PathVariable String rut, @RequestBody ModeloProfesional profesionalActualizado) {
        return ResponseEntity.ok(servicesProfesional.actualizar(rut, profesionalActualizado));
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<String> eliminarProfesional(@PathVariable String rut) {
        boolean eliminado = servicesProfesional.eliminar(rut);
        if (eliminado) {
            return ResponseEntity.ok("Profesional eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el profesional");
        }
    }
}