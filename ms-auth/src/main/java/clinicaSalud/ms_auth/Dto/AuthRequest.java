package clinicaSalud.ms_auth.Dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}