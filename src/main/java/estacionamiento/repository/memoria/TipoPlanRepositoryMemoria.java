package estacionamiento.repository.memoria;

import java.util.ArrayList;
import java.util.List;

import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.TipoPlanRepository;

public class TipoPlanRepositoryMemoria implements TipoPlanRepository{

	private List<TipoPlan> baseDeDatosMemoria;

    public TipoPlanRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }
    
	@Override
	public void guardar(TipoPlan tp) {
		Integer codigo = tp.getCodigo();

        if (buscarPorClave(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un tipo de plan en el sistema con ese numero.");
        }
        this.baseDeDatosMemoria.add(tp);
        System.out.println("Tipo de plan guardado con éxito, codigo de plan: " + codigo);
	}

	@Override
	public TipoPlan buscarPorClave(Integer codigo) {
		for (TipoPlan tp : this.baseDeDatosMemoria) {
            
            if (tp.getCodigo() == codigo) {
                return tp;
            }
        }
        return null;
	}

	@Override
	public List<TipoPlan> obtenerTodos() {
		return this.baseDeDatosMemoria;
	}

	@Override
	public void actualizar(Integer codigo, TipoPlan tpNuevosDatos) {
		TipoPlan tpExistente = buscarPorClave(codigo);

        if (tpExistente != null) {
        	tpExistente.setNombre(tpNuevosDatos.getNombre());
        	tpExistente.setDetalle(tpNuevosDatos.getDetalle());
        	tpExistente.setFechaBaja(tpNuevosDatos.getFechaBaja());
            System.out.println("Tipo de plan actualizado con exito, codigo: " + codigo + " "
            		+ tpNuevosDatos.getNombre() + " "
            		+ tpNuevosDatos.getDetalle() + " "
            		+ tpNuevosDatos.getFechaBaja() + " "
            );
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el tipo de plan.");
        }
	}

	@Override
	public void eliminar(Integer codigo) {
		TipoPlan tpAEliminar = buscarPorClave(codigo);

        if (tpAEliminar != null) {
            this.baseDeDatosMemoria.remove(tpAEliminar);
            System.out.println("Tipo de plan eliminado con éxito.");
        } else {
            System.out.println("No se encontró el tipo de plan para eliminar.");
        }
	}
}
