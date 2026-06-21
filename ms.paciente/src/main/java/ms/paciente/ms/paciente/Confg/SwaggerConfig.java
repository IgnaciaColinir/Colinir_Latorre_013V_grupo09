package ms.paciente.ms.paciente.Confg;

// PASO 3: Clase de configuracion para personalizar la informacion general de la API en Swagger.
// La anotacion @Configuration le indica a Spring que esta clase contiene definiciones de beans.
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Este metodo define un bean de tipo OpenAPI que SpringDoc utilizara para generar la documentacion.
    // Aqui personalizamos el titulo, la version y la descripcion que apareceran en la cabecera de Swagger UI.
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pacientes")
                        .version("1.0")
                        .description("API para gestionar pacientes, incluyendo operaciones CRUD y autenticacion"));
    }
}
