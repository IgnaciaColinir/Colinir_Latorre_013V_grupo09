package ms.agenda.agenda.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ms.agenda.agenda.Model.ModelAgenda;
import ms.agenda.agenda.Repository.RepositoryAgenda;
import ms.agenda.agenda.client.PacienteClient;
import ms.agenda.agenda.client.ProfesionalClient;
import ms.agenda.agenda.dto.request.AgendaRequestDTO;
import ms.agenda.agenda.dto.response.AgendaResponseDTO;
import ms.agenda.agenda.dto.response.PacienteResponse;
import ms.agenda.agenda.dto.response.ProfesionalResponse;

@SpringBootTest
@ActiveProfiles("test")
public class AgendaServicesTest {
   @Autowired
    private ServiceAgenda serviceAgenda;

    @MockitoBean
    private RepositoryAgenda agendaRepository;

    @MockitoBean
    private PacienteClient pacienteClient;

    @MockitoBean
    private ProfesionalClient profesionalClient;

    private ModelAgenda agendaMock;
    private AgendaRequestDTO requestMock;
    private PacienteResponse pacienteResponseMock;
    private ProfesionalResponse profesionalResponseMock;

    @BeforeEach
    void setUp() {
        // Inicializar entidad simulada de la BD
        agendaMock = ModelAgenda.builder()
                .id(1)
                .fecha(LocalDate.now().plusDays(2)) // Cita para pasado mañana
                .hora(LocalTime.of(10, 0))
                .idProfesional("11111111-1")
                .idPaciente("22222222-2")
                .estado("RESERVADA")
                .build();

        // Inicializar DTO de entrada para guardar o actualizar
        requestMock = new AgendaRequestDTO();
        requestMock.setId(1);
        requestMock.setFecha(LocalDate.now().plusDays(2));
        requestMock.setHora(LocalTime.of(10, 0));
        requestMock.setIdProfesional("11111111-1");
        requestMock.setIdPaciente("22222222-2");
        requestMock.setEstado("RESERVADA");

        // Inicializar respuestas simuladas de OpenFeign
        pacienteResponseMock = new PacienteResponse(); // Ajusta los sets según los campos de tu clase real
        profesionalResponseMock = new ProfesionalResponse();
    }

    @Test
    public void testObtenerPorIdExitoso() {
        when(agendaRepository.findById(1)).thenReturn(Optional.of(agendaMock));

        AgendaResponseDTO response = serviceAgenda.obtenerPorId(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("RESERVADA", response.getEstado());
    }

    @Test
    public void testObtenerPorIdLanzaExceptionCuandoNoExiste() {
        when(agendaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceAgenda.obtenerPorId(99);
        });

        assertTrue(exception.getMessage().contains("Cita con ID 99 no encontrada"));
    }

    @Test
    public void testGuardarCitaExitoso() {
        // 1. Simular FeignClients respondiendo con éxito
        when(pacienteClient.obtenerPacientePorRut("22222222-2")).thenReturn(pacienteResponseMock);
        when(profesionalClient.obtenerProfesionalPorRut("11111111-1")).thenReturn(profesionalResponseMock);

        // 2. Simular que no hay conflictos de horarios en el mismo día
        when(agendaRepository.findByIdProfesionalAndFecha("11111111-1", requestMock.getFecha()))
                .thenReturn(new ArrayList<>()); // Lista vacía = Horario libre

        // 3. Simular guardado en base de datos
        when(agendaRepository.save(any(ModelAgenda.class))).thenReturn(agendaMock);

        // Ejecutar
        AgendaResponseDTO response = serviceAgenda.guardarCita(requestMock);

        // Verificar
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("RESERVADA", response.getEstado());
    }

    @Test
    public void testGuardarCitaLanzaExceptionPorFechaPasada() {
        // Cambiar la fecha de la petición a ayer
        requestMock.setFecha(LocalDate.now().minusDays(1));

        when(pacienteClient.obtenerPacientePorRut(anyString())).thenReturn(pacienteResponseMock);
        when(profesionalClient.obtenerProfesionalPorRut(anyString())).thenReturn(profesionalResponseMock);

        // Debe saltar el bloque de validación temporal "isBefore(LocalDateTime.now())"
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceAgenda.guardarCita(requestMock);
        });

        assertTrue(exception.getMessage().contains("No se puede agendar una cita en una fecha u hora pasada."));
    }

    @Test
    public void testGuardarCitaLanzaExceptionPorHorarioOcupado() {
        when(pacienteClient.obtenerPacientePorRut(anyString())).thenReturn(pacienteResponseMock);
        when(profesionalClient.obtenerProfesionalPorRut(anyString())).thenReturn(profesionalResponseMock);

        // Simular que ya existe otra cita reservada exactamente a las 10:00 AM para ese médico
        ModelAgenda citaConflictiva = ModelAgenda.builder()
                .id(2)
                .fecha(requestMock.getFecha())
                .hora(LocalTime.of(10, 0)) // Misma hora de la petición
                .idProfesional("11111111-1")
                .idPaciente("33333333-3")
                .build();

        when(agendaRepository.findByIdProfesionalAndFecha("11111111-1", requestMock.getFecha()))
                .thenReturn(List.of(citaConflictiva));

        // Debe saltar tu regla de negocio .anyMatch(cita -> cita.getHora().equals(horaCita))
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceAgenda.guardarCita(requestMock);
        });

        assertTrue(exception.getMessage().contains("El profesional ya tiene una cita reservada a esa hora"));
    }

    @Test
    public void testActualizarExitoso() {
        when(agendaRepository.findById(1)).thenReturn(Optional.of(agendaMock));
        when(agendaRepository.save(any(ModelAgenda.class))).thenReturn(agendaMock);

        requestMock.setEstado("CONFIRMADA");
        agendaMock.setEstado("CONFIRMADA"); // Reflejamos el cambio simulado

        AgendaResponseDTO response = serviceAgenda.actualizar(1, requestMock);

        assertNotNull(response);
        assertEquals("CONFIRMADA", response.getEstado());
    }

    @Test
    public void testEliminarRetornaTrueSiExiste() {
        when(agendaRepository.existsById(1)).thenReturn(true);
        doNothing().when(agendaRepository).deleteById(1);

        boolean resultado = serviceAgenda.eliminar(1);

        assertTrue(resultado);
        verify(agendaRepository, times(1)).deleteById(1);
    }

    @Test
    public void testEliminarRetornaFalseSiNoExiste() {
        when(agendaRepository.existsById(99)).thenReturn(false);

        boolean resultado = serviceAgenda.eliminar(99);

        assertFalse(resultado);
        verify(agendaRepository, never()).deleteById(99);
    } 
}
