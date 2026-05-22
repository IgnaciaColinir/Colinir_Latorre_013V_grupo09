package ms.pagos.ms.pagos.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagoResponseDTO {

    private int id; 
    private int idConsulta;
    private String idPaciente; 
    private double valorConsulta;
    private double valorTratamiento;
    private double montoTotal;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;
}