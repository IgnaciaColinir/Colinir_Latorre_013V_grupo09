package ms.pagos.ms.pagos.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ms.pagos.ms.pagos.Client.ConsultaClient;
import ms.pagos.ms.pagos.Client.PacienteClient;
import ms.pagos.ms.pagos.Modelo.ModeloPago;
import ms.pagos.ms.pagos.Repository.RepositoryPago;
import ms.pagos.ms.pagos.dto.request.PagoRequestDTO;
import ms.pagos.ms.pagos.dto.response.ConsultasResponse;
import ms.pagos.ms.pagos.dto.response.PacienteResponse;
import ms.pagos.ms.pagos.dto.response.PagoResponseDTO;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesPagoTest {
    @Autowired
    private ServicePago servicePago;

    @MockitoBean
    private RepositoryPago pagoRepository;

    @MockitoBean
    private PacienteClient pacienteClient;

    @MockitoBean
    private ConsultaClient consultaClient;

    private ModeloPago pagoMock;
    private PagoRequestDTO requestMock;
    private PacienteResponse pacienteResponseMock;
    private ConsultasResponse consultaResponseMock;

    @BeforeEach
    void setUp() {
        // Mock del Modelo de Base de Datos (usando el Builder de Lombok que tiene tu código)
        pagoMock = ModeloPago.builder()
                .id(1)
                .idConsulta(100)
                .idPaciente("12345678-9")
                .valorConsulta(45000.0)
                .valorTratamiento(15000.0)
                .montoTotal(60000.0)
                .metodoPago("DEBITO")
                .estado("Pagado")
                .fechaPago(LocalDateTime.now())
                .build();

        // Mock del Request DTO
        requestMock = PagoRequestDTO.builder()
                .idConsulta(100)
                .idPaciente("12345678-9")
                .valorConsulta(45000.0)
                .valorTratamiento(15000.0)
                .metodoPago("DEBITO")
                .estado("Pagado")
                .build();

        // Mocks de Clientes Feign
        pacienteResponseMock = new PacienteResponse(); // Ajustar atributos si tiene
        
        consultaResponseMock = new ConsultasResponse();
        consultaResponseMock.setId(100);
        consultaResponseMock.setValorConsulta(45000.0);
        consultaResponseMock.setValorTratamiento(15000.0);
    }

    @Test
    public void testObtenerPorIdExitoso() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pagoMock));

        PagoResponseDTO response = servicePago.obtenerPorId(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(60000.0, response.getMontoTotal());
    }

    @Test
    public void testObtenerPorIdLanzaExceptionCuandoNoExiste() {
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicePago.obtenerPorId(99);
        });

        assertTrue(exception.getMessage().contains("Pago con ID 99 no encontrado"));
    }

    @Test
    public void testGuardarPagoExitoso() {
        // 1. Simular validaciones de Clientes externos (OpenFeign)
        when(pacienteClient.obtenerPacientePorRut("12345678-9")).thenReturn(pacienteResponseMock);
        when(consultaClient.obtenerConsultaPorid(100)).thenReturn(List.of(consultaResponseMock));
        
        // 2. Simular que no hay pagos previos para esta consulta
        when(pagoRepository.findByIdConsulta(100)).thenReturn(new ArrayList<>());
        
        // 3. Simular guardado persistente
        when(pagoRepository.save(any(ModeloPago.class))).thenReturn(pagoMock);

        PagoResponseDTO response = servicePago.guardarPago(requestMock);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Pagado", response.getEstado());
        assertEquals(60000.0, response.getMontoTotal());
        verify(pagoRepository, times(1)).save(any(ModeloPago.class));
    }

    @Test
    public void testGuardarPagoLanzaExceptionCuandoPacienteNoExiste() {
        when(pacienteClient.obtenerPacientePorRut("12345678-9")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicePago.guardarPago(requestMock);
        });

        assertEquals("El paciente no existe", exception.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    public void testGuardarPagoLanzaExceptionCuandoConsultaNoExiste() {
        when(pacienteClient.obtenerPacientePorRut("12345678-9")).thenReturn(pacienteResponseMock);
        when(consultaClient.obtenerConsultaPorid(100)).thenReturn(new ArrayList<>()); // Lista vacía

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicePago.guardarPago(requestMock);
        });

        assertEquals("No se puede pagar, la consulta no existe", exception.getMessage());
    }

    @Test
    public void testGuardarPagoLanzaExceptionPorInconsistenciaDeMontos() {
        when(pacienteClient.obtenerPacientePorRut("12345678-9")).thenReturn(pacienteResponseMock);
        
        // La consulta real cuesta 45000, pero cambiaremos el mock de la consulta externa para forzar el error
        consultaResponseMock.setValorConsulta(20000.0); 
        when(consultaClient.obtenerConsultaPorid(100)).thenReturn(List.of(consultaResponseMock));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicePago.guardarPago(requestMock);
        });

        assertTrue(exception.getMessage().contains("Inconsistencia, los valores no coinciden"));
    }

    @Test
    public void testGuardarPagoLanzaExceptionSiYaEstaPagado() {
        when(pacienteClient.obtenerPacientePorRut("12345678-9")).thenReturn(pacienteResponseMock);
        when(consultaClient.obtenerConsultaPorid(100)).thenReturn(List.of(consultaResponseMock));
        
        // Simular que findByIdConsulta ya trae un registro previo con estado "Pagado"
        when(pagoRepository.findByIdConsulta(100)).thenReturn(List.of(pagoMock));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicePago.guardarPago(requestMock);
        });

        assertTrue(exception.getMessage().contains("Ya existe un pago registrado e inmutable"));
    }

    @Test
    public void testGuardarPagoLanzaExceptionPorMetodoPagoInvalido() {
        when(pacienteClient.obtenerPacientePorRut("12345678-9")).thenReturn(pacienteResponseMock);
        when(consultaClient.obtenerConsultaPorid(100)).thenReturn(List.of(consultaResponseMock));
        when(pagoRepository.findByIdConsulta(100)).thenReturn(new ArrayList<>());

        // Modificamos el request con un método no aceptado (ej. "BITCOIN")
        requestMock.setMetodoPago("BITCOIN");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicePago.guardarPago(requestMock);
        });

        assertEquals("Metodo de pago no permitido por la clinica", exception.getMessage());
    }

    @Test
    public void testActualizarExitoso() {
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pagoMock));
        when(pagoRepository.save(any(ModeloPago.class))).thenReturn(pagoMock);

        requestMock.setMetodoPago("EFECTIVO");
        requestMock.setEstado("Modificado");

        PagoResponseDTO response = servicePago.actualizar(1, requestMock);

        assertNotNull(response);
        verify(pagoRepository, times(1)).save(pagoMock);
    }

    @Test
    public void testEliminarRetornaTrueSiExiste() {
        when(pagoRepository.existsById(1)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(1);

        boolean eliminado = servicePago.eliminar(1);

        assertTrue(eliminado);
        verify(pagoRepository, times(1)).deleteById(1);
    }

    @Test
    public void testEliminarRetornaFalseSiNoExiste() {
        when(pagoRepository.existsById(99)).thenReturn(false);

        boolean eliminado = servicePago.eliminar(99);

        assertFalse(eliminado);
        verify(pagoRepository, never()).deleteById(anyInt());
    }
}
