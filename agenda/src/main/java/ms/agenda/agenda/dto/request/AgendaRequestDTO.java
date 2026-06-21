package ms.agenda.agenda.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgendaRequestDTO {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "El campo fecha es obligatorio")
    @Column(name = "FECHA", nullable = false) // 💡 AGREGADO: Mapeo explícito de columna
    private LocalDate fecha;

    @NotNull(message = "El campo hora es obligatorio")
    @Column(name = "HORA", nullable = false)
    private LocalTime hora;

    @NotBlank(message = "El campo idProfesional es obligatorio")
    @Column(name = "ID_PROFESIONAL", nullable = false) // 💡 Buenas prácticas: Usar snake_case para Oracle
    private String idProfesional;

    @NotBlank(message = "El campo idPaciente es obligatorio")
    @Column(name = "ID_PACIENTE", nullable = false)
    private String idPaciente;

    @NotBlank(message = "El campo estado es obligatorio")
    @Column(name = "ESTADO", nullable = false)
    private String estado;
}
