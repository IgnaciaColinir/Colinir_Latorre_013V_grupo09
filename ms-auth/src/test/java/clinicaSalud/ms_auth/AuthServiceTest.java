package clinicaSalud.ms_auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import clinicaSalud.ms_auth.Dto.AuthRequest;
import clinicaSalud.ms_auth.Model.Usuario;
import clinicaSalud.ms_auth.Repository.UsuarioRepository;
import clinicaSalud.ms_auth.Security.JwtUtil;
import clinicaSalud.ms_auth.Service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private AuthRequest request;
    private Usuario usuarioDB;

    @BeforeEach
    void setUp() {
        request = new AuthRequest();
        request.setUsername("test");
        request.setPassword("1234");

        usuarioDB = new Usuario();
        usuarioDB.setId(1L);
        usuarioDB.setUsername("test");
        usuarioDB.setPassword("1234");
        usuarioDB.setRol("PACIENTE");
    }

    @Test
    void testLogin_Exitoso() {
        when(repository.findByUsername("test")).thenReturn(Optional.of(usuarioDB));
        when(jwtUtil.generateToken("test")).thenReturn("token_falso_123");

        String result = authService.login(request);

        assertEquals("token_falso_123", result);
        verify(repository, times(1)).findByUsername("test");
    }

    @Test
    void testLogin_Fallo_UsuarioNoExiste() {
        when(repository.findByUsername("invalido")).thenReturn(Optional.empty());
        request.setUsername("invalido");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(request);
        });

        assertEquals("Error: Credenciales incorrectas.", exception.getMessage());
    }

    @Test
    void testLogin_Fallo_PasswordIncorrecto() {
        when(repository.findByUsername("test")).thenReturn(Optional.of(usuarioDB));
        request.setPassword("claveMala");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(request);
        });

        assertEquals("Error: Credenciales incorrectas.", exception.getMessage());
    }

    @Test
    void testRegistrar_Exitoso() {
        when(repository.findByUsername("nuevo_user")).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).thenReturn(usuarioDB);

        request.setUsername("nuevo_user");
        String result = authService.registrar(request);

        assertEquals("Usuario registrado correctamente.", result);
        verify(repository, times(1)).save(any(Usuario.class));
    }
}