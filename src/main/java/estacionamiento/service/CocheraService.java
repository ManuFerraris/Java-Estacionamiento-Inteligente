package estacionamiento.service;

import java.util.List;

import estacionamiento.domain.Cochera;
import estacionamiento.repository.CocheraRepository;

public class CocheraService {

    private final CocheraRepository cocheraRepository;

    public CocheraService(CocheraRepository cocheraRepository) {
        this.cocheraRepository = cocheraRepository;
    }
    
    public void guardar(Cochera cochera) {
        // 1. Validaciones de integridad básica
        if (cochera == null) {
            throw new IllegalArgumentException("La cochera a registrar no puede ser nula.");
        }
        if (cochera.getCodigo() <= 0) {
            throw new IllegalArgumentException("El número de cochera debe ser mayor a cero.");
        }
        
        // 2. Validación de negocio: No pueden existir dos cocheras con el mismo código
        Cochera cocheraExistente = cocheraRepository.buscarPorClave(cochera.getCodigo());
        if (cocheraExistente != null) {
            throw new IllegalArgumentException("Error: Ya existe una cochera registrada con el número " + cochera.getCodigo() + ".");
        }

        // 3. Si todo está correcto, delegamos a la capa de acceso a datos
        cocheraRepository.guardar(cochera);
        System.out.println("Servicio: Cochera " + cochera.getCodigo() + " validada y guardada con éxito.");
    }

    public List<Cochera> obtenerTodas() {
        // En este caso no hay reglas de negocio complejas para listar, solo delegamos
        return cocheraRepository.obtenerTodos();
    }

    public Cochera buscarPorCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código de búsqueda debe ser válido.");
        }
        
        Cochera cochera = cocheraRepository.buscarPorClave(codigo);
        
        if (cochera == null) {
            throw new IllegalArgumentException("No se encontró ninguna cochera con el código " + codigo + ".");
        }
        
        return cochera;
    }
    
    public void eliminar(int codigo) {
        // Reutilizamos nuestra propia validación de búsqueda antes de intentar borrar
        buscarPorCodigo(codigo); 
        cocheraRepository.eliminar(codigo);
        System.out.println("Servicio: Cochera " + codigo + " eliminada con éxito.");
    }
}