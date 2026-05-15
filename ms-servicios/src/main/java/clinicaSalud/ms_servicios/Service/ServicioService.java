package clinicaSalud.ms_servicios.Service;

import clinicaSalud.ms_servicios.Model.Servicio;
import clinicaSalud.ms_servicios.Repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository repository;

    public List<Servicio> listarTodo() {
        return repository.findAll();
    }

    public Servicio guardar(Servicio servicio) {
        // Regla de negocio 1: El precio de un servicio no puede ser negativo ni gratis
        if (servicio.getPrecio() <= 0) {
            throw new RuntimeException("Error: El precio del servicio médico debe ser mayor a $0.");
        }
        return repository.save(servicio);
    }

    public Servicio obtenerPorId(Long id) {
        Optional<Servicio> servicio = repository.findById(id);
        // Regla de negocio 2 (La que pidió el profe)
        if (servicio.isEmpty()) {
            throw new RuntimeException("Atención: El servicio con ID " + id + " no existe en nuestros registros.");
        }
        return servicio.get();
    }
}