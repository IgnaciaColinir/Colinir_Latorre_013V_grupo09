package ms.agenda.agenda.Model;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name= "Agenda")
public class ModelAgenda {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la cita (Autoincremental)", example = "1")
    private Integer id;

    @Column(name = "fecha", nullable = false)
    @Schema(description = "Fecha programada para la cita médica", example = "2026-08-15")
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    @Schema(description = "Hora programada para la cita médica", example = "10:30")
    private LocalTime hora;

    @Column(name = "idProfesional", nullable = false, length = 150)
    @Schema(description = "RUT o identificador único del profesional médico", example = "11111111-1")
    private String idProfesional;

    @Column(name = "idPaciente", nullable = false, length = 150)
    @Schema(description = "RUT o identificador único del paciente", example = "22222222-2")
    private String idPaciente;

    @Column(name = "estado", nullable = false, length = 50)
    @Schema(description = "Estado actual de la cita", allowableValues = {"RESERVADA", "CONFIRMADA", "CANCELADA", "ATENDIDA"}, example = "RESERVADA")
    private String estado;

}
