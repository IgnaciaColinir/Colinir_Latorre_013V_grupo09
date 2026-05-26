package ms.consultas.ms.consultas.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloConsulta {

    private Integer id;
    @NotBlank(message = "El rut del paciente es obligatorio")
    private String idPaciente;

    private String nomPaciente;
    
    private String idMedico;
    @NotBlank(message = "El nombre del médico es obligatorio")
    private String nomMedico;
    @NotNull(message = "La fecha de la consulta es obligatoria")
    private LocalDate fechaConsulta;
    @NotNull(message = "La hora de la consulta es obligatoria")
    private LocalTime horaConsulta;

    @NotBlank(message = "El diagnostico de la consulta es obligatorio")
    private String diagnostico;

    @Min(value = 0, message = "El valor de la consulta no puede ser negativo")
    private Double valorConsulta;

    @Min(value = 0, message = "El valor del tratamiento no puede ser negativo")
    private Double valorTratamiento;
    

}
