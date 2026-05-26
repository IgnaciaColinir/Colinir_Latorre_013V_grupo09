package ms.consultas.ms.consultas.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultasResponseDTO {

    private int id;
    private String nomPaciente;
    private String nomMedico;

    private LocalDate fechaConsulta;
    private LocalTime horaConsulta;
    
    private String diagnostico;
    private double valorConsulta;
    private double valorTratamiento;
}