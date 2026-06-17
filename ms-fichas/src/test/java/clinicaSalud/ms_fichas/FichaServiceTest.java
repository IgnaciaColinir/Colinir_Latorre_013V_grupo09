package clinicaSalud.ms_fichas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import clinicaSalud.ms_fichas.DTO.FichaDTO;
import clinicaSalud.ms_fichas.Model.Ficha;
import clinicaSalud.ms_fichas.Repository.FichaRepository;
import clinicaSalud.ms_fichas.Service.FichaService;
import clinicaSalud.ms_fichas.feign.PacienteFeignClient;

@ExtendWith(MockitoExtension.class)
public class FichaServiceTest {

    @Mock
    private FichaRepository repository;

    @Mock
    private PacienteFeignClient pacienteFeignClient;

    @InjectMocks
    private FichaService fichaService;

    private Ficha fichaModel;
    private FichaDTO fichaDTO;

    @BeforeEach
    void setUp() {
        fichaModel = new Ficha();
        fichaModel.setIdFicha(1L);
        fichaModel.setRutPaciente("123-4");
        fichaModel.setAlergias("Mani");

        fichaDTO = new FichaDTO();
        fichaDTO.setIdFicha(1L);
        fichaDTO.setRutPaciente("123-4");
    }

    @Test
    void guardar_Exitoso_AutocompletaAlergias() {
        // GIVEN: El DTO viene sin alergias
        fichaDTO.setAlergias("");
        
        // Configuramos el Mock para devolver el modelo con las alergias "por defecto"
        Ficha fichaGuardada = new Ficha();
        fichaGuardada.setAlergias("Ninguna registrada");
        when(repository.save(any(Ficha.class))).thenReturn(fichaGuardada);

        // WHEN
        FichaDTO resultado = fichaService.guardar(fichaDTO);

        // THEN: Verificamos que la regla de negocio de autocompletar funcionó
        assertEquals("Ninguna registrada", resultado.getAlergias());
        verify(repository, times(1)).save(any(Ficha.class));
    }

    @Test
    void obtenerPorRut_Exitoso_ConFeign() {
        // GIVEN
        when(repository.findByRutPaciente("123-4")).thenReturn(List.of(fichaModel));
        // Simulamos la respuesta del ms-paciente de la Nacha
        when(pacienteFeignClient.obtenerPacientePorRut("123-4")).thenReturn("DatosPacienteFalsos");

        // WHEN
        List<FichaDTO> resultado = fichaService.obtenerPorRut("123-4");

        // THEN
        assertFalse(resultado.isEmpty());
        assertEquals("DatosPacienteFalsos", resultado.get(0).getPaciente());
        verify(pacienteFeignClient, times(1)).obtenerPacientePorRut("123-4");
    }
}