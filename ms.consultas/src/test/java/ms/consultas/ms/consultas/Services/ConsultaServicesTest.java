package ms.consultas.ms.consultas.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ms.consultas.ms.consultas.Modelo.ModeloConsulta;
import ms.consultas.ms.consultas.Repository.ConsultaRepository;
import ms.consultas.ms.consultas.client.PacienteClient;
import ms.consultas.ms.consultas.client.ProfesionalClient;
import ms.consultas.ms.consultas.dto.request.ConsultasRequestDTO;
import ms.consultas.ms.consultas.dto.response.ConsultasResponseDTO;
import ms.consultas.ms.consultas.dto.response.PacienteResponse;
import ms.consultas.ms.consultas.dto.response.ProfesionalResponse;

@SpringBootTest
@ActiveProfiles("test")
public class ConsultaServicesTest {
    @Autowired
    private ConsultaService consultaService;

    @MockitoBean
    private ConsultaRepository consultaRepository;

    @MockitoBean
    private PacienteClient pacienteClient;

    @MockitoBean
    private ProfesionalClient profesionalClient;

    private ModeloConsulta consultaMock;
    private ConsultasRequestDTO requestMock;
    private PacienteResponse pacienteResponseMock;
    private ProfesionalResponse profesionalResponseMock;

    @BeforeEach
    void setUp() {
        // Mock de la Entidad de la Base de Datos
        consultaMock = new ModeloConsulta();
        consultaMock.setId(1);
        consultaMock.setIdPaciente("12345678-9");
        consultaMock.setNomPaciente("Kevyn Silva");
        consultaMock.setIdMedico("98765432-1");
        consultaMock.setNomMedico("Ignacia Pérez");
        consultaMock.setFechaConsulta(LocalDate.of(2026, 7, 10));
        consultaMock.setHoraConsulta(LocalTime.of(14, 0));
        consultaMock.setDiagnostico("Control General");
        consultaMock.setValorConsulta(45000.0);
        consultaMock.setValorTratamiento(0.0);

        // Mock del DTO de entrada (Request)
        requestMock = new ConsultasRequestDTO();
        requestMock.setIdPaciente("12345678-9");
        requestMock.setIdMedico("98765432-1");
        requestMock.setFechaConsulta(LocalDate.of(2026, 7, 10));
        requestMock.setHoraConsulta(LocalTime.of(14, 0));
        requestMock.setDiagnostico("Control General");
        requestMock.setValorConsulta(45000);
        requestMock.setValorTratamiento(0);

        // Mock de las respuestas de OpenFeign
        pacienteResponseMock = new PacienteResponse();
        pacienteResponseMock.setNombre("Kevyn");
        pacienteResponseMock.setApellido("Silva");

        profesionalResponseMock = new ProfesionalResponse();
        profesionalResponseMock.setNombre("Ignacia");
        profesionalResponseMock.setApellido("Pérez");
    }

    @Test
    public void testObtenerPorIdExitoso() {
        when(consultaRepository.findById(1)).thenReturn(Optional.of(consultaMock));

        ConsultasResponseDTO response = consultaService.obtenerPorId(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Kevyn Silva", response.getNomPaciente());
        assertEquals("Control General", response.getDiagnostico());
    }

    @Test
    public void testObtenerPorIdLanzaExceptionCuandoNoExiste() {
        when(consultaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultaService.obtenerPorId(99);
        });

        assertTrue(exception.getMessage().contains("Consulta con ID 99 no encontrada"));
    }

    @Test
    public void testGuardarConsultaExitoso() {
        // 1. Indicar que el horario no está ocupado en la BD
        when(consultaRepository.existsByFechaConsultaAndHoraConsultaAndIdMedico(
                requestMock.getFechaConsulta(), requestMock.getHoraConsulta(), requestMock.getIdMedico()))
                .thenReturn(false);

        // 2. Simular llamadas Feign estables
        when(pacienteClient.obtenerPacientePorRut("12345678-9")).thenReturn(pacienteResponseMock);
        when(profesionalClient.obtenerProfesionalPorRut("98765432-1")).thenReturn(profesionalResponseMock);

        // 3. Simular almacenamiento en JPA
        when(consultaRepository.save(any(ModeloConsulta.class))).thenReturn(consultaMock);

        ConsultasResponseDTO response = consultaService.guardar(requestMock);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Kevyn Silva", response.getNomPaciente());
        assertEquals("Ignacia Pérez", response.getNomMedico());
        verify(consultaRepository, times(1)).save(any(ModeloConsulta.class));
    }

    @Test
    public void testGuardarLanzaExceptionPorHorarioOcupado() {
        // Simular que ya hay un registro de este médico en esa misma hora y fecha
        when(consultaRepository.existsByFechaConsultaAndHoraConsultaAndIdMedico(
                requestMock.getFechaConsulta(), requestMock.getHoraConsulta(), requestMock.getIdMedico()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultaService.guardar(requestMock);
        });

        assertTrue(exception.getMessage().contains("Ya existe una consulta programada para esa fecha y hora."));
        verify(consultaRepository, never()).save(any(ModeloConsulta.class));
    }

    @Test
    public void testGuardarLanzaExceptionCuandoPacienteNoExiste() {
        when(consultaRepository.existsByFechaConsultaAndHoraConsultaAndIdMedico(any(), any(), anyString()))
                .thenReturn(false);

        // Forzar error simulado de OpenFeign al caerse el microservicio externo o no pillar el RUT
        when(pacienteClient.obtenerPacientePorRut("12345678-9"))
                .thenThrow(new RuntimeException("Connection Timeout"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultaService.guardar(requestMock);
        });

        assertTrue(exception.getMessage().contains("no existe o el servicio no está disponible"));
    }

    @Test
    public void testActualizarExitosoSinCambioDeHorario() {
        when(consultaRepository.findById(1)).thenReturn(Optional.of(consultaMock));
        when(consultaRepository.save(any(ModeloConsulta.class))).thenReturn(consultaMock);

        // El request mantiene fecha, hora y médico, por lo que se salta la query de ocupación
        ConsultasResponseDTO response = consultaService.actualizar(1, requestMock);

        assertNotNull(response);
        assertEquals(1, response.getId());
        verify(consultaRepository, never()).existsByFechaConsultaAndHoraConsultaAndIdMedico(any(), any(), any());
        verify(consultaRepository, times(1)).save(consultaMock);
    }

    @Test
    public void testEliminarRetornaTrueSiExiste() {
        when(consultaRepository.existsById(1)).thenReturn(true);
        doNothing().when(consultaRepository).deleteById(1);

        boolean eliminado = consultaService.eliminar(1);

        assertTrue(eliminado);
        verify(consultaRepository, times(1)).deleteById(1);
    }

    @Test
    public void testEliminarRetornaFalseSiNoExiste() {
        when(consultaRepository.existsById(99)).thenReturn(false);

        boolean eliminado = consultaService.eliminar(99);

        assertFalse(eliminado);
        verify(consultaRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testObtenerPorIdPacienteVacioLanzaException() {
        when(consultaRepository.findByIdPaciente("12345678-9")).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultaService.obtenerPorIdPaciente("12345678-9");
        });

        assertTrue(exception.getMessage().contains("No se encontraron consultas para el paciente con RUT"));
    }
}
