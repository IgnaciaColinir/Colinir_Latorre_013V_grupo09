package clinicaSalud.ms_fichas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import clinicaSalud.ms_fichas.Controller.FichaController;
import clinicaSalud.ms_fichas.DTO.FichaDTO;
import clinicaSalud.ms_fichas.Security.JwtFilter;
import clinicaSalud.ms_fichas.Security.JwtUtil;
import clinicaSalud.ms_fichas.Service.FichaService;

@WebMvcTest(FichaController.class)
@AutoConfigureMockMvc(addFilters = false) // Apagamos el filtro JWT para el test
public class FichaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FichaService fichaService;

    // Tenemos que Mockear estos dos para que el contexto de Spring Security no explote
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private FichaDTO dto;

    @BeforeEach
    void setUp() {
        dto = new FichaDTO();
        dto.setRutPaciente("123-4");
    }

    @Test
    void crearFicha_RetornaOk() throws Exception {
        when(fichaService.guardar(any(FichaDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/fichas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorRut_Encontrado_RetornaOk() throws Exception {
        when(fichaService.obtenerPorRut(anyString())).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/fichas/paciente/123-4"))
                .andExpect(status().isOk());
    }
}