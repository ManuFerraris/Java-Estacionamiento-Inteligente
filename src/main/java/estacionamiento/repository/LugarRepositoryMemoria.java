package estacionamiento.repository;

import estacionamiento.domain.Lugar;

import java.util.ArrayList;
import java.util.List;

public class LugarRepositoryMemoria implements LugarRepository {
	private List<Lugar> baseDeDatosMemoria;

    public LugarRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Lugar> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public Lugar buscarPorClave(int codigo) {
        for (Lugar l : this.baseDeDatosMemoria) {
            
            if (l.getCodigo() == codigo) {
                return l;
            }
        }
        return null;
    }

    @Override
    public void guardar(Lugar lugar) {
        
        int codigo = lugar.getCodigo();

        if (buscarPorClave(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un Lugar en el sistema con ese numero.");
        }
        this.baseDeDatosMemoria.add(lugar);
        System.out.println("Lugar guardado con éxito, numero de lugar: " + codigo);
    }

    @Override
    public void actualizar(int codigo, Lugar lugarNuevosDatos) {
        Lugar lugarExistente = buscarPorClave(codigo);

        if (lugarExistente != null) {
        	lugarExistente.setDescripcion(lugarNuevosDatos.getDescripcion());
        	lugarExistente.setNumeroPiso(lugarNuevosDatos.getNumeroPiso());
        	// No agrego el codigo de cochera porque no lo veo necesario. Se borra y se vuelve a crear.
            System.out.println("Lugar actualizado con exito, codigo: " + codigo + " "
            		+ lugarNuevosDatos.getDescripcion() + " "
            		+ lugarNuevosDatos.getNumeroPiso() + " "
            );
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el lugar.");
        }
    }

    @Override
    public void eliminar(int codigo) {
        Lugar lugarAEliminar = buscarPorClave(codigo);

        if (lugarAEliminar != null) {
            this.baseDeDatosMemoria.remove(lugarAEliminar );
            System.out.println("Lugar eliminado con éxito.");
        } else {
            System.out.println("No se encontró el lugar para eliminar.");
        }
    }
}
