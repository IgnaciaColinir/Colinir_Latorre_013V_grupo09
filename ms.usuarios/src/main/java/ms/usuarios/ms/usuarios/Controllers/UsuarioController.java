package ms.usuarios.ms.usuarios.Controllers;

import jakarta.validation.Valid;
import ms.usuarios.ms.usuarios.Model.ModeloUsuario;
import ms.usuarios.ms.usuarios.Services.ServicesUsuario;
import ms.usuarios.ms.usuarios.dto.request.UsuarioRequestDTO;
import ms.usuarios.ms.usuarios.dto.response.UsuarioResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/usuarios") // Ruta base del controlador
public class UsuarioController {

@Autowired
private ServicesUsuario usuarioServices;


    @GetMapping // Endpoint GET para obtener todos
    public ResponseEntity<List<ModeloUsuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioServices.obtenerTodos());
    }

    //recuerden que la url aqui seria /api/v1/pokemones/1
   @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerPorEmail(@PathVariable String email) {
            List<ModeloUsuario> usuario = usuarioServices.obtenerPorEmail(email);
            return ResponseEntity.ok(usuario);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<?> obtenerPorRut(@PathVariable String rut) {
            List<ModeloUsuario> usuario = usuarioServices.obtenerPorRut(rut);
            return ResponseEntity.ok(usuario);
    }


    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> guardar(
            @Valid @RequestBody UsuarioRequestDTO request
    ) {
        UsuarioResponseDTO nuevo = usuarioServices.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }


    @PutMapping("/{rut}")// Endpoint PUT para actualizar
    public ResponseEntity<?> actualizar(@PathVariable String rut, @Valid @RequestBody ModeloUsuario usuario) {
        ModeloUsuario actualizado = usuarioServices.actualizar(rut, usuario);
            return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{rut}")// Endpoint DELETE
    public ResponseEntity<?> eliminar(@PathVariable String rut) {
        return ResponseEntity.ok("Eliminado");
    }

    @GetMapping("/cargo/{cargo}")// Endpoint GET por cargo
    public ResponseEntity<?> buscarPorCargo(@PathVariable String cargo) {
        try {
            return ResponseEntity.ok(usuarioServices.buscarPorCargo(cargo));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar por cargo: " + e.getMessage());
        }
    }

}