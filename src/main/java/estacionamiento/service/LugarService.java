package estacionamiento.service;

import estacionamiento.domain.Lugar;
import estacionamiento.repository.LugarRepository;

public class LugarService {

    private final LugarRepository lugarRepository;

    public LugarService(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    public void registrarLugar(Lugar nuevoLugar) {
        if (nuevoLugar == null) {
            throw new IllegalArgumentException("No se puede registrar un lugar nulo.");
        }

        // Validación de la descripción
        if (nuevoLugar.getDescripcion() == null || nuevoLugar.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del lugar es obligatoria.");
        }

        // Validación del número de piso
        if (nuevoLugar.getNumeroPiso() == null || nuevoLugar.getNumeroPiso().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de piso es obligatorio.");
        }

        if (nuevoLugar.getCodigoCochera() == null) {
            throw new IllegalArgumentException("El lugar debe estar asociado a una cochera válida.");
        }

        // Si pasa todas las validaciones, delegamos la persistencia al repositorio
        lugarRepository.guardar(nuevoLugar);

        System.out.println("Servicio: Lugar validado y procesado correctamente.");
    }
}