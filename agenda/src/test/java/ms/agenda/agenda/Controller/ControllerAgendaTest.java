package ms.agenda.agenda.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ms.agenda.agenda.Services.ServiceAgenda;
import ms.agenda.agenda.dto.request.AgendaRequestDTO;
import ms.agenda.agenda.dto.response.AgendaResponseDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(ControllerAgenda.class)
public class ControllerAgendaTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceAgenda agendaService; // Nombre del atributo sincronizado

    @Autowired
    private ObjectMapper objectMapper;

    private AgendaResponseDTO agendaResponseMock;
    private AgendaRequestDTO agendaRequestMock;

    @BeforeEach
    void setUp() {
        // Inicializar DTO de salida
        agendaResponseMock = AgendaResponseDTO.builder()
                .id(1)
                .fecha(LocalDate.of(2026, 8, 20))
                .hora(LocalTime.of(15, 30))
                .idProfesional("11111111-1")
                .idPaciente("22222222-2")
                .estado("RESERVADA")
                .build();

        // Inicializar DTO de entrada
        agendaRequestMock = new AgendaRequestDTO();
        agendaRequestMock.setId(1);
        agendaRequestMock.setFecha(LocalDate.of(2026, 8, 20));
        agendaRequestMock.setHora(LocalTime.of(15, 30));
        agendaRequestMock.setIdProfesional("11111111-1");
        agendaRequestMock.setIdPaciente("22222222-2");
        agendaRequestMock.setEstado("RESERVADA");
    }

    @Test
    public void testProbarSiEstaVivo() throws Exception {
        mockMvc.perform(get("/api/v1/agenda/test-vivo"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡SÍ, EL SERVIDOR NUEVO ESTÁ CORRIENDO AQUÍ!"));
    }

    @Test
    public void testObtenerTodas() throws Exception {
        when(agendaService.obtenerTodas()).thenReturn(List.of(agendaResponseMock));

        mockMvc.perform(get("/api/v1/agenda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value("RESERVADA"))
                .andExpect(jsonPath("$[0].idPaciente").value("22222222-2"));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        int idTest = 1;
        when(agendaService.obtenerPorId(idTest)).thenReturn(agendaResponseMock);

        mockMvc.perform(get("/api/v1/agenda/" + idTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idProfesional").value("11111111-1"));
    }

    @Test
    public void testGuardarCita() throws Exception {
        when(agendaService.guardarCita(any(AgendaRequestDTO.class))).thenReturn(agendaResponseMock);

        mockMvc.perform(post("/api/v1/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequestMock)))
                .andExpect(status().isCreated()) // Valida el HTTP 201 CREATED de tu método
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("RESERVADA"));
    }

    @Test
    public void testActualizar() throws Exception {
        int idTest = 1;
        when(agendaService.actualizar(eq(idTest), any(AgendaRequestDTO.class))).thenReturn(agendaResponseMock);

        mockMvc.perform(put("/api/v1/agenda/" + idTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequestMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testEliminarExitoso() throws Exception {
        int idTest = 1;
        when(agendaService.eliminar(idTest)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/agenda/" + idTest))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado de la lista"));
    }

    @Test
    public void testEliminarNoEncontrado() throws Exception {
        int idTest = 99;
        when(agendaService.eliminar(idTest)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/agenda/" + idTest))
                .andExpect(status().isNotFound()) // Valida el HTTP 404 NOT_FOUND del else
                .andExpect(content().string("No se pudo eliminar: El ID " + idTest + " no se encontró en la lista."));
    }

    @Test
    public void testBuscarPorPaciente() throws Exception {
        String rutPaciente = "22222222-2";
        when(agendaService.obtenerPorPaciente(rutPaciente)).thenReturn(List.of(agendaResponseMock));

        mockMvc.perform(get("/api/v1/agenda/paciente/" + rutPaciente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPaciente").value(rutPaciente));
    }

    @Test
    public void testCitasDisponibles() throws Exception {
        String rutProfesional = "11111111-1";
        LocalDate fechaConsultada = LocalDate.of(2026, 8, 20);

        when(agendaService.obtenerCitasDisponibles(rutProfesional, fechaConsultada)).thenReturn(List.of(agendaResponseMock));

        // 💡 En lugar de construir la URL a mano, pasamos los @RequestParam usando .param()
        mockMvc.perform(get("/api/v1/agenda/disponibilidad")
                        .param("idProfesional", rutProfesional)
                        .param("fecha", fechaConsultada.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProfesional").value(rutProfesional));
    }

}
