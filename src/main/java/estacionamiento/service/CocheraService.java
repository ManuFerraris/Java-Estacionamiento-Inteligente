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
        
        if(cochera.getNombre() == null) {
        	throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        
        if(cochera.getNombre().length() > 255) {
        	throw new IllegalArgumentException("El nombre no puede superar los 255 caracteres.");
        }
        
        if(cochera.getDescripcion() == null) {
        	throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        
        if(cochera.getDescripcion().length() > 255) {
        	throw new IllegalArgumentException("La descripcion no puede superar los 255 caracteres.");
        }
        
        if(cochera.getDireccion() == null) {
        	throw new IllegalArgumentException("La direccion es obligatoria.");
        }
        
        if(cochera.getDireccion().length() > 255) {
        	throw new IllegalArgumentException("La direccion no puede superar los 255 caracteres.");
        }

        // 3. Si todo está correcto, delegamos a la capa de acceso a datos
        cocheraRepository.guardar(cochera);
        System.out.println("Servicio: Cochera " + cochera.getCodigo() + " validada y guardada con éxito.");
    }
    
    public void actualizar (Integer codigo, Cochera cocheraActualizada) {
    	if (cocheraActualizada == null) {
            throw new IllegalArgumentException("La cochera a actualizar no puede ser nula.");
        }
    	
    	buscarPorCodigo(codigo);
        
        if(cocheraActualizada.getNombre() == null) {
        	throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        
        if(cocheraActualizada.getNombre().length() > 255) {
        	throw new IllegalArgumentException("El nombre no puede superar los 255 caracteres.");
        }
        
        if(cocheraActualizada.getDescripcion() == null) {
        	throw new IllegalArgumentException("La descripcion es obligatoria.");
        }
        
        if(cocheraActualizada.getDescripcion().length() > 255) {
        	throw new IllegalArgumentException("La descripcion no puede superar los 255 caracteres.");
        }
        
        if(cocheraActualizada.getDireccion() == null) {
        	throw new IllegalArgumentException("La direccion no puede estar vacia.");
        }
        
        if(cocheraActualizada.getDireccion().length() > 255) {
        	throw new IllegalArgumentException("La direccion no puede superar los 255 caracteres.");
        }
        cocheraRepository.actualizar(codigo, cocheraActualizada);
        System.out.println("Servicio: Cochera " + cocheraActualizada.getCodigo() + " validada y actualizada con éxito.");
    }

    public List<Cochera> obtenerTodas() {
        // En este caso no hay reglas de negocio complejas para listar, solo delegamos
        return cocheraRepository.obtenerTodos();
    }

    public Cochera buscarPorCodigo(Integer codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código de búsqueda debe ser válido.");
        }
        
        Cochera cochera = cocheraRepository.buscarPorClave(codigo);
        
        if (cochera == null) {
            throw new IllegalArgumentException("No se encontró ninguna cochera con el código " + codigo + ".");
        }
        
        return cochera;
    }
    
    public void eliminar(Integer codigo) {
        // Reutilizamos nuestra propia validación de búsqueda antes de intentar borrar
        buscarPorCodigo(codigo); 
        cocheraRepository.eliminar(codigo);
        System.out.println("Servicio: Cochera " + codigo + " eliminada con éxito.");
    }

    public void darDeBaja(Integer codigo) throws Exception {

        Cochera cochera = buscarPorCodigo(codigo);
        
        if (cochera != null) {
            cochera.setDescripcion("Inactiva");
            // Mandas a guardar el cambio
            cocheraRepository.actualizar(codigo, cochera); 
        } else {
            throw new Exception("La cochera no existe.");
        }
    }
}