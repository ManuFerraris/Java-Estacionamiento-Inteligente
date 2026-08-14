package estacionamiento.service;

import java.util.List;
import estacionamiento.domain.Lugar;
import estacionamiento.repository.LugarRepository;

public class LugarService {

    private final LugarRepository lugarRepository;

    public LugarService(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    public Lugar registrarLugar(Lugar nuevoLugar) {
        if (nuevoLugar == null) {
            throw new IllegalArgumentException("No se puede registrar un lugar nulo.");
        }

        // Validación de la descripción
        if (nuevoLugar.getDescripcion() == null || nuevoLugar.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del lugar es obligatoria.");
        }

        // Validación del piso (Ahora es int, por lo que no puede ser null)
        if (nuevoLugar.getNumeroPiso() < 0) {
            throw new IllegalArgumentException("El número de piso no puede ser negativo.");
        }

        if (nuevoLugar.getCochera() == null) {
            throw new IllegalArgumentException("El lugar debe estar asociado a una cochera válida.");
        }

        return lugarRepository.guardar(nuevoLugar);
    }

    public List<Lugar> obtenerTodosLosLugares() {
        return lugarRepository.obtenerTodos();
    }

    public Lugar buscarLugarPorCodigo(int codigo) throws Exception {
    	Lugar lugarBuscado = lugarRepository.buscarPorClave(codigo);
    	if(lugarBuscado == null) {
                throw new Exception("No se encontró ningún lugar con el código: " + codigo);
    	}
    	return lugarBuscado;
    }

    public void actualizarLugar(int codigo, Lugar lugarNuevosDatos) {
        lugarRepository.actualizar(codigo, lugarNuevosDatos);
    }

    public void eliminarLugar(int codigo) {
        lugarRepository.eliminar(codigo);
    }
}