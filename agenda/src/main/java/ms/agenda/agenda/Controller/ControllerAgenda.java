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

import java.time.LocalDate;
import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/agenda") // Ruta base del controlador
public class ControllerAgenda {

@Autowired
private ServiceAgenda agendaService;
    @GetMapping // Endpoint GET para obtener todos
    public ResponseEntity<List<ModelAgenda>> obtenerTodas() {
        return ResponseEntity.ok(agendaService.obtenerTodas());
    }

    //recuerden que la url aqui seria /api/v1/pokemones/1
   @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
            List<ModelAgenda> cita = agendaService.obtenerPorId(id);
            return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<AgendaResponseDTO> guardarCita(
            @Valid @RequestBody AgendaRequestDTO request
    ) {
        AgendaResponseDTO nuevaCita = agendaService.guardarCita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
    }


    @PutMapping("/{id}")// Endpoint PUT para actualizar
    public ResponseEntity<?> actualizar(@PathVariable int id, @Valid @RequestBody ModelAgenda citaActualizada) {
        ModelAgenda actualizada = agendaService.actualizar(id, citaActualizada);
            return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")// Endpoint DELETE
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        return ResponseEntity.ok("Eliminado");
    }

    @GetMapping("/paciente/{idpaciente}")// Endpoint GET por paciente
    public ResponseEntity<?> buscarPorPaciente(@PathVariable String idPaciente) {
        try {
            return ResponseEntity.ok(agendaService.obtenerPorPaciente(idPaciente));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar por paciente: " + e.getMessage());
        }
    }

    @GetMapping("/disponibilidad")// Endpoint GET solo de las citas disponibles
    public ResponseEntity<?> citasDisponibles(@RequestParam String idProfesional, @RequestParam LocalDate fecha) {
        try {
            return ResponseEntity.ok(agendaService.obtenerCitasDisponibles(idProfesional, fecha));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener citas disponibles: " + e.getMessage());
        }
    }
}