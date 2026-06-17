package clinicaSalud.ms_profesionales;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import clinicaSalud.ms_profesionales.Controller.ProfesionalController;
import clinicaSalud.ms_profesionales.Security.JwtFilter;
import clinicaSalud.ms_profesionales.Security.JwtUtil;
import clinicaSalud.ms_profesionales.Services.ServicesProfesional;
import clinicaSalud.ms_profesionales.dto.request.ProfesionalRequestDTO;
import clinicaSalud.ms_profesionales.dto.response.ProfesionalResponseDTO;

@WebMvcTest(ProfesionalController.class)
@AutoConfigureMockMvc(addFilters = false) // JWT apagado
public class ProfesionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicesProfesional servicesProfesional;

    // Mockeamos la seguridad
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfesionalRequestDTO requestDTO;
    private ProfesionalResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ProfesionalRequestDTO();
        requestDTO.setRut("12345678-9");
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Perez");
        requestDTO.setEspecialidad("Cardiología");
        requestDTO.setEmail("juan@gmail.com");
        requestDTO.setTelefono("987654321");

        responseDTO = ProfesionalResponseDTO.builder()
                .rut("12345678-9")
                .nombre("Juan")
                .apellido("Perez")
                .especialidad("Cardiología")
                .email("juan@gmail.com")
                .telefono("987654321")
                .build();
    }

    @Test
    void guardarProfesional_RetornaCreated() throws Exception {
        when(servicesProfesional.guardar(any(ProfesionalRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/profesionales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.especialidad").value("Cardiología"));
    }
}