package com.belmonte.ms_pasajeros.config;

import com.belmonte.ms_pasajeros.model.Pasajero;
import com.belmonte.ms_pasajeros.repository.PasajeroRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Carga datos 100% sintéticos (el anexo prohíbe usar datos reales).
 * Solo corre en el perfil "dev" y únicamente si la tabla está vacía.
 */
@Slf4j
@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    CommandLineRunner cargarPasajeros(PasajeroRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            Faker faker = new Faker(Locale.forLanguageTag("es"));
            List<Pasajero> pasajeros = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                pasajeros.add(new Pasajero(
                        null,
                        faker.name().fullName(),
                        "pasajero" + i + "@ejemplo.test",
                        "+5699" + faker.number().digits(7),
                        faker.company().name(),
                        true
                ));
            }
            repository.saveAll(pasajeros);
            log.info(">>> {} pasajeros sintéticos cargados", repository.count());
        };
    }
}
