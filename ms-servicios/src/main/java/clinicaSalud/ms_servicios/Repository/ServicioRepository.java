package clinicaSalud.ms_servicios.Repository;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import clinicaSalud.ms_servicios.Model.Servicio;

@Repository
public interface ServicioRepository extends ListCrudRepository<Servicio, Long> {
    List<Servicio> findByRequiereAyuno(boolean requiereAyuno);
}