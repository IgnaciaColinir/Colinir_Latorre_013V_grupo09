package ms.paciente.ms.paciente.dto.response;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PacienteResponseDTO {

    private String rut;
    private String nombre;
    private String apellido;
    private String direccion;
    LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String prevision;
    private String rutTutor;
    private String nombreTutor;

}