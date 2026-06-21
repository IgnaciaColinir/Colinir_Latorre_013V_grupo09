package ms.consultas.ms.consultas.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultasResponseDTO {

    private int id;
    private String idPaciente;
    private String nomPaciente;
    private String idMedico;
    private String nomMedico;

    private LocalDate fechaConsulta;
    private LocalTime horaConsulta;
    
    private String diagnostico;
    private double valorConsulta;
    private double valorTratamiento;
    private String estado;
}