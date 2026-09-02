package estacionamiento.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.claves.ReservaId;
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
            this.pagoRepo
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
            
            String patente = request.getParameter("patente");
            Integer numeroUsuario = Integer.parseInt(request.getParameter("numeroUsuario"));
            Integer numeroTipoEstadia = Integer.parseInt(request.getParameter("numeroTipoEstadia"));
            // Asumimos que guardaste la fecha ISO en el QR para reconstruir la clave
            LocalDateTime fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesde"));
            
            // ATENCIÓN: Asegúrate de reconstruir el ReservaId con los campos que realmente definiste 
            // en tu clase @EmbeddedId. Si usas idTipoEstadia, tendrás que pasarlo en el QR también.
            ReservaId idReserva = new ReservaId(patente, numeroUsuario, numeroTipoEstadia, fechaDesde);
            
            if ("INGRESO".equals(operacion)) {
                
                reservaService.registrarIngreso(idReserva);
                
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"accion\": \"ABRIR_BARRERA\", \"mensaje\": \"Ingreso autorizado. ¡Bienvenido!\"}");
                
            } else if ("SALIDA".equals(operacion)) {
                
                reservaService.registrarSalida(idReserva, true);
                Reserva reservaActualizada = reservaRepo.buscarPorClave(idReserva);
                
                if (reservaActualizada.getPagoSaldo() != null && "PENDIENTE".equals(reservaActualizada.getPagoSaldo().getEstado())) {
                    response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
                    out.print("{\"accion\": \"COBRAR\", \"monto\": " + reservaActualizada.getPagoSaldo().getMonto() + ", \"mensaje\": \"Tiempo de cortesía excedido. Debe abonar saldo para salir.\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"accion\": \"ABRIR_BARRERA\", \"mensaje\": \"Salida registrada exitosamente. ¡Buen viaje!\"}");
                }
                
            } else {
                throw new IllegalArgumentException("Operación de barrera no reconocida.");
            }
            
        } catch (IllegalArgumentException e) {
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
