package clinicaSalud.ms_inventario.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_inventario.DTO.InsumoDTO;
import clinicaSalud.ms_inventario.Service.InsumoService;

@RestController
@RequestMapping("/api/v1/insumos") 
public class InsumoController {

    @Autowired
    private InsumoService service;

    @PostMapping
    public ResponseEntity<InsumoDTO> crearInsumo(@RequestBody InsumoDTO insumoDTO) {
        return ResponseEntity.ok(service.guardar(insumoDTO));
    }

    @GetMapping
    public ResponseEntity<List<InsumoDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }
}