package ms.paciente.ms.paciente.Model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
@Schema(description = "Modelo de paciente para la creación y actualización de pacientes")
@Builder
public class ModeloPaciente {
    @Id
    @Column(name = "rut", nullable = false, length = 150) // 💡 AGREGADO: Llave primaria física obligatoria
    @Schema(description = "RUT del paciente, actúa como identificador único", example = "12345678-9")
    private String rut;

    @Column(name = "nombre", nullable = false, length = 255)
    @Schema(description = "Nombre del paciente", example = "María")
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 255)
    @Schema(description = "Apellido del paciente", example = "Gómez")
    private String apellido;

    @Column(name = "direccion", nullable = false, length = 500)
    @Schema(description = "Dirección residencial del paciente", example = "Calle Principal 123")
    private String direccion;

    @Column(name = "fecha_nacimiento", nullable = false) // 💡 Snake case para la BD relacional
    @Schema(description = "Fecha de nacimiento del paciente", example = "1990-01-01")
    private LocalDate fechaNacimiento;

    @Column(name = "telefono", nullable = false, length = 50)
    @Schema(description = "Teléfono de contacto del paciente", example = "987654321")
    private String telefono;

    @Column(name = "email", nullable = false, length = 150)
    @Schema(description = "Correo electrónico del paciente", example = "maria.gomez@example.com")
    private String email;

    @Column(name = "prevision", nullable = false, length = 100)
    @Schema(description = "Previsión de salud del paciente (ej: Fonasa, Isapre, Particular)", example = "Fonasa")
    private String prevision;

    @Column(name = "rut_tutor", length = 150) 
    @Schema(description = "RUT del tutor del paciente (Opcional, aplica para menores de edad)", example = "98765432-1")
    private String rutTutor;

    @Column(name = "nombre_tutor", length = 255)
    @Schema(description = "Nombre completo del tutor del paciente (Opcional)", example = "Carlos Gómez")
    private String nombreTutor;
    

}
