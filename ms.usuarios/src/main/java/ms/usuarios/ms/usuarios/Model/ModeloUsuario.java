package ms.usuarios.ms.usuarios.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de usuario para la creación y actualización de usuarios")
@Builder
public class ModeloUsuario {
    
    @Id
    @Column(name = "rut", nullable = false, length = 150)
    @Schema(description = "RUT del usuario, actúa como identificador único", example = "12345678-9")
    private String rut;

    @Column(name = "nombre", nullable = false, length = 255)
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 255)
    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Column(name = "email", nullable = false, unique = true, length = 150) // 💡 AGREGADO: unique = true asegura que no se repitan correos
    @Schema(description = "Correo electrónico del usuario, debe ser único", example = "juan.perez@example.com")
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    @Schema(description = "Contraseña encriptada del usuario", example = "$2a$10$v...password_hash")
    private String password;
    
    @Column(name = "cargo", nullable = false, length = 100) 
    // 💡 CORREGIDO: Se removió el @ManyToOne erróneo. Ahora es un campo de texto estructurado normal
    @Schema(description = "Rol o cargo asignado al usuario dentro del sistema", allowableValues = {"ADMINISTRADOR", "MEDICO", "RECEPCIONISTA"}, example = "MEDICO")
    private String cargo;

}
