package com.pagoseguro.repository;

import com.pagoseguro.model.EstadoTransaccion;
import com.pagoseguro.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findAllByOrderByFechaCreacionDesc();

    @Query("""
            select t from Transaccion t
            where lower(t.producto) like lower(concat('%', :texto, '%'))
               or lower(t.vendedor) like lower(concat('%', :texto, '%'))
               or lower(t.emailComprador) like lower(concat('%', :texto, '%'))
            order by t.fechaCreacion desc
            """)
    List<Transaccion> buscar(@Param("texto") String texto);

    @Query("select coalesce(sum(t.monto), 0) from Transaccion t where t.estado in :estados")
    BigDecimal sumarMontoPorEstados(@Param("estados") Collection<EstadoTransaccion> estados);
}
