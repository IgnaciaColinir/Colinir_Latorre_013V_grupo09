package clinicaSalud.ms_profesionales.Controller;

import java.util.List;

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

import clinicaSalud.ms_profesionales.Services.ServicesProfesional;
import clinicaSalud.ms_profesionales.dto.request.ProfesionalRequestDTO;
import clinicaSalud.ms_profesionales.dto.response.ProfesionalResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profesionales")
public class ProfesionalController {

    @Autowired
    private ServicesProfesional servicesProfesional;

    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> guardarProfesional(@Valid @RequestBody ProfesionalRequestDTO request) {
        return new ResponseEntity<>(servicesProfesional.guardar(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(servicesProfesional.obtenerTodos());
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<ProfesionalResponseDTO> obtenerPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(servicesProfesional.obtenerPorRut(rut));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(servicesProfesional.buscarPorEspecialidad(especialidad));
    }

    @PutMapping("/{rut}")
    public ResponseEntity<ProfesionalResponseDTO> actualizarProfesional(@PathVariable String rut, @Valid @RequestBody ProfesionalRequestDTO profesionalActualizado) {
        return ResponseEntity.ok(servicesProfesional.actualizar(rut, profesionalActualizado));
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<String> eliminarProfesional(@PathVariable String rut) {
        servicesProfesional.eliminar(rut);
        return ResponseEntity.ok("Profesional eliminado correctamente");
    }
}