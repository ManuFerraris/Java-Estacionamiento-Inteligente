package estacionamiento.repository.memoria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.domain.claves.PrecioHistoricoTPId;
import estacionamiento.repository.PrecioHistoricoTPRepository;

public class PrecioHistoricoTPRepositoryMemoria implements PrecioHistoricoTPRepository {
    
    private List<PrecioHistoricoTP> baseDeDatosMemoria;

    public PrecioHistoricoTPRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<PrecioHistoricoTP> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public PrecioHistoricoTP buscarPorClave(PrecioHistoricoTPId id) {
        for (PrecioHistoricoTP p : this.baseDeDatosMemoria) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void guardar(PrecioHistoricoTP precioHistorico) {
        if (buscarPorClave(precioHistorico.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un precio histórico para este plan en esa fecha exacta.");
        }
        
        this.baseDeDatosMemoria.add(precioHistorico);
        System.out.println("Memoria: Precio histórico guardado: Plan " + precioHistorico.getId().getCodigoPlan() + 
                           " a partir del " + precioHistorico.getId().getFechaDesde());
    }

    @Override
    public void eliminar(int codigoTP, LocalDateTime fechaDesde) {

        PrecioHistoricoTPId idBusqueda = new PrecioHistoricoTPId(codigoTP, fechaDesde);
        PrecioHistoricoTP precioAEliminar = buscarPorClave(idBusqueda);

        if (precioAEliminar != null) {
            this.baseDeDatosMemoria.remove(precioAEliminar);
            System.out.println("Memoria: Precio histórico del plan eliminado con éxito.");
        } else {
            System.out.println("Memoria: No se encontró el precio histórico para eliminar.");
        }
    }

    @Override
    public void actualizar(int codigoPlan, LocalDateTime fechaDesde, BigDecimal nuevoPrecio) {
        PrecioHistoricoTPId idBusqueda = new PrecioHistoricoTPId(codigoPlan, fechaDesde);
        PrecioHistoricoTP precioExistente = buscarPorClave(idBusqueda);

        if (precioExistente != null) {
            precioExistente.setPrecio(nuevoPrecio);
            System.out.println("Memoria: Precio histórico actualizado con éxito.");
        } else {
            throw new IllegalArgumentException("Memoria: No se encontró el registro histórico para actualizar.");
        }
    }
}