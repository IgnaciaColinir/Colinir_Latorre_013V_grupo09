package ms.paciente.ms.paciente.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PacienteResponseDTO {

    private String tratamiento;
    private String rut;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;
    private double valorTratamiento;
    private String prevision;


}