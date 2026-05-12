package estacionamiento.repository;

import estacionamiento.domain.Cochera;

import java.util.ArrayList;
import java.util.List;

public class CocheraRepositoryMemoria implements CocheraRepository {
	private List<Cochera> baseDeDatosMemoria;
	
	public CocheraRepositoryMemoria() {
		this.baseDeDatosMemoria = new ArrayList<>();
	}
	
	 @Override
	 public List<Cochera> obtenerTodos() {
		 return this.baseDeDatosMemoria;
	 }

	 @Override
	 public Cochera buscarPorClave(int codigo) {
		 for (Cochera c : this.baseDeDatosMemoria) {
	            
			 if (c.getCodigo() == codigo) {
				 return c;
			 }
		 }
		 return null;
	 }
	 
	 @Override
	 public void guardar(Cochera cochera) {
	        
		 int codigo = cochera.getCodigo();

		 if (buscarPorClave(codigo) != null) {
			 throw new IllegalArgumentException("Ya existe una cochera en el sistema con ese codigo numerico.");
		 }
		 this.baseDeDatosMemoria.add(cochera);
		 System.out.println("Cochera registrada con éxito, codigo de cochera: " + codigo);
	 }

	 @Override
	 public void actualizar(int codigo, Cochera nuevaCochera) {
		 Cochera cocheraExistente = buscarPorClave(codigo);

		 if (cocheraExistente != null) {
			 cocheraExistente.setNombre(nuevaCochera.getNombre());
			 cocheraExistente.setDireccion(nuevaCochera.getDireccion());
			 cocheraExistente.setDescripcion(nuevaCochera.getDescripcion());

			 System.out.println("Cochera actualizada con exito, codigo: " + codigo + " "
					 + nuevaCochera.getNombre() + " ubicada en: "
					 + nuevaCochera.getDireccion() + " "
					 );
		 } else {
			 throw new IllegalArgumentException("No se puede actualizar. No se encontró la cochera.");
		 }
	 }

	 @Override
	 public void eliminar(int codigo) {
		 Cochera cocheraAEliminar = buscarPorClave(codigo);

		 if (cocheraAEliminar != null) {
			 this.baseDeDatosMemoria.remove(cocheraAEliminar);
			 System.out.println("Cochera eliminada con éxito.");
		 } else {
			 System.out.println("No se encontró la cochera para eliminar.");
		 }
	 }
}
