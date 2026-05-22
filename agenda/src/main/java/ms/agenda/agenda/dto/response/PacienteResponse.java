package ms.agenda.agenda.dto.response;

import lombok.Data;

import java.time.LocalDate;

// DTO que representa la respuesta del microservicio de pacientes
@Data
public class PacienteResponse {

    private String tratamiento;
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