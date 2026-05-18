package clinicaSalud.ms_inventario.DTO;

import lombok.Data;

@Data
public class InsumoDTO {
    private Long idInsumo;
    private String nombre;
    private String categoria;
    private int stockActual;
    private int stockMinimo;
}