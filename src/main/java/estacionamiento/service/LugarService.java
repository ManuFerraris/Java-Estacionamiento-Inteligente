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

        // Validación del código
        if (nuevoLugar.getCodigo() == null || nuevoLugar.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del lugar es obligatorio.");
        }

        // Validación de la descripción
        if (nuevoLugar.getDescripcion() == null || nuevoLugar.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del lugar es obligatoria.");
        }

        // Validación del piso (Ahora es int, por lo que no puede ser null)
        if (nuevoLugar.getNumeroPiso() < 0) {
            throw new IllegalArgumentException("El número de piso no puede ser negativo.");
        }

        if (nuevoLugar.getCodigoCochera() == null) {
            throw new IllegalArgumentException("El lugar debe estar asociado a una cochera válida.");
        }

        return lugarRepository.guardar(nuevoLugar);
    }

    public List<Lugar> obtenerTodosLosLugares() {
        return lugarRepository.obtenerTodos();
    }

    public Lugar buscarLugarPorCodigo(String codigo) throws Exception {
        return lugarRepository.buscarPorClave(codigo)
                .orElseThrow(() -> new Exception("No se encontró ningún lugar con el código: " + codigo));
    }

    public void actualizarLugar(String codigo, Lugar lugarNuevosDatos) {
        lugarRepository.actualizar(codigo, lugarNuevosDatos);
    }

    public void eliminarLugar(String codigo) {
        lugarRepository.eliminar(codigo);
    }
}