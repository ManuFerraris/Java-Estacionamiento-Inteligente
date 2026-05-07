package estacionamiento.repository;
import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.TipoEstadia;

public class TipoEstadiaRepositoryMemoria implements TipoEstadiaRepository {
private List<TipoEstadia> baseDeDatosMemoria;
	
	public TipoEstadiaRepositoryMemoria() {
		this.baseDeDatosMemoria = new ArrayList<>();
	}
	
	@Override
    public List<TipoEstadia> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }
	
	 @Override
	 public TipoEstadia buscarPorNumero(int numero) {
		 for (TipoEstadia te : this.baseDeDatosMemoria) {
			 if (te.getNumero() == numero) {
	         	return te;
	         }
	     }
	     return null;
	}
	
	 @Override
	 public void guardar(TipoEstadia tipoEstadia) {
		 // Validamos el numero porque estamos en MEMORIA (@Nati @Marilu) pero con la BD es incremental
		 if (buscarPorNumero(tipoEstadia.getNumero()) != null) {
			 throw new IllegalArgumentException("Ya existe un Tipo De Estadia con dicho numero: " + tipoEstadia.getNumero());
	     }
	     this.baseDeDatosMemoria.add(tipoEstadia);
	     System.out.println("Tipo De Estadia guardada con éxito: " + tipoEstadia.getNumero());
	 }
	 
	 @Override
	 public void actualizar(int numeroBuscado, TipoEstadia tipoEstadiaNuevosDatos) {
		 TipoEstadia tipoEstadiaExistente = buscarPorNumero(numeroBuscado);
	        
		 if (tipoEstadiaExistente != null) {
			 tipoEstadiaExistente.setDescripcion(tipoEstadiaNuevosDatos.getDescripcion());
			 tipoEstadiaExistente.setCupo(tipoEstadiaNuevosDatos.getCupo());
			 System.out.println("Tipo De Estadia actualizado: " + numeroBuscado 
					 + " " + tipoEstadiaExistente.getDescripcion() 
					 + " " + tipoEstadiaExistente.getCupo());
		 } else {
			 throw new IllegalArgumentException("No se puede actualizar. No se encontró el numero de Tipo de Estadia: " + numeroBuscado);
		 }
	 }
	 
	 @Override
	 public void eliminar(int numero) {
		 TipoEstadia estadiaAEliminar = buscarPorNumero(numero);
		 
		 if (estadiaAEliminar != null) {
			 this.baseDeDatosMemoria.remove(estadiaAEliminar);
			 System.out.println("Tipo de Estadia eliminado: " + numero);
		 } else {
			 System.out.println("No se encontró el Tipo De Estadia para eliminar.");
		 }
	 }
}
