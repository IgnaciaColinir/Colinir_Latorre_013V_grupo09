package clinicaSalud.ms_fichas.Service;

import clinicaSalud.ms_fichas.DTO.FichaDTO;
import clinicaSalud.ms_fichas.Model.Ficha;
import clinicaSalud.ms_fichas.Repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FichaService {

    @Autowired
    private FichaRepository repository;

    private FichaDTO convertirADto(Ficha ficha) {
        FichaDTO dto = new FichaDTO();
        dto.setIdFicha(ficha.getIdFicha());
        dto.setRutPaciente(ficha.getRutPaciente());
        dto.setTipoSangre(ficha.getTipoSangre());
        dto.setAlergias(ficha.getAlergias());
        dto.setAntecedentesFamiliares(ficha.getAntecedentesFamiliares());
        return dto;
    }

    private Ficha convertirAModel(FichaDTO dto) {
        Ficha ficha = new Ficha();
        ficha.setIdFicha(dto.getIdFicha());
        ficha.setRutPaciente(dto.getRutPaciente());
        ficha.setTipoSangre(dto.getTipoSangre());
        ficha.setAlergias(dto.getAlergias());
        ficha.setAntecedentesFamiliares(dto.getAntecedentesFamiliares());
        return ficha;
    }

    public FichaDTO guardar(FichaDTO dto) {
        // Regla de Negocio: Autocompletar campos críticos vacíos
        if (dto.getAlergias() == null || dto.getAlergias().isBlank()) {
            dto.setAlergias("Ninguna registrada");
        }
        
        Ficha fichaGuardada = repository.save(convertirAModel(dto));
        return convertirADto(fichaGuardada);
    }

    public List<FichaDTO> obtenerPorRut(String rut) {
        List<Ficha> fichas = repository.findByRutPaciente(rut);
        List<FichaDTO> dtos = new ArrayList<>();
        for (Ficha f : fichas) {
            dtos.add(convertirADto(f));
        }
        return dtos;
    }
}