package estacionamiento.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.domain.TipoPlan;
import estacionamiento.domain.claves.PrecioHistoricoTPId;
import estacionamiento.repository.PrecioHistoricoTPRepository;
import estacionamiento.repository.TipoPlanRepository;

public class PrecioHistoricoTPService {

    private final PrecioHistoricoTPRepository precioHistoricoRepository;
    private final TipoPlanRepository tipoPlanRepository;

    public PrecioHistoricoTPService(PrecioHistoricoTPRepository precioHistoricoRepository, 
                                    TipoPlanRepository tipoPlanRepository) {
        this.precioHistoricoRepository = precioHistoricoRepository;
        this.tipoPlanRepository = tipoPlanRepository;
    }

    public void registrarPrecio(int codigoPlan, BigDecimal precio) {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser un valor numérico positivo o cero.");
        }

        TipoPlan planAsociado = tipoPlanRepository.buscarPorClave(codigoPlan);
        if (planAsociado == null) {
            throw new IllegalArgumentException("El tipo de plan seleccionado no existe.");
        }

        LocalDateTime fechaVigencia = LocalDateTime.now();
        PrecioHistoricoTP nuevoPrecio = new PrecioHistoricoTP(planAsociado, fechaVigencia, precio);

        precioHistoricoRepository.guardar(nuevoPrecio);
        System.out.println("Servicio: Precio histórico registrado para el plan " + codigoPlan);
    }

    public List<PrecioHistoricoTP> obtenerTodos() {
        return precioHistoricoRepository.obtenerTodos();
    }
    
    public PrecioHistoricoTP buscarPorClaveCompuesta(int codigoPlan, LocalDateTime fechaDesde) {
        PrecioHistoricoTPId idCompuesto = new PrecioHistoricoTPId(codigoPlan, fechaDesde);
        PrecioHistoricoTP precioHistorico = precioHistoricoRepository.buscarPorClave(idCompuesto);
        
        if (precioHistorico == null) {
            throw new IllegalArgumentException("No se encontró el registro histórico para la fecha solicitada.");
        }
        return precioHistorico;
    }

    public void actualizar(int codigoPlan, LocalDateTime fechaDesde, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio a actualizar debe ser mayor o igual a cero.");
        }
        buscarPorClaveCompuesta(codigoPlan, fechaDesde);
        precioHistoricoRepository.actualizar(codigoPlan, fechaDesde, nuevoPrecio);
    }

    public void eliminar(int codigoPlan, LocalDateTime fechaDesde) {
        buscarPorClaveCompuesta(codigoPlan, fechaDesde);
        precioHistoricoRepository.eliminar(codigoPlan, fechaDesde);
    }
}