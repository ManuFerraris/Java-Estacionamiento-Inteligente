package estacionamiento.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import estacionamiento.dto.FilaReporteFinancieroDTO;
import estacionamiento.repository.mysql.ReporteFinancieroRepository;

public class ReporteFinancieroService {
    
    private final ReporteFinancieroRepository reporteRepo;

    private static final Set<String> ORIGENES_VALIDOS =
            Set.of("TODOS", "RESERVAS", "SUSCRIPCIONES");
    
    public ReporteFinancieroService(ReporteFinancieroRepository reporteRepo) {
    	this.reporteRepo = new ReporteFinancieroRepository();
    }

    public List<FilaReporteFinancieroDTO> generarReporte(LocalDateTime desde, LocalDateTime hasta, String origen) {
        
        // Rango de fechas
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                "La fecha inicial no puede ser posterior a la fecha final."
            );
        }

        // Por defecto hago que sea TODOS para evitar errores y generalizar
        if (origen == null || origen.isBlank()) {
            origen = "TODOS";
        } else {
            origen = origen.trim().toUpperCase();
        }

        if (!ORIGENES_VALIDOS.contains(origen)) {
            throw new IllegalArgumentException(
                "El origen seleccionado no es válido."
            );
        }

        return reporteRepo.generarReporte(desde, hasta, origen);
    }
}