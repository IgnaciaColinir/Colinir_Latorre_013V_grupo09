package ms.consultas.ms.consultas.Controller;

import jakarta.validation.Valid;
import ms.consultas.ms.consultas.Modelo.ModeloConsulta;
import ms.consultas.ms.consultas.Services.ConsultaService;
import ms.consultas.ms.consultas.dto.request.ConsultasRequestDTO;
import ms.consultas.ms.consultas.dto.response.ConsultasResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/consultas") // Ruta base del controlador
public class ConsultaController {

@Autowired
private ConsultaService consultaService;
    @GetMapping // Endpoint GET para obtener todos
    public ResponseEntity<List<ModeloConsulta>> obtenerTodos() {
        return ResponseEntity.ok(consultaService.obtenerTodos());
    }

    //recuerden que la url aqui seria /api/v1/pokemones/1
   @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
            List<ModeloConsulta> consulta = consultaService.obtenerPorId(id);
            return ResponseEntity.ok(consulta);
    }


    @PostMapping
    public ResponseEntity<ConsultasResponseDTO> guardar(
            @Valid @RequestBody ConsultasRequestDTO request
    ) {
        ConsultasResponseDTO nuevo = consultaService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")// Endpoint PUT para actualizar
    public ResponseEntity<?> actualizar(@PathVariable int id, @Valid @RequestBody ModeloConsulta consulta) {
        ModeloConsulta actualizado = consultaService.actualizar(id, consulta);
            return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")// Endpoint DELETE
    public ResponseEntity<?> eliminarPorId(@PathVariable int id) {
        return ResponseEntity.ok("Eliminado");
    }

    @DeleteMapping("/{nomPaciente}")// Endpoint DELETE
    public ResponseEntity<?> eliminarPorPaciente(@PathVariable String nomPaciente) {
        return ResponseEntity.ok("Eliminado");
    }



    @GetMapping("/nomPaciente/{nomPaciente}")// Endpoint GET por tipo
    public ResponseEntity<?> buscarPorPaciente(@PathVariable String nomPaciente) {
        try {
            return ResponseEntity.ok(consultaService.buscarPorPaciente(nomPaciente));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar por paciente: " + e.getMessage());
        }
    }


      @GetMapping("/fecha/{fecha}")// Endpoint GET por fecha
    public ResponseEntity<?> buscarPorFecha(@PathVariable String fecha) {
        try {
            return ResponseEntity.ok(consultaService.buscarPorFecha(fecha));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar por fecha: " + e.getMessage());
        }
    }

    @GetMapping("consulta/consulta")// Endpoint GET solo consulta
    public ResponseEntity<?> buscarPorConsulta(String motivoConsulta) {
        try {
            return ResponseEntity.ok(consultaService.buscarPorConsulta(motivoConsulta));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener legendarios: " + e.getMessage());
        }
    }
}