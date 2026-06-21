package ms.pagos.ms.pagos.Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name= "pagos")
@Schema(name="ModeloPago", description = "Entidad que representa un registro fisico de un pago")
public class ModeloPago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del pago (Autoincremental)", example = "1")
    private int id; 

    @Column(name = "id_consulta", nullable = false) // 💡 Mapeo a snake_case y restricción física NOT NULL
    @Schema(description = "ID de la consulta médica asociada a este pago", example = "45")
    private int idConsulta;

    @Column(name = "id_paciente", nullable = false, length = 150)
    @Schema(description = "RUT o identificador único del paciente", example = "12345678-9")
    private String idPaciente; 

    @Column(name = "valor_consulta", nullable = false)
    @Schema(description = "Costo base de la consulta médica", example = "15000.0")
    private double valorConsulta;

    @Column(name = "valor_tratamiento", nullable = false)
    @Schema(description = "Costo adicional por tratamientos o insumos aplicados", example = "5000.0")
    private double valorTratamiento;

    @Column(name = "monto_total", nullable = false)
    @Schema(description = "Suma total pagada (valorConsulta + valorTratamiento)", example = "20000.0")
    private double montoTotal;

    @Column(name = "metodo_pago", nullable = false, length = 100)
    @Schema(description = "Método utilizado para efectuar el pago", allowableValues = {"EFECTIVO", "DEBITO", "CREDITO", "TRANSFERENCIA"}, example = "DEBITO")
    private String metodoPago;

    @Column(name = "estado", nullable = false, length = 50)
    @Schema(description = "Estado actual del flujo de pago", allowableValues = {"PENDIENTE", "PAGADO", "REEMBOLSADO"}, example = "PAGADO")
    private String estado;
    
    @Column(name = "fecha_pago", nullable = false)
    @Schema(description = "Fecha y hora exacta en la que se procesó la transacción", example = "2026-06-18T14:30:00")
    private LocalDateTime fechaPago;

}
