package clinicaSalud.ms_inventario;

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

import clinicaSalud.ms_inventario.DTO.InsumoDTO;
import clinicaSalud.ms_inventario.Model.Insumo;
import clinicaSalud.ms_inventario.Repository.InsumoRepository;
import clinicaSalud.ms_inventario.Service.InsumoService;

@ExtendWith(MockitoExtension.class)
public class InsumoServiceTest {

    @Mock
    private InsumoRepository repository;

    @InjectMocks
    private InsumoService insumoService;

    private Insumo insumoModel;
    private InsumoDTO insumoDTO;

    @BeforeEach
    void setUp() {
        insumoModel = new Insumo();
        insumoModel.setIdInsumo(1L);
        insumoModel.setNombre("Gasa");
        insumoModel.setCategoria("Insumo Médico");
        insumoModel.setStockActual(10);
        insumoModel.setStockMinimo(5);

        insumoDTO = new InsumoDTO();
        insumoDTO.setIdInsumo(1L);
        insumoDTO.setNombre("Gasa");
        insumoDTO.setCategoria("Insumo Médico");
        insumoDTO.setStockActual(10);
        insumoDTO.setStockMinimo(5);
    }

    @Test
    void obtenerTodos_RetornaLista() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(insumoModel));

        // WHEN
        List<InsumoDTO> resultado = insumoService.obtenerTodos();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gasa", resultado.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    void guardar_Exitoso() {
        // GIVEN: Stock válido mayor o igual a 0
        when(repository.save(any(Insumo.class))).thenReturn(insumoModel);

        // WHEN
        InsumoDTO resultado = insumoService.guardar(insumoDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Gasa", resultado.getNombre());
        verify(repository, times(1)).save(any(Insumo.class));
    }

    @Test
    void guardar_StockNegativo_LanzaExcepcion() {
        // GIVEN: Stock negativo (rompe la regla de negocio)
        insumoDTO.setStockActual(-5);

        // WHEN & THEN: Esperamos que lance IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            insumoService.guardar(insumoDTO);
        });
        
        assertEquals("Error: El stock de un insumo no puede ser menor a 0.", exception.getMessage());
        verify(repository, never()).save(any(Insumo.class)); // Verificamos que jamás llegó a guardarse
    }
}