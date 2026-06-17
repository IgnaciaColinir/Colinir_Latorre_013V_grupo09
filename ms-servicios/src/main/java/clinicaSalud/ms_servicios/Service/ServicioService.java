package clinicaSalud.ms_servicios.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import clinicaSalud.ms_servicios.DTO.ServicioDTO;
import clinicaSalud.ms_servicios.Model.Servicio;
import clinicaSalud.ms_servicios.Repository.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository repository;

    private ServicioDTO convertirADto(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setIdServicio(servicio.getIdServicio());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setPrecio(servicio.getPrecio());
        dto.setRequiereAyuno(servicio.isRequiereAyuno());
        return dto;
    }

    private Servicio convertirAModel(ServicioDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setIdServicio(dto.getIdServicio());
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setRequiereAyuno(dto.isRequiereAyuno());
        return servicio;
    }

    public List<ServicioDTO> listarTodo() {
        List<Servicio> servicios = repository.findAll();
        List<ServicioDTO> dtos = new ArrayList<>();
        for (Servicio s : servicios) {
            dtos.add(convertirADto(s));
        }
        return dtos;
    }

    public ServicioDTO guardar(ServicioDTO dto) {
        if (dto.getPrecio() <= 0) {
            throw new RuntimeException("Error: El precio del servicio médico debe ser mayor a $0.");
        }
        Servicio guardado = repository.save(convertirAModel(dto));
        return convertirADto(guardado);
    }

    public ServicioDTO obtenerPorId(Long id) {
        Optional<Servicio> servicio = repository.findById(id);
        if (servicio.isEmpty()) {
            throw new RuntimeException("Atención: El servicio con ID " + id + " no existe en nuestros registros.");
        }
        return convertirADto(servicio.get());
    }
}