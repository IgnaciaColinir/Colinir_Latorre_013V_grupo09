package clinicaSalud.ms_inventario.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import clinicaSalud.ms_inventario.DTO.InsumoDTO;
import clinicaSalud.ms_inventario.Model.Insumo;
import clinicaSalud.ms_inventario.Repository.InsumoRepository;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository repository;

    private InsumoDTO convertirADTO(Insumo insumo) {
        InsumoDTO dto = new InsumoDTO();
        dto.setIdInsumo(insumo.getIdInsumo());
        dto.setNombre(insumo.getNombre());
        dto.setCategoria(insumo.getCategoria());
        dto.setStockActual(insumo.getStockActual());
        dto.setStockMinimo(insumo.getStockMinimo());
        return dto;
    }

    private Insumo convertirAModel(InsumoDTO dto) {
        Insumo insumo = new Insumo();
        insumo.setIdInsumo(dto.getIdInsumo());
        insumo.setNombre(dto.getNombre());
        insumo.setCategoria(dto.getCategoria());
        insumo.setStockActual(dto.getStockActual());
        insumo.setStockMinimo(dto.getStockMinimo());
        return insumo;
    }

    public List<InsumoDTO> obtenerTodos() {
        List<Insumo> insumos = repository.findAll();
        List<InsumoDTO> dtos = new ArrayList<>();
        for (Insumo i : insumos) {
            dtos.add(convertirADTO(i));
        }
        return dtos;
    }

    public InsumoDTO guardar(InsumoDTO dto) {
        // Regla de Negocio: El stock no puede ser negativo (Reforzado aquí además del DTO)
        if (dto.getStockActual() < 0 || dto.getStockMinimo() < 0) {
            throw new IllegalArgumentException("Error: El stock de un insumo no puede ser menor a 0.");
        }
        Insumo guardado = repository.save(convertirAModel(dto));
        return convertirADTO(guardado);
    }
}