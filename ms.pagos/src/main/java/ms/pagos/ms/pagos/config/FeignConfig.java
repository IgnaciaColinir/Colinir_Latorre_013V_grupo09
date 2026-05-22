package ms.pagos.ms.pagos.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

// Configuración opcional de Feign: timeout y nivel de logs
@Configuration
public class FeignConfig {

    @Bean
    public Request.Options requestOptions() {
        // 3 segundos para conectarse y 5 para leer la respuesta
        return new Request.Options(3, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}