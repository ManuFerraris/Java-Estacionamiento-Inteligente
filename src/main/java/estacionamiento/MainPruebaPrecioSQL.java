package estacionamiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PrecioHistoricoTV;
import estacionamiento.domain.TipoVehiculo;
import estacionamiento.repository.PrecioHistoricoTVRepositorySQL;

public class MainPruebaPrecioSQL {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   PRUEBA: CLAVE COMPUESTA EN MYSQL (PRECIOS)     ");
        System.out.println("==================================================\n");

        try {
            // 1. Inicializamos el repositorio específico de MySQL
            PrecioHistoricoTVRepositorySQL repoPrecios = new PrecioHistoricoTVRepositorySQL();

            // 2. Simulamos el Tipo de Vehículo que viene del dominio
            // Nota: En un escenario real, este objeto se recuperaría primero de su propio repositorio
            TipoVehiculo tipoAuto = new TipoVehiculo();
            tipoAuto.setNumero(2); // Asumimos que el código 1 corresponde a un vehículo tipo "Auto"
            tipoAuto.setNombre("Motocicleta");
            // 3. Creamos el registro del Histórico de Precio
            LocalDateTime fechaRegistro = LocalDateTime.now(); // Esta será la segunda parte de nuestra clave compuesta

            PrecioHistoricoTV nuevoPrecio = new PrecioHistoricoTV();
            nuevoPrecio.setTipoVehiculo(tipoAuto);
            nuevoPrecio.setFechaDesde(fechaRegistro);
            nuevoPrecio.setPrecio(new BigDecimal("1500.00"));

            System.out.println("--- 1. INTENTANDO GUARDAR EL PRECIO HISTÓRICO ---");
            repoPrecios.guardar(nuevoPrecio);

            System.out.println("\n--- 2. INTENTANDO BUSCAR POR CLAVE COMPUESTA ---");
            // Pasamos los dos datos de la clave por separado; el repositorio se encargará de agruparlos
            PrecioHistoricoTV recuperado = repoPrecios.buscarPorClave(1, fechaRegistro);

            if (recuperado != null) {
                System.out.println("¡ÉXITO: Se recuperó el registro correctamente!");
                System.out.println("Vehículo Código: " + recuperado.getTipoVehiculo().getNumero());
                System.out.println("Fecha Desde: " + recuperado.getFechaDesde());
                System.out.println("Precio asignado: $" + recuperado.getPrecio());
            } else {
                System.out.println("Fallo: No se pudo encontrar el registro con esa clave compuesta.");
            }

            System.out.println("\n--- 3. LISTADO COMPLETO DE HISTÓRICOS ---");
            List<PrecioHistoricoTV> todos = repoPrecios.obtenerTodos();
            System.out.println("Cantidad de registros en la tabla: " + todos.size());

        } catch (Exception e) {
            System.err.println("¡Error durante la ejecución de la prueba de persistencia!");
            System.err.println("Causa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}