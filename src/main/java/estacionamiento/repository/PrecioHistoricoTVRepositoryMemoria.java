package estacionamiento.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTV;

public class PrecioHistoricoTVRepositoryMemoria implements PrecioHistoricoTVRepository {
    
    private List<PrecioHistoricoTV> baseDeDatosMemoria;

    public PrecioHistoricoTVRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<PrecioHistoricoTV> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public PrecioHistoricoTV buscarPorClave(int numeroTV, LocalDateTime fechaDesde) {
        for (PrecioHistoricoTV p : this.baseDeDatosMemoria) {
            // Evaluamos la clave compuesta
            if (p.getTipoVehiculo().getNumero() == numeroTV && 
                p.getFechaDesde().equals(fechaDesde)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void guardar(PrecioHistoricoTV precioHistorico) {
        int numTV = precioHistorico.getTipoVehiculo().getNumero();
        LocalDateTime fecha = precioHistorico.getFechaDesde();

        if (buscarPorClave(numTV, fecha) != null) {
            throw new IllegalArgumentException("Ya existe un precio histórico para este tipo de vehículo en esa fecha exacta.");
        }
        
        this.baseDeDatosMemoria.add(precioHistorico);
        System.out.println("Precio histórico guardado: Vehículo Tipo " + numTV + " a partir del " + fecha);
    }

    @Override
    public void actualizar(int numeroTV, LocalDateTime fechaDesde, PrecioHistoricoTV precioNuevosDatos) {
        PrecioHistoricoTV precioExistente = buscarPorClave(numeroTV, fechaDesde);

        if (precioExistente != null) {
            // Importante: No modificamos ni el TipoVehiculo ni la FechaDesde porque son la identidad del registro.
            // Solo actualizamos el valor del precio.
            precioExistente.setPrecio(precioNuevosDatos.getPrecio());
            
            System.out.println("Precio histórico actualizado con éxito a: $" + precioExistente.getPrecio());
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el precio histórico.");
        }
    }

    @Override
    public void eliminar(int numeroTV, LocalDateTime fechaDesde) {
        PrecioHistoricoTV precioAEliminar = buscarPorClave(numeroTV, fechaDesde);

        if (precioAEliminar != null) {
            this.baseDeDatosMemoria.remove(precioAEliminar);
            System.out.println("Precio histórico eliminado con éxito.");
        } else {
            System.out.println("No se encontró el precio histórico para eliminar.");
        }
    }
}