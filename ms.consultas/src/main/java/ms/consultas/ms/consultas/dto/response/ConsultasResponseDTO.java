package ms.consultas.ms.consultas.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultasResponseDTO {

    private int id;
    private String nomPaciente;
    private String nomMedico;
    private String fechaConsulta;
    private String horaConsulta;
    private String motivoConsulta;
    private double valorConsulta;
}