package clinicaSalud.ms_profesionales.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profesionales")
public class ModeloProfesional {
    
    @Id
    @NotBlank(message = "El rut no puede estar vacío")
    private String rut;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "La especialidad no puede estar vacía")
    private String especialidad;

    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
}