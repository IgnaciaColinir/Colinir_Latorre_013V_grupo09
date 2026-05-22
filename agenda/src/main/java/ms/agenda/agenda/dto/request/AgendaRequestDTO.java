package ms.agenda.agenda.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgendaRequestDTO {

    private int id;
    @NotBlank(message = "El campo fecha es obligatorio")
    private LocalDate fecha;
    @NotBlank(message = "El campo hora es obligatorio")
    private LocalTime hora;
    @NotBlank(message = "El campo idProfesional es obligatorio")
    private String idProfesional;
    @NotBlank(message = "El campo idPaciente es obligatorio")
    private String idPaciente;
    @NotBlank(message = "El campo estado es obligatorio")
    private String estado;
}