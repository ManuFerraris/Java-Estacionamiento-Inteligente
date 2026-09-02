package estacionamiento.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import estacionamiento.domain.Reserva;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;
import estacionamiento.domain.Cochera;

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
            List<Vehiculo> misVehiculos = misReservas.stream()
                    .map(Reserva::getVehiculo)
                    .distinct()
                    .collect(Collectors.toList());
            
            if (misVehiculos.isEmpty()) {
                misVehiculos = vehiculoRepo.obtenerTodos();
            }
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

            reservaService.generarReserva(patente, cliente.getNumero(), idCochera, idTipoEstadia, fechaDesde, fechaHasta);
            
            request.getSession().setAttribute("exito", "¡Reserva confirmada! Tienes tu lugar asegurado en la cochera seleccionada.");

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Ocurrió un error inesperado al procesar la reserva.");
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/mis-reservas-user");
    }
}