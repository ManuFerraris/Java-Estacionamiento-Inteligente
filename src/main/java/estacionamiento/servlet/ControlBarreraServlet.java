package estacionamiento.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import estacionamiento.domain.claves.ReservaId;
import estacionamiento.dto.SalidaDTO;
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

import estacionamiento.service.ReservaService;
import estacionamiento.service.SuscripcionService;
import estacionamiento.service.NotificacionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/control-barrera")
public class ControlBarreraServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;

    private ReservaService reservaService;
    private VehiculoRepositoryMySQL vehiculoRepo;
    private TipoEstadiaRepositoryMySQL tipoEstadiaRepo;
    private ReservaRepositoryMySQL reservaRepo;
    private PagoRepositoryMySQL pagoRepo;
    private NotificacionService notiService;

    @Override
    public void init() throws ServletException {
    	this.vehiculoRepo = new VehiculoRepositoryMySQL();
        this.tipoEstadiaRepo = new TipoEstadiaRepositoryMySQL();
        this.reservaRepo = new ReservaRepositoryMySQL();
        this.pagoRepo = new PagoRepositoryMySQL();
        
        SuscripcionService suscripcionService = new SuscripcionService(
            new SuscripcionRepositoryMySQL(), 
            new UsuarioRepositoryMySQL(), 
            new TipoPlanRepositoryMySQL(), 
            new PrecioHistoricoTPRepositoryMySQL(), 
            new PagoSuscripcionRepositoryMySQL() 
        );
        
        this.reservaService = new ReservaService(
            this.reservaRepo, 
            new LugarRepositoryMySQL(), 
            this.vehiculoRepo, 
            new UsuarioRepositoryMySQL(), 
            this.tipoEstadiaRepo, 
            new PrecioHistoricoTVRepositoryMySQL(), 
            suscripcionService,
            this.pagoRepo,
            this.notiService
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Capturamos el nuevo parámetro que nos dice qué hacer
            String operacion = request.getParameter("operacion"); // "INGRESO" o "SALIDA"
            String paramDefinitiva = request.getParameter("esDefinitiva"); // Si es una salida temporal o definitiva
            boolean esDefinitiva = paramDefinitiva == null || Boolean.parseBoolean(paramDefinitiva);
            
            String patente = request.getParameter("patente").trim();
            Integer numeroUsuario = Integer.parseInt(request.getParameter("numeroUsuario").trim());
            Integer numeroTipoEstadia = Integer.parseInt(request.getParameter("numeroTipoEstadia").trim());
            LocalDateTime fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesde").trim());
            
            // Mi DEBUG
            System.out.println("==== MI DEBUG DE DATOS PARA BUSCAR EN MYSQL ====");
            System.out.println("Patente: [" + patente + "]");
            System.out.println("Usuario: [" + numeroUsuario + "]");
            System.out.println("Tipo Estadia: [" + numeroTipoEstadia + "]");
            System.out.println("Fecha Desde: [" + fechaDesde + "]");
            
            ReservaId idReserva = new ReservaId(patente, numeroUsuario, numeroTipoEstadia, fechaDesde);
            
            if ("INGRESO".equals(operacion)) {
                reservaService.registrarIngreso(idReserva);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"accion\": \"ABRIR_BARRERA\", \"mensaje\": \"Ingreso autorizado. ¡Bienvenido!\"}");
                
            } else if ("SALIDA".equals(operacion)) {
                // Evaluamos si puede salir (pasamos true porque el QR de la puerta final asume salida definitiva)
                SalidaDTO resultado = reservaService.procesarIntentoSalida(idReserva, esDefinitiva);
                
                if (!resultado.isBarreraAbierta()) {
                    response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED); // HTTP 402
                    out.print("{\"accion\": \"COBRAR\", \"monto\": " + resultado.getMontoDeuda() + ", \"mensaje\": \"" + resultado.getMensaje() + "\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"accion\": \"ABRIR_BARRERA\", \"mensaje\": \"" + resultado.getMensaje() + "\"}");
                }
                
            } else if ("COBRAR_MULTA".equals(operacion)) {
                // El municipal apretó el botón de cobro en efectivo
                SalidaDTO resultado = reservaService.cobrarYLiberarBarrera(idReserva);
                
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"accion\": \"ABRIR_BARRERA\", \"mensaje\": \"" + resultado.getMensaje() + "\"}");
                
            } else {
                throw new IllegalArgumentException("Operación de barrera no reconocida.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("==== ERROR LÓGICO EN BARRERA ====");
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno en el servidor de cocheras.\"}");
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
    
}
