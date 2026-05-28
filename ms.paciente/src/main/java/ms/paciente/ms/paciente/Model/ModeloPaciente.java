package ms.paciente.ms.paciente.Model;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloPaciente {
    @NotBlank(message = "El rut del paciente no puede estar vacío")
    private String rut;
    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El apellido del paciente no puede estar vacío")
    private String apellido;
    @NotBlank(message = "La dirección del paciente no puede estar vacía")
    private String direccion;
    @NotNull(message = "La fecha de nacimiento del paciente no puede estar vacía")
    @Past(message = "La fecha de nacimiento del paciente debe ser una fecha pasada")
    private LocalDate fechaNacimiento;
    @NotBlank(message = "El teléfono del paciente no puede estar vacío")
    private String telefono;
    @NotBlank(message = "El email del paciente no puede estar vacío")
    @Email(message = "El email del paciente debe ser válido")
    private String email;
    @NotBlank(message = "La previsión del paciente no puede estar vacía")
    private String prevision;


    private String rutTutor;
    private String nombreTutor;
    

}
