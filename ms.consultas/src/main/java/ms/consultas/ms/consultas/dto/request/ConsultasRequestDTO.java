package ms.consultas.ms.consultas.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConsultasRequestDTO {

    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nomPaciente;

    @NotBlank(message = "El nombre del médico es obligatorio")
    private String nomMedico;
    
    @NotBlank(message = "La fecha de la consulta es obligatoria")
    private String fechaConsulta;

    @NotBlank(message = "La hora de la consulta es obligatoria")
    private String horaConsulta;

    @NotBlank(message = "El motivo de la consulta es obligatorio")
    private String motivoConsulta;
    @Min(value = 0, message = "El valor de la consulta no puede ser negativo")
    private double valorConsulta;
}