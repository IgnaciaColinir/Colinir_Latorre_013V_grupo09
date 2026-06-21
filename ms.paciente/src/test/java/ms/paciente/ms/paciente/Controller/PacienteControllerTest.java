package ms.paciente.ms.paciente.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ms.paciente.ms.paciente.Services.ServicesPaciente;
import ms.paciente.ms.paciente.dto.request.PacienteRequestDTO;
import ms.paciente.ms.paciente.dto.response.ContactoPacienteDTO;
import ms.paciente.ms.paciente.dto.response.PacienteResponseDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicesPaciente pacienteServices; // Sincronizado con el nombre exacto de tu controlador

    @Autowired
    private ObjectMapper objectMapper;

    private PacienteResponseDTO pacienteResponseMock;
    private PacienteRequestDTO pacienteRequestMock;
    private ContactoPacienteDTO contactoResponseMock;

    @BeforeEach
    void setUp() {
        // Datos simulados de salida
        pacienteResponseMock = new PacienteResponseDTO();
        pacienteResponseMock.setRut("12345678-9");
        pacienteResponseMock.setNombre("Ignacia");
        pacienteResponseMock.setApellido("Pérez");
        pacienteResponseMock.setDireccion("Av. Concha y Toro 1230");
        pacienteResponseMock.setFechaNacimiento(LocalDate.of(2000, 5, 20));
        pacienteResponseMock.setTelefono("+56912345678");
        pacienteResponseMock.setEmail("ignacia@example.com");
        pacienteResponseMock.setPrevision("FONASA");

        // Datos simulados de entrada
        pacienteRequestMock = new PacienteRequestDTO();
        pacienteRequestMock.setRut("12345678-9");
        pacienteRequestMock.setNombre("Ignacia");
        pacienteRequestMock.setApellido("Pérez");
        pacienteRequestMock.setFechaNacimiento(LocalDate.of(2000, 5, 20));
        pacienteRequestMock.setEmail("ignacia@example.com");
        pacienteRequestMock.setPrevision("FONASA");

        // Datos simulados de contacto básico
        contactoResponseMock = ContactoPacienteDTO.builder()
                .telefono("+56912345678")
                .email("ignacia@example.com")
                .build();
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(pacienteServices.obtenerTodos()).thenReturn(List.of(pacienteResponseMock));

        mockMvc.perform(get("/api/v1/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0].nombre").value("Ignacia"))
                .andExpect(jsonPath("$[0].prevision").value("FONASA"));
    }

    @Test
    public void testObtenerPorRut() throws Exception {
        String rutTest = "12345678-9";
        when(pacienteServices.obtenerPorRut(rutTest)).thenReturn(pacienteResponseMock);

        mockMvc.perform(get("/api/v1/pacientes/rut/" + rutTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Ignacia"));
    }

    @Test
    public void testObtenerPorPrevision() throws Exception {
        String previsionTest = "FONASA";
        when(pacienteServices.obtenerPorPrevision(previsionTest)).thenReturn(List.of(pacienteResponseMock));

        mockMvc.perform(get("/api/v1/pacientes/prevision/" + previsionTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prevision").value("FONASA"))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"));
    }

    @Test
    public void testGuardarPaciente() throws Exception {
        when(pacienteServices.registrarPaciente(any(PacienteRequestDTO.class))).thenReturn(pacienteResponseMock);

        mockMvc.perform(post("/api/v1/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteRequestMock)))
                .andExpect(status().isCreated()) // Valida el estado 201 Created de tu ResponseEntity
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Ignacia"));
    }

    @Test
    public void testActualizar() throws Exception {
        String rutTest = "12345678-9";
        when(pacienteServices.actualizar(eq(rutTest), any(PacienteRequestDTO.class))).thenReturn(pacienteResponseMock);

        mockMvc.perform(put("/api/v1/pacientes/" + rutTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteRequestMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    public void testEliminarExitoso() throws Exception {
        String rutTest = "12345678-9";
        when(pacienteServices.eliminar(rutTest)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/pacientes/" + rutTest))
                .andExpect(status().isOk())
                .andExpect(content().string("Eliminado con éxito"));
    }

    @Test
    public void testEliminarNoEncontrado() throws Exception {
        String rutTest = "99999999-9";
        when(pacienteServices.eliminar(rutTest)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/pacientes/" + rutTest))
                .andExpect(status().isNotFound()) // Valida el 404 de tu cláusula 'else'
                .andExpect(content().string("Eliminacion fallida, el RUT no coincide."));
    }

    @Test
    public void testObtenerDatosContacto() throws Exception {
        String rutTest = "12345678-9";
        when(pacienteServices.obtenerDatosContacto(rutTest)).thenReturn(contactoResponseMock);

        mockMvc.perform(get("/api/v1/pacientes/contact/" + rutTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefono").value("+56912345678"))
                .andExpect(jsonPath("$.email").value("ignacia@example.com"));
    }

}
