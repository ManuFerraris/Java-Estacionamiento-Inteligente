package estacionamiento.repository;

import java.util.ArrayList;
import java.util.List;
import estacionamiento.domain.Beneficio;

public class BeneficioRepositoryMemoria implements BeneficioRepository {
    private List<Beneficio> baseDeDatosMemoria;

    public BeneficioRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Beneficio> obtenerTodos() {
        return this.baseDeDatosMemoria;
    }

    @Override
    public Beneficio buscarPorClave(int codTP, int numero) {
        for (Beneficio b : this.baseDeDatosMemoria) {
            if (b.getTipoPlan().getCodigo() == codTP && b.getNumero() == numero) {
                return b;
            }
        }
        return null;
    }

    @Override
    public void guardar(Beneficio beneficio) {
        if (buscarPorClave(beneficio.getTipoPlan().getCodigo(), beneficio.getNumero()) != null) {
            throw new IllegalArgumentException("Ya existe un Beneficio con esa combinación de Plan y Número.");
        }
        this.baseDeDatosMemoria.add(beneficio);
        System.out.println("Beneficio guardado: " + beneficio.getNumero() + " para Plan: " + beneficio.getTipoPlan().getCodigo());
    }

    @Override
    public void actualizar(int codTP, int numero, Beneficio beneficioNuevosDatos) {
        Beneficio existente = buscarPorClave(codTP, numero);
        if (existente != null) {
            existente.setDescripcion(beneficioNuevosDatos.getDescripcion());
            existente.setFechaBaja(beneficioNuevosDatos.getFechaBaja());
            System.out.println("Beneficio actualizado con éxito.");
        } else {
            throw new IllegalArgumentException("No se encontró el beneficio para actualizar.");
        }
    }

    @Override
    public void eliminar(int codTP, int numero) {
        Beneficio b = buscarPorClave(codTP, numero);
        if (b != null) {
            this.baseDeDatosMemoria.remove(b);
            System.out.println("Beneficio eliminado correctamente.");
        } else {
            System.out.println("No se encontró el beneficio para eliminar.");
        }
    }
}