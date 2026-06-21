package ms.pagos.ms.pagos.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ms.pagos.ms.pagos.Services.ServicePago;
import ms.pagos.ms.pagos.dto.request.PagoRequestDTO;
import ms.pagos.ms.pagos.dto.response.PagoResponseDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(ControllerPago.class)
public class ControllerPagoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicePago pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PagoResponseDTO responseMock;
    private PagoRequestDTO requestMock;

    @BeforeEach
    void setUp() {
        // Mock de la respuesta DTO usando Builder (el response sí lo soporta)
        responseMock = PagoResponseDTO.builder()
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

        // 💡 Instanciación tradicional por Setters para evitar errores con el Builder
        requestMock = new PagoRequestDTO();
        requestMock.setIdConsulta(100);
        requestMock.setIdPaciente("12345678-9");
        requestMock.setValorConsulta(45000.0);
        requestMock.setValorTratamiento(15000.0);
        requestMock.setMetodoPago("DEBITO");
        requestMock.setEstado("Pagado");
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(pagoService.obtenerTodos()).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].montoTotal").value(60000.0))
                .andExpect(jsonPath("$[0].estado").value("Pagado"));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(pagoService.obtenerPorId(1)).thenReturn(responseMock);

        mockMvc.perform(get("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.metodoPago").value("DEBITO"));
    }

    @Test
    public void testGuardarPago() throws Exception {
        when(pagoService.guardarPago(any(PagoRequestDTO.class))).thenReturn(responseMock);

        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMock)))
                .andExpect(status().isCreated()) // Valida HTTP 201 Created
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.montoTotal").value(60000.0));
    }

    @Test
    public void testActualizar() throws Exception {
        when(pagoService.actualizar(eq(1), any(PagoRequestDTO.class))).thenReturn(responseMock);

        mockMvc.perform(put("/api/v1/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testEliminarExitoso() throws Exception {
        when(pagoService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado con éxito"));
    }

    @Test
    public void testEliminarNoEncontrado() throws Exception {
        when(pagoService.eliminar(99)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/pagos/99"))
                .andExpect(status().isNotFound()) // Valida HTTP 404
                .andExpect(content().string("No se borró nada porque el ID 99 no coincidió exactamente."));
    }

    @Test
    public void testBuscarPorConsulta() throws Exception {
        int idConsulta = 100;
        when(pagoService.buscarPorConsulta(idConsulta)).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v1/pagos/consulta/" + idConsulta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idConsulta").value(idConsulta));
    }


}
