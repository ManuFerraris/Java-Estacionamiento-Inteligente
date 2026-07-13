package estacionamiento.repository;

import estacionamiento.domain.Lugar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LugarRepositoryMemoria implements LugarRepository {
    private List<Lugar> baseDeDatosMemoria;

    public LugarRepositoryMemoria() {
        this.baseDeDatosMemoria = new ArrayList<>();
    }

    @Override
    public List<Lugar> obtenerTodos() {

        return new ArrayList<>(this.baseDeDatosMemoria);
    }

    @Override
    public Optional<Lugar> buscarPorClave(String codigo) {

        return this.baseDeDatosMemoria.stream()
                .filter(l -> l.getCodigo().equals(codigo))
                .findFirst();
    }

    @Override
    public Lugar guardar(Lugar lugar) {
        String codigo = lugar.getCodigo();

        if (buscarPorClave(codigo).isPresent()) {
            throw new IllegalArgumentException("Ya existe un Lugar en el sistema con ese numero.");
        }
        
        this.baseDeDatosMemoria.add(lugar);
        System.out.println("Lugar guardado con éxito, numero de lugar: " + codigo);

        return lugar;
    }

    @Override
    public void actualizar(String codigo, Lugar lugarNuevosDatos) {
        Optional<Lugar> lugarExistenteOpt = buscarPorClave(codigo);

        if (lugarExistenteOpt.isPresent()) {
            Lugar lugarExistente = lugarExistenteOpt.get(); 
            
            lugarExistente.setDescripcion(lugarNuevosDatos.getDescripcion());
            lugarExistente.setNumeroPiso(lugarNuevosDatos.getNumeroPiso());
                       
            System.out.println("Lugar actualizado con exito, codigo: " + codigo + " "
                    + lugarNuevosDatos.getDescripcion() + " "
                    + lugarNuevosDatos.getNumeroPiso() + " "
            );
        } else {
            throw new IllegalArgumentException("No se puede actualizar. No se encontró el lugar.");
        }
    }

    @Override
    public void eliminar(String codigo) {
        Optional<Lugar> lugarAEliminarOpt = buscarPorClave(codigo);

        if (lugarAEliminarOpt.isPresent()) {
           
            this.baseDeDatosMemoria.remove(lugarAEliminarOpt.get());
            System.out.println("Lugar eliminado con éxito.");
        } else {
            System.out.println("No se encontró el lugar para eliminar.");
        }
    }
}
