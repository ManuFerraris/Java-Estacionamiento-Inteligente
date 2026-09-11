package estacionamiento.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.domain.TipoPago;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;
import estacionamiento.domain.Cochera;
import estacionamiento.domain.EstadoPago;
import estacionamiento.domain.Pago;
import estacionamiento.repository.mysql.LugarRepositoryMySQL;
import estacionamiento.repository.mysql.PagoSuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTPRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTVRepositoryMySQL;
import estacionamiento.repository.mysql.ReservaRepositoryMySQL;
import estacionamiento.repository.mysql.SuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.repository.mysql.VehiculoRepositoryMySQL;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;
import estacionamiento.repository.mysql.PagoRepositoryMySQL;

import estacionamiento.service.ReservaService;
import estacionamiento.service.SuscripcionService;
import estacionamiento.service.MercadoPagoService;
import estacionamiento.service.PagoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/mis-reservas-user")
public class MisReservasServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private ReservaService reservaService;
    private VehiculoRepositoryMySQL vehiculoRepo;
    private TipoEstadiaRepositoryMySQL tipoEstadiaRepo;
    private ReservaRepositoryMySQL reservaRepo;
    private CocheraRepositoryMySQL cocheraRepo;
    private PagoRepositoryMySQL pagoRepo;
    private PagoService pagoService;

    @Override
    public void init() throws ServletException {
        this.vehiculoRepo = new VehiculoRepositoryMySQL();
        this.tipoEstadiaRepo = new TipoEstadiaRepositoryMySQL();
        this.reservaRepo = new ReservaRepositoryMySQL();
        this.cocheraRepo = new CocheraRepositoryMySQL();
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
        
        this.pagoService = new PagoService(this.pagoRepo);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        try {
            // Cargar las cocheras disponibles
            List<Cochera> cocheras = cocheraRepo.obtenerTodos();
            request.setAttribute("cocheras", cocheras);

            // Cargar el catálogo de tipos de estadía
            List<TipoEstadia> tiposEstadia = tipoEstadiaRepo.obtenerTodos();
            request.setAttribute("tiposEstadia", tiposEstadia);

            // Cargar el historial de reservas filtrando por usuario
            List<Reserva> misReservas = reservaRepo.obtenerTodos().stream()
                    .filter(r -> r.getUsuario().getNumero().equals(cliente.getNumero()))
                    .collect(Collectors.toList());
            request.setAttribute("misReservas", misReservas);

            // Extraer los vehículos
            List<Vehiculo> misVehiculos = vehiculoRepo.buscarPorUsuario(cliente.getNumero());
            
            System.out.println("Vehículos reales del usuario " + cliente.getNombre() + ": " + misVehiculos.size());
            request.setAttribute("misVehiculos", misVehiculos);

            request.getRequestDispatcher("/WEB-INF/views/misReservas.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar la información del módulo de reservas.");
            request.getRequestDispatcher("/WEB-INF/views/misReservas.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        
        try {
            String patente = request.getParameter("patente");
            Integer idCochera = Integer.parseInt(request.getParameter("idCochera"));
            Integer idTipoEstadia = Integer.parseInt(request.getParameter("idTipoEstadia"));
            
            LocalDateTime fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesde"));
            LocalDateTime fechaHasta = LocalDateTime.parse(request.getParameter("fechaHasta"));
            
            if (fechaHasta.isBefore(fechaDesde) || fechaHasta.isEqual(fechaDesde)) {
                throw new IllegalArgumentException("La fecha y hora de salida debe ser posterior a la de ingreso.");
            }

            // Primero creo la reserva:
            Reserva reserva = reservaService.generarReserva(patente, cliente.getNumero(), idCochera, idTipoEstadia, fechaDesde, fechaHasta);
            
            // Segundo creo el pago:
            Pago nuevoPago = new Pago();
            nuevoPago.setMonto(reserva.getSenia());
            
            // Validamos el caso PREMIUM ($0) ANTES de registrar el pago
            boolean esPremium = nuevoPago.getMonto().compareTo(BigDecimal.ZERO) == 0;
            
            if (esPremium) {
                nuevoPago.setEstado(EstadoPago.APROBADO);
                // El PagoService exige que un pago APROBADO tenga TipoPago.
                // Creo el BONIFICADO para diferenciar porque pago 0.
                nuevoPago.setTipoPago(TipoPago.BONIFICADO); 
            } else {
                nuevoPago.setEstado(EstadoPago.PENDIENTE);
            }

            // Finalmente lo guardamos:
            pagoService.registrarPago(nuevoPago);
            
            // Tercero, asigno el pago en pagoSenia de reserva y lo guardo
            reserva.setPagoSenia(nuevoPago);
            reservaService.actualizarReserva(reserva);
            
            // Cuarto valido el caso PREMIUM ($0)
            if (esPremium) {
            	request.getSession().setAttribute("exito", "¡Reserva confirmada! (Beneficio Premium aplicado 100% Bonificado).");
                response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
                return; // Cortamos con la ejecucion aca porque solamente se puede redirigir una vez en el servlet y no DOS o mas.
            }
            
            // Quinto, ahora si llamo al servicio de MP en caso de que tenga que pagar porque no tiene el plan "Premium".
            MercadoPagoService mpService = new MercadoPagoService();
            String urlCheckout = mpService.crearPreferencia(
            		"Seña Reserva Cochera - " + reserva.getVehiculo().getPatente(), 
            	    nuevoPago.getMonto(), 
            	    nuevoPago.getNumero(),
            	    cliente.getMail()
            );
            
            // Finalmente redirijo al ciudadano directo a la pantalla de mercado pago.
            // Esta pantalla la provee la API de MP.
            response.sendRedirect(urlCheckout);
            return; // Nuevamente, cortamos el flujo de redireccion.
            
        } catch (com.mercadopago.exceptions.MPApiException mpEx) {
            System.err.println("=== ERROR DE MERCADO PAGO ===");
            System.err.println("Status Code: " + mpEx.getApiResponse().getStatusCode());
            System.err.println("Response: " + mpEx.getApiResponse().getContent());
            System.err.println("=============================");
            
            request.getSession().setAttribute("error", "Error en la pasarela de pagos. Contacte al administrador.");
            response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
            return;
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Ocurrió un error inesperado al procesar la reserva.");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
            return;
        }
    }
}
