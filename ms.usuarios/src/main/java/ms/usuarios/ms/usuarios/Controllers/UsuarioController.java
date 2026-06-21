package ms.usuarios.ms.usuarios.Controllers;

import jakarta.validation.Valid;
import ms.usuarios.ms.usuarios.Services.ServicesUsuario;
import ms.usuarios.ms.usuarios.dto.request.UsuarioRequestDTO;
import ms.usuarios.ms.usuarios.dto.response.UsuarioResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@RestController // Indica que retorna JSON automáticamente
@RequestMapping("/api/v1/usuarios") // Ruta base del controlador
@Tag(name = "Usuarios", description = "Endpoints para gestionar usuarios, incluyendo operaciones CRUD y autenticacion")
public class UsuarioController {

    @Autowired
    private ServicesUsuario usuarioServices;


    @GetMapping // Endpoint GET para obtener todos
    @Operation(
        summary = "Obtener todos los usuarios", 
        description = "Retorna una lista de todos los usuarios registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de usuarios obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)
            )
        )
    )
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioServices.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }



/*---------------------------------------------*/

    @GetMapping("/rut/{rut}")
    @Operation(
        summary = "Obtener usuario por rut",
        description = "Retorna el usuario asociado al rut proporcionado"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario encontrado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioResponseDTO.class)
        )
    )
    public ResponseEntity<UsuarioResponseDTO> obtenerPorRut(@PathVariable String rut) {
            UsuarioResponseDTO usuario = usuarioServices.obtenerPorRut(rut);
            return ResponseEntity.ok(usuario);
    }




/*---------------------------------------------*/
    @PostMapping
    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Crea un nuevo usuario con la información proporcionada en el cuerpo de la solicitud"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Usuario creado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioResponseDTO.class)
        )
    )
    public ResponseEntity<UsuarioResponseDTO> guardar(
            @Valid @RequestBody UsuarioRequestDTO request
    ) {
        UsuarioResponseDTO nuevo = usuarioServices.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }


/*---------------------------------------------*/
    @PutMapping("/{rut}")// Endpoint PUT para actualizar
    @Operation(
        summary = "Actualizar un usuario",
        description = "Actualiza la información de un usuario existente"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuario actualizado exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioResponseDTO.class)
        )
    )
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable String rut, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO actualizado = usuarioServices.actualizar(rut, request);
            return ResponseEntity.ok(actualizado);
    }



    /*---------------------------------------------*/
    @DeleteMapping("/{rut}")// Endpoint DELETE
    @Operation(
        summary = "Eliminar un usuario por rut",
        description = "Elimina un usuario existente del sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario eliminado exitosamente",
            content = @Content(mediaType = "text/plain", 
            schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado en el sistema",
            content = @Content
        )
    })
    
    public ResponseEntity<String> eliminar(@PathVariable String rut) {
        boolean eliminado = usuarioServices.eliminar(rut);
    
        if (eliminado) {
            return ResponseEntity.ok("Eliminado con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con rut " + rut + " no encontrado");
                }
    }




    /*---------------------------------------------*/
    @GetMapping("/email/{email}")
    @Operation(
        summary = "Obtener usuario por email",
        description = "Retorna la información del usuario asociado al correo electrónico proporcionado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El email no corresponde a ningún usuario registrado",
            content = @Content
        )
    })
    public ResponseEntity<UsuarioResponseDTO> obtenerPorEmail(@PathVariable String email) {
        UsuarioResponseDTO usuario = usuarioServices.obtenerPorEmail(email);
        return ResponseEntity.ok(usuario);
    }


    @GetMapping("/cargo/{cargo}")
    @Operation(
        summary = "Obtener usuario por cargo",
        description = "Retorna la información del usuario asociado al cargo proporcionado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado con éxito",
            content = @Content(mediaType = "application/json", 
            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class))
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "El cargo no corresponde a ningún usuario registrado",
            content = @Content
        )
    })
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerPorCargo(@PathVariable String cargo) {
        List <UsuarioResponseDTO> usuarios = usuarioServices.obtenerPorCargo(cargo);
        return ResponseEntity.ok(usuarios);
    }

}