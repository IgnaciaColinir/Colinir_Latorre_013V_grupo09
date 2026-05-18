package clinicaSalud.ms_servicios.DTO;

import lombok.Data;

@Data
public class ServicioDTO {
    private Long idServicio;
    private String nombre;
    private String descripcion;
    private int precio;
    private boolean requiereAyuno;
}