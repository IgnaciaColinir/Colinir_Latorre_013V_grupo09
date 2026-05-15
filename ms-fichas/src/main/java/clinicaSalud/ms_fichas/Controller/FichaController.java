package clinicaSalud.ms_fichas.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinicaSalud.ms_fichas.Model.Ficha;
import clinicaSalud.ms_fichas.Repository.FichaRepository;

@RestController
@RequestMapping("/fichas")
public class FichaController {

    @Autowired
    private FichaRepository fichaRepository;

    // 1. Crear una nueva ficha médica
    @PostMapping
    public ResponseEntity<Ficha> crearFicha(@RequestBody Ficha ficha) {
        Ficha nuevaFicha = fichaRepository.save(ficha);
        return ResponseEntity.ok(nuevaFicha);
    }

    // 2. Buscar la ficha de un paciente específico por su RUT
    @GetMapping("/paciente/{rut}")
    public ResponseEntity<List<Ficha>> obtenerPorRut(@PathVariable String rut) {
        List<Ficha> fichas = fichaRepository.findByRutPaciente(rut);
        if(fichas.isEmpty()) {
            return ResponseEntity.notFound().build(); // Devuelve error 404 si no pilla nada
        }
        return ResponseEntity.ok(fichas);
    }
}