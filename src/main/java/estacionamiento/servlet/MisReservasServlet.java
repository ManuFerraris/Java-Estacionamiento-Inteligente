package estacionamiento.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
import estacionamiento.service.NotificacionService;

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
    private NotificacionService notiService;

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
            this.pagoRepo,
            this.notiService
        );
        
        this.pagoService = new PagoService(this.pagoRepo);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (cliente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Primera carga
        String accion = request.getParameter("accion");
        if ("verificarCupo".equals(accion)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                int idCochera = Integer.parseInt(request.getParameter("idCochera"));
                LocalDateTime desde = LocalDateTime.parse(request.getParameter("fechaDesde"));
                LocalDateTime hasta = LocalDateTime.parse(request.getParameter("fechaHasta"));
                
                if (!hasta.isAfter(desde)) {
                    response.getWriter().write("{\"disponibles\": -1, \"mensaje\": \"La salida debe ser posterior al ingreso.\"}");
                    return;
                }
                
                long libres = reservaService.consultarDisponibilidad(idCochera, desde, hasta);
                response.getWriter().write("{\"disponibles\": " + libres + "}");
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("{\"disponibles\": 0, \"error\": \"Datos incompletos\"}");
            }
            return;
        }
        
        // carga de la vista
        try {
            List<Cochera> cocheras = cocheraRepo.obtenerTodos();
            request.setAttribute("cocheras", cocheras);

            List<TipoEstadia> tiposEstadia = tipoEstadiaRepo.obtenerTodos();
            request.setAttribute("tiposEstadia", tiposEstadia);

            List<Reserva> misReservas = reservaService.obtenerReservasPorUsuario(cliente.getNumero());
            request.setAttribute("misReservas", misReservas);

            List<Vehiculo> misVehiculos = vehiculoRepo.buscarPorUsuario(cliente.getNumero());
            
            System.out.println("Vehículos reales del usuario " + cliente.getNombre() + ": " + misVehiculos.size());
            request.setAttribute("misVehiculos", misVehiculos);

            request.getRequestDispatcher("/WEB-INF/views/misReservas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar la información del módulo de reservas.");
            request.getRequestDispatcher("/WEB-INF/views/misReservas.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (cliente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            String accion = request.getParameter("accion");

            // Flujo de pago pendiente
            if ("pagarPendiente".equals(accion)) {
                Integer idPago = Integer.parseInt(request.getParameter("idPago"));
                String concepto = request.getParameter("concepto");
                
                Pago pagoPendiente = pagoService.buscarPago(idPago); 
                if (pagoPendiente == null || pagoPendiente.getEstado() == EstadoPago.APROBADO) {
                    throw new IllegalArgumentException("El pago seleccionado no existe o ya se encuentra abonado.");
                }

                MercadoPagoService mpService = new MercadoPagoService();
                String urlCheckout = mpService.crearPreferencia(
                        concepto, 
                        pagoPendiente.getMonto(), 
                        pagoPendiente.getNumero(),
                        cliente.getMail()
                );
                
                response.sendRedirect(urlCheckout);
                return;
            }

            // Flujo de la nueva reserva
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
            
            // Segundo creo el pago de la SEÑA:
            Pago nuevoPagoSenia = new Pago();
            nuevoPagoSenia.setMonto(reserva.getSenia());
            
            // Validamos el caso PREMIUM ($0) ANTES de registrar el pago
            boolean esPremium = nuevoPagoSenia.getMonto().compareTo(BigDecimal.ZERO) == 0;
            
            if (esPremium) {
                nuevoPagoSenia.setEstado(EstadoPago.APROBADO);
                nuevoPagoSenia.setTipoPago(TipoPago.BONIFICADO); 
            } else {
                nuevoPagoSenia.setEstado(EstadoPago.PENDIENTE);
            }

            // Guardamos el pago de la seña y lo asignamos:
            pagoService.registrarPago(nuevoPagoSenia);
            reserva.setPagoSenia(nuevoPagoSenia);

            // Tercero, calculamos si queda SALDO RESTANTE de la estadía
            if (!esPremium) {
                BigDecimal saldoRestante = reservaService.calcularSaldoRestante(reserva);
                if (saldoRestante.compareTo(BigDecimal.ZERO) > 0) {
                    Pago nuevoPagoSaldo = new Pago();
                    nuevoPagoSaldo.setMonto(saldoRestante);
                    nuevoPagoSaldo.setEstado(EstadoPago.PENDIENTE);
                    
                    pagoService.registrarPago(nuevoPagoSaldo);
                    reserva.setPagoSaldo(nuevoPagoSaldo);
                }
            }
            
            // Actualizamos la reserva en BD con ambos pagos (seña y saldo si aplica) vinculados
            reservaService.actualizarReserva(reserva);
            
            // Cuarto valido el caso PREMIUM ($0)
            if (esPremium) {
                request.getSession().setAttribute("exito", "¡Reserva confirmada! (Beneficio Premium aplicado 100% Bonificado).");
                response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
                return; // Cortamos con la ejecucion aca porque solamente se puede redirigir una vez en el servlet y no DOS o mas.
            }
            
            // Quinto, ahora si llamo al servicio de MP para cobrar la SEÑA en caso de que no tenga el plan "Premium".
            MercadoPagoService mpService = new MercadoPagoService();
            String urlCheckout = mpService.crearPreferencia(
                    "Seña Reserva Cochera - " + reserva.getVehiculo().getPatente(), 
                    nuevoPagoSenia.getMonto(), 
                    nuevoPagoSenia.getNumero(),
                    cliente.getMail()
            );
            System.out.println("Estructura mpService creada: " + mpService);
            
            // Finalmente redirijo al ciudadano directo a la pantalla de mercado pago.
            response.sendRedirect(urlCheckout);
            return; 
            
        } catch (IllegalArgumentException e) {
            // Mostramos al usuario el mensaje exacto de nuestras validaciones de negocio
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
            return;
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
