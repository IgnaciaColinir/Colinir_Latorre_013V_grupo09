package ms.consultas.ms.consultas.Modelo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloConsulta {

    private int id;
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
