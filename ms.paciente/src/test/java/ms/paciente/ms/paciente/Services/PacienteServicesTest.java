package ms.paciente.ms.paciente.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ms.paciente.ms.paciente.Model.ModeloPaciente;
import ms.paciente.ms.paciente.Repository.RepositoryPacientes;
import ms.paciente.ms.paciente.dto.request.PacienteRequestDTO;
import ms.paciente.ms.paciente.dto.response.ContactoPacienteDTO;
import ms.paciente.ms.paciente.dto.response.PacienteResponseDTO;

@SpringBootTest
@ActiveProfiles("test")
public class PacienteServicesTest {
    @Autowired
    private ServicesPaciente servicesPaciente;

    @MockitoBean
    private RepositoryPacientes pancientesRepository; // Mapeado exacto de tu atributo typo ("pancientes")

    @Test
    public void testObtenerTodos() {
        // PASO 1 (Mock): Preparar entidad simulada en BD
        ModeloPaciente pacienteMock = ModeloPaciente.builder()
                .rut("12345678-9")
                .nombre("Ignacia")
                .apellido("Pérez")
                .direccion("Av. Concha y Toro 1230")
                .fechaNacimiento(LocalDate.of(2000, 5, 20))
                .telefono("+56912345678")
                .email("ignacia@example.com")
                .prevision("FONASA")
                .build();

        when(pancientesRepository.findAll()).thenReturn(List.of(pacienteMock));

        // PASO 2 (Ejecutar)
        List<PacienteResponseDTO> resultado = servicesPaciente.obtenerTodos();

        // PASO 3 (Verificar)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("12345678-9", resultado.get(0).getRut());
        assertEquals("Ignacia", resultado.get(0).getNombre());
    }

    @Test
    public void testObtenerPorRut() {
        String rutTest = "12345678-9";
        ModeloPaciente pacienteMock = ModeloPaciente.builder()
                .rut(rutTest)
                .nombre("Kevyn")
                .apellido("Gómez")
                .prevision("ISAPRE")
                .build();

        when(pancientesRepository.findById(rutTest)).thenReturn(Optional.of(pacienteMock));

        // PASO 2 (Ejecutar)
        PacienteResponseDTO found = servicesPaciente.obtenerPorRut(rutTest);

        // PASO 3 (Verificar)
        assertNotNull(found);
        assertEquals(rutTest, found.getRut());
        assertEquals("ISAPRE", found.getPrevision());
    }

    @Test
    public void testRegistrarPacienteAdultoExitoso() {
        // Objeto Request de entrada (Mayor de 15 años, no exige tutor)
        PacienteRequestDTO request = new PacienteRequestDTO();
        request.setRut("12345678-9");
        request.setNombre("Hanses");
        request.setApellido("Montilva");
        request.setFechaNacimiento(LocalDate.of(1995, 10, 15)); // Adulto
        request.setEmail("hanses@example.com");
        request.setPrevision("FONASA");

        ModeloPaciente pacienteGuardadoMock = ModeloPaciente.builder()
                .rut("12345678-9")
                .nombre("Hanses")
                .apellido("Montilva")
                .fechaNacimiento(LocalDate.of(1995, 10, 15))
                .email("hanses@example.com")
                .prevision("FONASA")
                .build();

        // No existe previamente duplicado
        when(pancientesRepository.findById(request.getRut())).thenReturn(Optional.empty());
        when(pancientesRepository.save(any(ModeloPaciente.class))).thenReturn(pacienteGuardadoMock);

        // PASO 2 (Ejecutar)
        PacienteResponseDTO resultado = servicesPaciente.registrarPaciente(request);

        // PASO 3 (Verificar)
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("FONASA", resultado.getPrevision());
    }

    @Test
    public void testRegistrarPacienteMenorDeEdadLanzaException() {
        // Request de un menor de 15 años sin datos de tutor
        PacienteRequestDTO request = new PacienteRequestDTO();
        request.setRut("22345678-K");
        request.setNombre("Pedrito");
        request.setApellido("González");
        request.setFechaNacimiento(LocalDate.now().minusYears(10)); // 10 años de edad
        request.setRutTutor(null); // Sin tutor en la petición
        request.setNombreTutor("");

        when(pancientesRepository.findById(request.getRut())).thenReturn(Optional.empty());

        // PASO 2 y 3 (Ejecutar y Verificar excepción de negocio)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicesPaciente.registrarPaciente(request);
        });

        assertTrue(exception.getMessage().contains("se requiere un tutor responsable"));
    }

    @Test
    public void testEliminar() {
        String rutEliminar = "12345678-9";

        when(pancientesRepository.existsById(rutEliminar)).thenReturn(true);
        doNothing().when(pancientesRepository).deleteById(rutEliminar);

        // PASO 2 (Ejecutar)
        boolean eliminado = servicesPaciente.eliminar(rutEliminar);

        // PASO 3 (Verificar)
        assertTrue(eliminado);
        verify(pancientesRepository, times(1)).deleteById(rutEliminar);
    }

    @Test
    public void testObtenerDatosContacto() {
        String rutTest = "12345678-9";
        ModeloPaciente pacienteMock = ModeloPaciente.builder()
                .rut(rutTest)
                .telefono("+56999999999")
                .email("contacto@clinica.cl")
                .build();

        when(pancientesRepository.findById(rutTest)).thenReturn(Optional.of(pacienteMock));

        // PASO 2 (Ejecutar método específico de contacto)
        ContactoPacienteDTO contacto = servicesPaciente.obtenerDatosContacto(rutTest);

        // PASO 3 (Verificar)
        assertNotNull(contacto);
        assertEquals("+56999999999", contacto.getTelefono());
        assertEquals("contacto@clinica.cl", contacto.getEmail());
    }
}
