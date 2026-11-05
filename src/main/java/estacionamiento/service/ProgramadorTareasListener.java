package estacionamiento.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import estacionamiento.repository.mysql.LugarRepositoryMySQL;
import estacionamiento.repository.mysql.PagoRepositoryMySQL;
import estacionamiento.repository.mysql.PagoSuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTPRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTVRepositoryMySQL;
import estacionamiento.repository.mysql.ReservaRepositoryMySQL;
import estacionamiento.repository.mysql.SuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.repository.mysql.VehiculoRepositoryMySQL;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ProgramadorTareasListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private ReservaService reservaService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	
        System.out.println("[SISTEMA] Iniciando motor de tareas en segundo plano...");

        ReservaRepositoryMySQL reservaRepo = new ReservaRepositoryMySQL();
        LugarRepositoryMySQL lugarRepo = new LugarRepositoryMySQL();
        VehiculoRepositoryMySQL vehiculoRepo = new VehiculoRepositoryMySQL();
        UsuarioRepositoryMySQL usuarioRepo = new UsuarioRepositoryMySQL();
        TipoEstadiaRepositoryMySQL tipoEstadiaRepo = new TipoEstadiaRepositoryMySQL();
        PrecioHistoricoTVRepositoryMySQL precioRepo = new PrecioHistoricoTVRepositoryMySQL();
        PagoRepositoryMySQL pagoRepo = new PagoRepositoryMySQL();
        
        SuscripcionRepositoryMySQL suscripcionRepo = new SuscripcionRepositoryMySQL();
        TipoPlanRepositoryMySQL tipoPlanRepo = new TipoPlanRepositoryMySQL();
        PrecioHistoricoTPRepositoryMySQL precioHistRepo = new PrecioHistoricoTPRepositoryMySQL();
        PagoSuscripcionRepositoryMySQL pagoSuscRepo = new PagoSuscripcionRepositoryMySQL();
        
        NotificacionService notifService = new NotificacionService();
        SuscripcionService suscripcionService = new SuscripcionService(
            suscripcionRepo, usuarioRepo, tipoPlanRepo, precioHistRepo, pagoSuscRepo
        ); 

        this.reservaService = new ReservaService(
            reservaRepo, lugarRepo, vehiculoRepo, usuarioRepo, tipoEstadiaRepo, 
            precioRepo, suscripcionService, pagoRepo, notifService
        );

        // Creamos el hilo programado (Pool de 1 hilo)
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Le decimos qué hacer y cada cuánto tiempo
        Runnable tareaAlertas = () -> {
            try {
                reservaService.notificarAlertasDeTiempo(); 
                reservaService.cancelarReservasNoPagadasPorVencer();
            } catch (Exception e) {
                System.err.println("[ERROR EN TAREA PROGRAMADA] " + e.getMessage());
            }
        };

        // Ejecuta la tarea: Arranca a los 0 minutos, y se repite cada 1 minuto
        scheduler.scheduleAtFixedRate(tareaAlertas, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Es vital apagar el hilo cuando detenemos Tomcat para evitar fugas de memoria (Memory Leaks)
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            System.out.println("[SISTEMA] Motor de tareas en segundo plano detenido.");
        }
    }
}