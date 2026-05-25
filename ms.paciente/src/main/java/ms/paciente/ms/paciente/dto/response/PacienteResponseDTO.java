package ms.paciente.ms.paciente.dto.response;


import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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