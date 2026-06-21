package clinicaSalud.ms_fichas.Service;

import clinicaSalud.ms_fichas.DTO.FichaDTO;
import clinicaSalud.ms_fichas.Model.Ficha;
import clinicaSalud.ms_fichas.Repository.FichaRepository;
import clinicaSalud.ms_fichas.feign.PacienteFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FichaService {

    @Autowired
    private FichaRepository repository;

    @Autowired
    private PacienteFeignClient pacienteFeignClient;

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
        // REGLA DE NEGOCIO CRÍTICA (Evaluación): Validar que el paciente exista
        try {
            Object pacienteValido = pacienteFeignClient.obtenerPacientePorRut(dto.getRutPaciente());
            if (pacienteValido == null) {
                throw new IllegalArgumentException("Error: El RUT ingresado no existe en el sistema de pacientes.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error conectando con el servicio de pacientes. No se puede crear la ficha sin validar el RUT.");
        }

        // Regla de Negocio 2: Autocompletar campos
        if (dto.getAlergias() == null || dto.getAlergias().isBlank()) {
            dto.setAlergias("Ninguna registrada");
        }
        
        Ficha fichaGuardada = repository.save(convertirAModel(dto));
        return convertirADto(fichaGuardada);
    }

    public List<FichaDTO> obtenerPorRut(String rut) {
        List<Ficha> fichas = repository.findByRutPaciente(rut);
        List<FichaDTO> dtos = new ArrayList<>();

        Object datosPaciente = null;
        try {
            datosPaciente = pacienteFeignClient.obtenerPacientePorRut(rut);
        } catch (Exception e) {
            System.out.println("Aviso: No se pudo conectar con ms-paciente para el RUT " + rut);
        }

        for (Ficha f : fichas) {
            FichaDTO dto = convertirADto(f);
            dto.setPaciente(datosPaciente);
            dtos.add(dto);
        }
        return dtos;
    }
}