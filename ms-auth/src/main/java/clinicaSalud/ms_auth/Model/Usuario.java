package clinicaSalud.ms_auth.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Table("usuario") // Anotación de Spring Data JDBC, no de JPA
@Data
public class Usuario {
    @Id // Importante: org.springframework.data.annotation.Id
    private Long id;
    private String username;
    private String password;
    private String rol;
}