package ms.agenda.agenda.confg;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Request;

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
