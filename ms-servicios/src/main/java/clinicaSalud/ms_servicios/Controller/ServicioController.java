package clinicaSalud.ms_servicios.Controller;

import clinicaSalud.ms_servicios.Model.Servicio;
import clinicaSalud.ms_servicios.Service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo") 
public class ServicioController {

    // Cambiamos el Repository por el Service
    @Autowired
    private ServicioService service; 

    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        return ResponseEntity.ok(service.guardar(servicio));
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> listar() {
        return ResponseEntity.ok(service.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}