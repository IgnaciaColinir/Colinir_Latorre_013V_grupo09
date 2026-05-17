package ms.consultas.ms.consultas.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.consultas.ms.consultas.Modelo.ModeloConsulta;
import ms.consultas.ms.consultas.Repository.ConsultaRepository;
import ms.consultas.ms.consultas.dto.request.ConsultasRequestDTO;
import ms.consultas.ms.consultas.dto.response.ConsultasResponseDTO;

import java.util.List;


@Service // Marca esta clase como capa de lógica de negocio
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<ModeloConsulta> obtenerTodos() {
        try {
            return consultaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener consultas: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> obtenerPorId(int id) {
        try {
            return consultaRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar consulta: " + e.getMessage());
        }
    }

    public ConsultasResponseDTO guardar(ConsultasRequestDTO request) {

        ModeloConsulta consulta = ModeloConsulta.builder()
                .nomPaciente(request.getNomPaciente())
                .nomMedico(request.getNomMedico())
                .fechaConsulta(request.getFechaConsulta())
                .horaConsulta(request.getHoraConsulta())
                .motivoConsulta(request.getMotivoConsulta())
                .valorConsulta(request.getValorConsulta())
                .build();

        ModeloConsulta guardado = consultaRepository.save(consulta);

        return ConsultasResponseDTO.builder()
                .id(guardado.getId())
                .nomPaciente(guardado.getNomPaciente())
                .nomMedico(guardado.getNomMedico())
                .fechaConsulta(guardado.getFechaConsulta())
                .horaConsulta(guardado.getHoraConsulta())
                .motivoConsulta(guardado.getMotivoConsulta())
                .valorConsulta(guardado.getValorConsulta())
                .build();
    }
    
    public ModeloConsulta actualizar(int id, ModeloConsulta consultaActualizada) {
        try {
            return consultaRepository.update(id, consultaActualizada);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar consulta: " + e.getMessage());
        }
    }

    public boolean eliminarbyId(int id) {
        try {
            return consultaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar consulta: " + e.getMessage());
        }
    }

    public boolean eliminarbyPaciente(String paciente) {
        try {
            return consultaRepository.deleteByPaciente(paciente);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar consulta: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> buscarPorPaciente(String nomPaciente) {
        try {
            return consultaRepository.findByPaciente(nomPaciente);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por paciente: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> buscarPorFecha(String fechaConsulta) {
        try {
            return consultaRepository.findByDate(fechaConsulta);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por fecha: " + e.getMessage());
        }
    }

    public List<ModeloConsulta> buscarPorConsulta(String motivoConsulta) {
        try {
            return consultaRepository.findByMotivoConsultas(motivoConsulta);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por motivo de consulta: " + e.getMessage());
        }
    }

    
}