package clinicaSalud.ms_fichas.Controller;

import clinicaSalud.ms_fichas.DTO.FichaDTO;
import clinicaSalud.ms_fichas.Service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fichas")
public class FichaController {

    @Autowired
    private FichaService service;

    @PostMapping
    public ResponseEntity<FichaDTO> crearFicha(@RequestBody FichaDTO fichaDTO) {
        return ResponseEntity.ok(service.guardar(fichaDTO));
    }

    @GetMapping("/paciente/{rut}")
    public ResponseEntity<List<FichaDTO>> obtenerPorRut(@PathVariable String rut) {
        List<FichaDTO> fichas = service.obtenerPorRut(rut);
        if (fichas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fichas);
    }
}