package clinicaSalud.ms_inventario;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import clinicaSalud.ms_inventario.Model.Insumo;
import clinicaSalud.ms_inventario.Repository.InsumoRepository;

@SpringBootApplication
public class MsInventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsInventarioApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(InsumoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Insumo i1 = new Insumo();
                i1.setNombre("Paracetamol 500mg");
                i1.setCategoria("Medicamento");
                i1.setStockActual(500);
                i1.setStockMinimo(100);
                repository.save(i1);

                // El Easter Egg 
                Insumo i2 = new Insumo();
                i2.setNombre("Poción de Sanación Mayor");
                i2.setCategoria("Alquimia Medica");
                i2.setStockActual(99);
                i2.setStockMinimo(10);
                repository.save(i2);
            }
        };
    }
}