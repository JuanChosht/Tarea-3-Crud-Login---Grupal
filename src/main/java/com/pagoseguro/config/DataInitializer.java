package com.pagoseguro.config;

import com.pagoseguro.model.EstadoTransaccion;
import com.pagoseguro.model.Transaccion;
import com.pagoseguro.model.Usuario;
import com.pagoseguro.repository.TransaccionRepository;
import com.pagoseguro.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(TransaccionRepository transaccionRepository, UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.transaccionRepository = transaccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario("admin", passwordEncoder.encode("admin123"), "Administrador", "ADMIN"));
        }

        if (transaccionRepository.count() == 0) {
            LocalDateTime ahora = LocalDateTime.now();
            transaccionRepository.saveAll(List.of(
                    ejemplo("Audífonos Sony WH-1000XM5", "Nuevos, sellados, con factura", "289.99",
                            "TecnoShop UIO", "carla.mena@mail.com", 3, EstadoTransaccion.LIBERADA, ahora.minusDays(12)),
                    ejemplo("Bicicleta de montaña aro 29", "Usada 6 meses, incluye casco", "420.00",
                            "Andrés Vela", "pablo.r@mail.com", 7, EstadoTransaccion.ENVIADA, ahora.minusDays(6)),
                    ejemplo("iPhone 13 128GB", "Batería al 88%, sin rayones", "510.00",
                            "Celulares Express", "maria.j@mail.com", 2, EstadoTransaccion.EN_DISPUTA, ahora.minusDays(5)),
                    ejemplo("Zapatos Nike Air Force 1", "Talla 41, blancos", "95.50",
                            "Urban Kicks", "jorge.p@mail.com", 4, EstadoTransaccion.PAGADA, ahora.minusDays(2)),
                    ejemplo("Mesa de centro de madera", "Hecha a mano, 90x60 cm", "150.00",
                            "Taller Roble", "lucia.v@mail.com", 10, EstadoTransaccion.CREADA, ahora.minusHours(5)),
                    ejemplo("Libro: Clean Code", "Edición en español", "28.00",
                            "Librería Sur", "diego.c@mail.com", 5, EstadoTransaccion.CREADA, ahora.minusHours(1))
            ));
        }
    }

    private Transaccion ejemplo(String producto, String descripcion, String monto, String vendedor, String email,
                                int dias, EstadoTransaccion estado, LocalDateTime fecha) {
        Transaccion t = new Transaccion(producto, descripcion, new BigDecimal(monto), vendedor, email, dias, estado);
        t.setFechaCreacion(fecha);
        return t;
    }
}
