package clinicaSalud.ms_inventario.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Inventario Clínico")
                        .version("1.0")
                        .description("Microservicio encargado de gestionar el stock y las categorías de los insumos médicos."));
    }
}