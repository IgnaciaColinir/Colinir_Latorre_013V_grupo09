package ms.usuarios.ms.usuarios;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ms.usuarios.ms.usuarios.Model.ModeloUsuario;
import ms.usuarios.ms.usuarios.Repository.RepositoryUsuarios;
import net.datafaker.Faker;

@Profile("dev")
@Component
public class DataLoaderUsuarios implements CommandLineRunner {

    @Autowired
    private RepositoryUsuarios usuarioRepository;
    @Override
    public void run(String... args) throws Exception {
        // Configuramos Faker en español ("es") para obtener nombres y apellidos comunes de la región
        Faker faker = new Faker();
        Random random = new Random();

        // Roles o cargos estandarizados que acepta tu sistema/modelo
        String[] cargosDisponibles = {"ADMINISTRADOR", "MEDICO", "RECEPCIONISTA"};

        // 💡 Regla de oro: Validar que la tabla esté vacía para evitar duplicar registros en cada reinicio
        if (usuarioRepository.count() == 0) {
            System.out.println("=======> [ms-usuarios] Iniciando poblado de datos con DataFaker...");

            // Generamos 15 usuarios administrativos y de salud ficticios
            for (int i = 0; i < 15; i++) {
                
                String rutSimulado = faker.number().numberBetween(10000000, 25000000) + "-" + faker.number().numberBetween(0, 9);
                
                String nombre = faker.name().firstName();
                String apellido = faker.name().lastName();
                
                String emailCorporativo = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@clinica.cl";
                
                String cargoAsignado = cargosDisponibles[random.nextInt(cargosDisponibles.length)];

                ModeloUsuario usuario = ModeloUsuario.builder()
                        .rut(rutSimulado)
                        .nombre(nombre)
                        .apellido(apellido)
                        .email(emailCorporativo)
                        .password("password123") 
                        .cargo(cargoAsignado)
                        .build();

                usuarioRepository.save(usuario);
            }


            System.out.println("=======> [ms-usuarios] Poblado exitoso. Se crearon 15 usuarios en la base de datos.");
        } else {
            System.out.println("=======> [ms-usuarios] La tabla ya tiene datos. Omitiendo DataLoader.");
        }
    }
}
