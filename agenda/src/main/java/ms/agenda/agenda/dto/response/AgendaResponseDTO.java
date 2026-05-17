package ms.agenda.agenda.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgendaResponseDTO {

    private int id;
    private String fecha;
    private String hora;
    private String idProfesional;
    private String idPaciente;
    private String estado;
}