package clinicaSalud.ms_inventario.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_inventario.Model.Insumo;
import clinicaSalud.ms_inventario.Repository.InsumoRepository;

@RestController
@RequestMapping("/inventario")
public class InsumoController {

    @Autowired
    private InsumoRepository insumoRepository;

    // 1. Endpoint para crear un nuevo insumo 
    @PostMapping
    public ResponseEntity<Insumo> crearInsumo(@RequestBody Insumo insumo) {
        Insumo nuevoInsumo = insumoRepository.save(insumo);
        return ResponseEntity.ok(nuevoInsumo);
    }

    // 2. Endpoint para ver todo el stock
    @GetMapping
    public ResponseEntity<List<Insumo>> obtenerTodos() {
        return ResponseEntity.ok(insumoRepository.findAll());
    }
}