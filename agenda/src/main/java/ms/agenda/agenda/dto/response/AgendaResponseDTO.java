package ms.agenda.agenda.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgendaResponseDTO {

    private int id;
    private LocalDate fecha;
    private LocalTime hora;
    private String idProfesional;
    private String idPaciente;
    private String estado;
}