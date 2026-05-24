package clinicaSalud.ms_servicios.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Servicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;
    
    private String nombre; // Ej: Ecografia Abdominal, Hemograma,etc,etcetvc
    private String descripcion;
    private int precio; 
    private boolean requiereAyuno; // Pa meter algun booleano 
}