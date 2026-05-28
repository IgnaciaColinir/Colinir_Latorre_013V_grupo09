package ms.pagos.ms.pagos.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultasResponse {

    private int id;
    private String nomPaciente;
    private String nomMedico;
    private LocalDate fechaConsulta;
    private LocalTime horaConsulta;
    private String diagnostico;
    private double valorConsulta;
    private double valorTratamiento;
}