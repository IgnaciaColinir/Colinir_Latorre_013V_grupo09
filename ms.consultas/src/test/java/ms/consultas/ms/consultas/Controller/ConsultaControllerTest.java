package ms.consultas.ms.consultas.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ms.consultas.ms.consultas.Services.ConsultaService;
import ms.consultas.ms.consultas.dto.request.ConsultasRequestDTO;
import ms.consultas.ms.consultas.dto.response.ConsultasResponseDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(ConsultaController.class)
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock 
    private ConsultaService consultaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ConsultasResponseDTO responseMock;
    private ConsultasRequestDTO requestMock;

    @BeforeEach
    void setUp() {
        // Inicialización del DTO de salida esperado
        responseMock = new ConsultasResponseDTO();
        responseMock.setId(1);
        responseMock.setIdPaciente("12345678-9");
        responseMock.setNomPaciente("Kevyn Silva");
        responseMock.setIdMedico("98765432-1");
        responseMock.setNomMedico("Ignacia Pérez");
        responseMock.setFechaConsulta(LocalDate.of(2026, 7, 10));
        responseMock.setHoraConsulta(LocalTime.of(14, 0));
        responseMock.setDiagnostico("Control General");
        // 💡 Ajustados con decimales .0 para prevenir fallos si los tipos son Double
        responseMock.setValorConsulta(45000.0); 
        responseMock.setValorTratamiento(0.0);

        // Inicialización del DTO de entrada para peticiones POST/PUT
        requestMock = new ConsultasRequestDTO();
        requestMock.setIdPaciente("12345678-9");
        requestMock.setIdMedico("98765432-1");
        requestMock.setFechaConsulta(LocalDate.of(2026, 7, 10));
        requestMock.setHoraConsulta(LocalTime.of(14, 0));
        requestMock.setDiagnostico("Control General");
        requestMock.setValorConsulta(45000.0);
        requestMock.setValorTratamiento(0.0);
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(consultaService.obtenerTodos()).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v1/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomPaciente").value("Kevyn Silva"))
                .andExpect(jsonPath("$[0].diagnostico").value("Control General"));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(consultaService.obtenerPorId(1)).thenReturn(responseMock);

        mockMvc.perform(get("/api/v1/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomMedico").value("Ignacia Pérez"));
    }

    @Test
    public void testGuardarConsulta() throws Exception {
        when(consultaService.guardar(any(ConsultasRequestDTO.class))).thenReturn(responseMock);

        mockMvc.perform(post("/api/v1/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMock)))
                .andExpect(status().isCreated()) // Valida HTTP 201 CREATED
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idPaciente").value("12345678-9"));
    }

    @Test
    public void testActualizar() throws Exception {
        when(consultaService.actualizar(eq(1), any(ConsultasRequestDTO.class))).thenReturn(responseMock);

        mockMvc.perform(put("/api/v1/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testObtenerPorIdPacienteExitoso() throws Exception {
        String rutPaciente = "12345678-9";
        when(consultaService.obtenerPorIdPaciente(rutPaciente)).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v1/consultas/paciente/" + rutPaciente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPaciente").value(rutPaciente));
    }

    @Test
    public void testObtenerPorIdPacienteNoEncontrado() throws Exception {
        String rutInvalido = "99999999-9";
        // Simulamos la lista vacía que causa la evaluación del if() en tu controlador
        when(consultaService.obtenerPorIdPaciente(rutInvalido)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/consultas/paciente/" + rutInvalido))
                .andExpect(status().isNotFound()); // Valida HTTP 404
    }

    @Test
    public void testEliminarPorIdExitoso() throws Exception {
        when(consultaService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminada con éxito"));
    }

    @Test
    public void testEliminarPorIdNoEncontrado() throws Exception {
        when(consultaService.eliminar(99)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/consultas/99"))
                .andExpect(status().isNotFound()) // Valida HTTP 404 del else
                .andExpect(content().string("Consulta con id 99 no encontrado"));
    }

    @Test
    public void testObtenerPorDiagnostico() throws Exception {
        String diag = "Control General";
        when(consultaService.obtenerPorDiagnostico(diag)).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v1/consultas/diagnostico/" + diag))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diagnostico").value(diag));
    }

    @Test
    public void testObtenerPorEstado() throws Exception {
        String estado = "Atendido";
        when(consultaService.obtenerConsultasPorEstado(estado)).thenReturn(List.of(responseMock));

        mockMvc.perform(get("/api/v1/consultas/estado/" + estado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

}
