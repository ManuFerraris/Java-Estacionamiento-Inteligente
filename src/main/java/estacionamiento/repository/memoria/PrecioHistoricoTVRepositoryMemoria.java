package estacionamiento.repository.memoria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.PrecioHistoricoTV;
import estacionamiento.domain.claves.PrecioHistoricoTVId;
import estacionamiento.repository.PrecioHistoricoTVRepository;

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
        PrecioHistoricoTVId historicoId = new PrecioHistoricoTVId(numeroTV, fechaDesde);
        
        for (PrecioHistoricoTV p : this.baseDeDatosMemoria) {
            if (p.getId().equals(historicoId)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void guardar(PrecioHistoricoTV precioHistorico) {
        PrecioHistoricoTVId historicoId = precioHistorico.getId();
        
        // Corregido: accedemos correctamente a los atributos del ID compuesto
        if (buscarPorClave(historicoId.getNumeroTipoVehiculo(), historicoId.getFechaDesde()) != null) {
            throw new IllegalArgumentException("Ya existe un precio histórico para este tipo de vehículo en esa fecha exacta.");
        }
        
        this.baseDeDatosMemoria.add(precioHistorico);
        System.out.println("Precio histórico guardado: Vehículo Tipo " + historicoId.getNumeroTipoVehiculo() + " a partir del " + historicoId.getFechaDesde());
    }

    @Override
    public void actualizar(PrecioHistoricoTV precioHistoricoTV) {
        // Normalmente como es una entidad debil de precios no hacemos actualizaciones sobre la misma
    	// creamos o eliminamos.
    }

    @Override
    public void eliminar(PrecioHistoricoTV precioHistoricoTV) {
    	this.baseDeDatosMemoria.remove(precioHistoricoTV);
    	System.out.println("Precio histórico eliminado con éxito.");
    	System.out.println("No se encontró el precio histórico para eliminar.");
    }

	@Override
	public BigDecimal obtenerPrecioVigente(int numero) {
		// TODO Auto-generated method stub
		return null;
	}
}