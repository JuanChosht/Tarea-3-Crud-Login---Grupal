package com.pagoseguro.service;

import com.pagoseguro.model.EstadoTransaccion;
import com.pagoseguro.model.Transaccion;
import com.pagoseguro.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TransaccionService {
    private final TransaccionRepository transaccionRepository;

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    @Transactional(readOnly = true)
    public List<Transaccion> listar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return transaccionRepository.findAllByOrderByFechaCreacionDesc();
        }
        return transaccionRepository.buscar(busqueda.trim());
    }

    @Transactional(readOnly = true)
    public Optional<Transaccion> buscarPorId(Long id) {
        return transaccionRepository.findById(id);
    }

    @Transactional
    public Transaccion crear(Transaccion transaccion) {
        transaccion.setId(null);
        transaccion.setEstado(EstadoTransaccion.CREADA);
        return transaccionRepository.save(transaccion);
    }

    @Transactional
    public Transaccion actualizar(Transaccion actual, Transaccion datos) {
        actual.setProducto(datos.getProducto());
        actual.setDescripcion(datos.getDescripcion());
        actual.setMonto(datos.getMonto());
        actual.setVendedor(datos.getVendedor());
        actual.setEmailComprador(datos.getEmailComprador());
        actual.setDiasEntrega(datos.getDiasEntrega());
        return transaccionRepository.save(actual);
    }

    @Transactional
    public void eliminar(Transaccion transaccion) {
        transaccionRepository.delete(transaccion);
    }

    @Transactional(readOnly = true)
    public long contar() {
        return transaccionRepository.count();
    }

    @Transactional(readOnly = true)
    public BigDecimal montoRetenido() {
        return transaccionRepository.sumarMontoPorEstados(
                Arrays.stream(EstadoTransaccion.values()).filter(EstadoTransaccion::esRetenido).toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal montoLiberado() {
        return transaccionRepository.sumarMontoPorEstados(List.of(EstadoTransaccion.LIBERADA));
    }
}
