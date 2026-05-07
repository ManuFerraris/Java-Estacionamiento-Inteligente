package estacionamiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import estacionamiento.domain.EstadoReserva;
import estacionamiento.domain.Lugar;
import estacionamiento.domain.Reserva;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.domain.TipoVehiculo;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;
import estacionamiento.repository.ReservaRepositoryMemoria;
import estacionamiento.repository.TipoEstadiaRepositoryMemoria;
import estacionamiento.repository.UsuarioRepositoryMemoria;
import estacionamiento.repository.VehiculoRepositoryMemoria;
import estacionamiento.service.ReservaService;

// Aca probamos la creacion de reservar y demas cosas que tienen mas relaciones.
public class MainPruebaGeneral {

    public static void main(String[] args) {
        
        System.out.println("--- 1. INICIALIZANDO REPOSITORIOS ---");
        UsuarioRepositoryMemoria repoUsuarios = new UsuarioRepositoryMemoria();
        VehiculoRepositoryMemoria repoVehiculos = new VehiculoRepositoryMemoria();
        TipoEstadiaRepositoryMemoria repoTipoEstadia = new TipoEstadiaRepositoryMemoria();
        ReservaRepositoryMemoria repoReservas = new ReservaRepositoryMemoria();

        // Capa de Negocio (Inyectamos los repositorios en el Servicio)
        ReservaService servicioReserva = new ReservaService(repoReservas, repoVehiculos, repoUsuarios, repoTipoEstadia);
        
        System.out.println("\n--- 2. CREANDO DATOS PREVIOS (PADRES) ---");
        
        // Crear el Usuario
        Usuario usuario1 = new Usuario();
        usuario1.setNumero(1);
        usuario1.setNombre("Marilu");
        usuario1.setApellido("Natalia #Dolidas");
        repoUsuarios.guardar(usuario1);

        // Crear el Tipo de Vehículo y el Vehículo
        TipoVehiculo tipoAuto = new TipoVehiculo(1, "Auto");
        Vehiculo miAuto = new Vehiculo("AB123CD", "Fiat Cronos Rosado", tipoAuto);
        repoVehiculos.guardar(miAuto);

        // Crear el Tipo de Estadía
        TipoEstadia estadiaPorHora = new TipoEstadia();
        estadiaPorHora.setNumero(1);
        estadiaPorHora.setDescripcion("Estadía Por Hora");
        estadiaPorHora.setCupo(50);
        repoTipoEstadia.guardar(estadiaPorHora);

        // Crear un Lugar (El espacio físico)
        Lugar lugarA1 = new Lugar(1, "Sector Verde - PB", "A1");

        System.out.println("\n--- 3. CREANDO LA RESERVA ---");
        
        // Configuramos los datos de negocio (Fechas y Montos)
        LocalDateTime fechaInicio = LocalDateTime.now();
        BigDecimal seniaAbonada = new BigDecimal("1500.00");

        // Armamos la Reserva con todos los objetos completos
        Reserva nuevaReserva = new Reserva(
            miAuto,                 // El objeto Vehiculo entero
            usuario1,               // El objeto Usuario entero
            estadiaPorHora,         // El objeto TipoEstadia entero
            fechaInicio,            // fechaDesde
            null,                   // El servicio se encarga de calcular la fechaHastaTentativa
            null,                   // fechaHastaReal (todavía no terminó)
            EstadoReserva.CONFIRMADA, // Usando el Enum
            seniaAbonada,           // senia
            null,                   // pago (Aún no pagó el saldo total), recuerden que aca iria el OBJETO pago.
            lugarA1                 // lugar asignado
        );

        // Ahora el servicio se encarga de las validaciones
        servicioReserva.registrarNuevaReserva(nuevaReserva);

        System.out.println("\n--- 4. COMPROBACIÓN ---");
        // Vamos a buscarla a la "base de datos" para ver si realmente se guardó
        Reserva reservaRecuperada = repoReservas.buscarPorClave("AB123CD", 1, 1, fechaInicio);
        
        if (reservaRecuperada != null) {
            System.out.println("¡ÉXITO AL REALIZAR LA RESERVA!");
            System.out.println("Reserva encontrada para: " + reservaRecuperada.getUsuario().getNombre() + " " + reservaRecuperada.getUsuario().getApellido());
            System.out.println("Vehículo asignado: " + reservaRecuperada.getVehiculo().getDescripcion());
            System.out.println("Límite de llegada: " + reservaRecuperada.getFechaHastaTentativa());
        }
    }
}