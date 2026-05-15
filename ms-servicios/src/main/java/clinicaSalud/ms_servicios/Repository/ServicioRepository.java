package clinicaSalud.ms_servicios.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinicaSalud.ms_servicios.Model.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    // Para filtrar servicios que requieren ayuno por ejemplo. 
    List<Servicio> findByRequiereAyuno(boolean requiereAyuno);
}