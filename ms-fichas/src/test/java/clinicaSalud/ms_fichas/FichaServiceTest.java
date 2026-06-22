package clinicaSalud.ms_fichas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
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
        fichaModel.setTipoSangre("O+");
        fichaModel.setAlergias("Mani");
        fichaModel.setAntecedentesFamiliares("Nada");

        fichaDTO = new FichaDTO();
        fichaDTO.setIdFicha(1L);
        fichaDTO.setRutPaciente("123-4");
        fichaDTO.setTipoSangre("O+");
        fichaDTO.setAntecedentesFamiliares("Nada");
    }

    @Test
    void guardar_Exitoso_AutocompletaAlergias() {
        // GIVEN
        fichaDTO.setAlergias("");
        
        Ficha fichaGuardada = new Ficha();
        fichaGuardada.setAlergias("Ninguna registrada");
        
        // Simular que el FeignClient encuentra al paciente
        when(pacienteFeignClient.obtenerPacientePorRut("123-4")).thenReturn(new Object());
        when(repository.save(any(Ficha.class))).thenReturn(fichaGuardada);

        // WHEN
        FichaDTO resultado = fichaService.guardar(fichaDTO);

        // THEN
        assertEquals("Ninguna registrada", resultado.getAlergias());
        verify(pacienteFeignClient, times(1)).obtenerPacientePorRut("123-4");
        verify(repository, times(1)).save(any(Ficha.class));
    }

    @Test
    void guardar_Falla_PacienteNoExiste() {
        // GIVEN: El FeignClient tira un error o devuelve null
        when(pacienteFeignClient.obtenerPacientePorRut("123-4")).thenThrow(new RuntimeException("Error 404"));

        // WHEN & THEN
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
            fichaService.guardar(fichaDTO);
        });

        assertTrue(error.getMessage().contains("No se puede crear la ficha sin validar el RUT."));
        verify(repository, never()).save(any()); // Verificamos que jamás guarde en la BD
    }

    @Test
    void obtenerPorRut_Exitoso_ConFeign() {
        // GIVEN
        when(repository.findByRutPaciente("123-4")).thenReturn(List.of(fichaModel));
        when(pacienteFeignClient.obtenerPacientePorRut("123-4")).thenReturn("DatosPacienteFalsos");

        // WHEN
        List<FichaDTO> resultado = fichaService.obtenerPorRut("123-4");

        // THEN
        assertFalse(resultado.isEmpty());
        assertEquals("DatosPacienteFalsos", resultado.get(0).getPaciente());
    }
}