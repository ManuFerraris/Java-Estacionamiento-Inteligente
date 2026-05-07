package estacionamiento;
import estacionamiento.domain.Vehiculo;
import estacionamiento.domain.TipoVehiculo;
import estacionamiento.repository.VehiculoRepositoryMemoria;

// Aca podemos probar las distintas clases que vamos creando
public class MainPrueba {
    public static void main(String[] args) {
        VehiculoRepositoryMemoria repo = new VehiculoRepositoryMemoria();
        TipoVehiculo tipoAuto = new TipoVehiculo(1, "Auto");

        // 1. CREATE
        Vehiculo miAuto = new Vehiculo("AB123CD", "Taunus 2.3 Beige", tipoAuto);
        repo.guardar(miAuto);

        // 2. READ
        Vehiculo autoEncontrado = repo.buscarPorPatente("ab123cd");
        System.out.println("Encontré el auto: " + autoEncontrado.getDescripcion());

        // 3. UPDATE
        Vehiculo datosNuevos = new Vehiculo("AB123CD", "Peugeot 208 Negro (Pintado)", tipoAuto);
        repo.actualizar("AB123CD", datosNuevos);

        // 4. DELETE
        repo.eliminar("AB123CD");
    }
}