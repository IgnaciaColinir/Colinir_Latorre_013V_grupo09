package clinicaSalud.ms_inventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import clinicaSalud.ms_inventario.Model.Insumo;
import clinicaSalud.ms_inventario.Repository.InsumoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private InsumoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            Insumo i1 = new Insumo();
            i1.setNombre("Mascarillas Quirúrgicas");
            i1.setCategoria("Insumo Médico");
            i1.setStockActual(500);
            i1.setStockMinimo(100);
            repository.save(i1);

            Insumo i2 = new Insumo();
            i2.setNombre("Jeringas 5ml");
            i2.setCategoria("Material Estéril");
            i2.setStockActual(200);
            i2.setStockMinimo(50);
            repository.save(i2);
            
            log.info("Insumos de prueba cargados exitosamente.");
        }
    }
}