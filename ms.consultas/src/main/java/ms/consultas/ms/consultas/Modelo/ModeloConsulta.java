package ms.consultas.ms.consultas.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Consultas")
@Schema(description = "Modelo de consulta que registra en el sistema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la consulta (Autoincremental)", example = "1")
    private Integer id;

    @Column(name = "id_paciente", nullable = false, length = 150) // 💡 AGREGADO: Restricción física en BD
    @Schema(description = "RUT o identificador único del paciente asociado a la consulta", example = "12345678-9")
    private String idPaciente;

    @Column(name = "nom_paciente", nullable = false, length = 255)
    @Schema(description = "Nombre completo del paciente en el momento de la consulta", example = "María Gómez")
    private String nomPaciente;
    
    @Column(name = "id_medico", nullable = false, length = 150)
    @Schema(description = "RUT o identificador único del médico asociado a la consulta", example = "98765432-1")
    private String idMedico;

    @Column(name = "nom_medico", nullable = false, length = 255)
    @Schema(description = "Nombre completo del médico que atiende la consulta", example = "Dr. Juan Pérez")
    private String nomMedico;
    
    @Column(name = "fecha_consulta", nullable = false)
    @Schema(description = "Fecha en la que se efectúa la consulta médica", example = "2026-10-10")
    private LocalDate fechaConsulta;

    @Column(name = "hora_consulta", nullable = false)
    @Schema(description = "Hora en la que se efectúa la consulta médica", example = "10:00")
    private LocalTime horaConsulta;

    @Column(name = "diagnostico", nullable = false, length = 500)
    @Schema(description = "Diagnóstico clínico emitido por el profesional sanitario", example = "Resfriado común")
    private String diagnostico;

    @Column(name = "valor_consulta", nullable = false)
    @Schema(description = "Costo base de la atención médica", example = "10000.0")
    private Double valorConsulta;

    @Column(name = "valor_treatment", nullable = false) // 'valorTratamiento' mapeado a la columna
    @Schema(description = "Costo adicional derivado de los procedimientos o tratamientos aplicados", example = "5000.0")
    private Double valorTratamiento;

    @Column(name = "estado", nullable = false, length = 50)
    @Schema(description = "Estado actual de la consulta", allowableValues = {"PENDIENTE", "FINALIZADA", "CANCELADA"}, example = "FINALIZADA")
    private String estado;

}
