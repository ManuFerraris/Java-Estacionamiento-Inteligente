package estacionamiento.service;

import java.util.List;
import estacionamiento.domain.Lugar;
import estacionamiento.repository.mysql.LugarRepositoryMySQL;

public class LugarService {

    private final LugarRepositoryMySQL lugarRepository;

    public LugarService(LugarRepositoryMySQL lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    public void registrarLugar(Lugar nuevoLugar) {
        if (nuevoLugar == null) throw new IllegalArgumentException("El lugar no puede ser nulo.");
        if (nuevoLugar.getNumeroPiso() < 0) throw new IllegalArgumentException("El piso no puede ser negativo.");
        if (nuevoLugar.getCochera() == null) throw new IllegalArgumentException("El lugar debe pertenecer a una cochera.");
        
        lugarRepository.guardar(nuevoLugar);
    }

    public List<Lugar> obtenerTodosLosLugares() {
        return lugarRepository.obtenerTodos();
    }

    // CORRECCIÓN: Validación clásica de nulos (sin Optional)
    public Lugar buscarLugarPorCodigo(Integer codigo) {
        if (codigo == null || codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un número positivo válido.");
        }
        
        Lugar lugar = lugarRepository.buscarPorClave(codigo);
        
        if (lugar == null) {
            throw new IllegalArgumentException("No se encontró el lugar con código: " + codigo);
        }
        
        return lugar;
    }

    public void actualizarLugar(Integer codigo, Lugar lugarActualizado) {
        Lugar lugarExistente = buscarLugarPorCodigo(codigo);
        
        if (lugarActualizado.getNumeroPiso() < 0) {
            throw new IllegalArgumentException("El número de piso no puede ser negativo.");
        }
        if (lugarActualizado.getCochera() == null) {
            throw new IllegalArgumentException("El lugar debe pertenecer a una cochera válida.");
        }

        lugarExistente.setNumeroPiso(lugarActualizado.getNumeroPiso());
        lugarExistente.setDescripcion(lugarActualizado.getDescripcion());
        lugarExistente.setCochera(lugarActualizado.getCochera());

        lugarRepository.actualizar(codigo, lugarExistente);
    }

    public void darDeBajaLugar(Integer codigo) {
        Lugar lugar = buscarLugarPorCodigo(codigo);
        lugar.setDescripcion("Inactivo");
        lugarRepository.actualizar(codigo, lugar);
    }
}