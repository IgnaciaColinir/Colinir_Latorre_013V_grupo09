package clinicaSalud.ms_servicios.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_servicios.DTO.ServicioDTO;
import clinicaSalud.ms_servicios.Service.ServicioService;

// aca arreglé lo que dijo el profe de catalogo porque ahora ta el servicio y el DTO

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {

    @Autowired
    private ServicioService service; 

    @PostMapping
    public ResponseEntity<ServicioDTO> crear(@RequestBody ServicioDTO servicioDTO) {
        return ResponseEntity.ok(service.guardar(servicioDTO));
    }

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listar() {
        return ResponseEntity.ok(service.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}