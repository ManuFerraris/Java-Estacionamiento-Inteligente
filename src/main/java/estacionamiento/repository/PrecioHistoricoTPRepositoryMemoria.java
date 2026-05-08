package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTP;

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
    public PrecioHistoricoTP buscarPorClave(int codigoTP, LocalDateTime fechaDesde) {
        for (PrecioHistoricoTP p : this.baseDeDatosMemoria) {
            if (p.getTipoPlan().getCodigo() == codigoTP && 
                p.getFechaDesde().equals(fechaDesde)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void guardar(PrecioHistoricoTP precioHistorico) {
        int codTP = precioHistorico.getTipoPlan().getCodigo();
        LocalDateTime fecha = precioHistorico.getFechaDesde();

        if (buscarPorClave(codTP, fecha) != null) {
            throw new IllegalArgumentException("Ya existe un precio histórico para este plan en esa fecha exacta.");
        }
        
        this.baseDeDatosMemoria.add(precioHistorico);
        System.out.println("Precio histórico guardado: Plan " + codTP + " a partir del " + fecha);
    }

    @Override
    public void actualizar(int codigoTP, LocalDateTime fechaDesde, PrecioHistoricoTP precioNuevosDatos) {
        PrecioHistoricoTP precioExistente = buscarPorClave(codigoTP, fechaDesde);

        if (precioExistente != null) {
            precioExistente.setPrecio(precioNuevosDatos.getPrecio());
            System.out.println("Precio histórico del plan actualizado con éxito a: $" + precioExistente.getPrecio());
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el precio histórico del plan.");
        }
    }

    @Override
    public void eliminar(int codigoTP, LocalDateTime fechaDesde) {
        PrecioHistoricoTP precioAEliminar = buscarPorClave(codigoTP, fechaDesde);

        if (precioAEliminar != null) {
            this.baseDeDatosMemoria.remove(precioAEliminar);
            System.out.println("Precio histórico del plan eliminado con éxito.");
        } else {
            System.out.println("No se encontró el precio histórico para eliminar.");
        }
    }
}