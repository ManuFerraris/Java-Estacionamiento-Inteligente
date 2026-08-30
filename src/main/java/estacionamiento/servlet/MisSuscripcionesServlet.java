package estacionamiento.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.Usuario;
import estacionamiento.domain.TipoPago;
import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.TipoPlan;

import estacionamiento.repository.mysql.SuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTPRepositoryMySQL;
import estacionamiento.repository.mysql.PagoSuscripcionRepositoryMySQL;

import estacionamiento.service.SuscripcionService;
import estacionamiento.service.PagoSuscripcionService;
import estacionamiento.service.TipoPlanService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/mis-suscripciones-user")
public class MisSuscripcionesServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private SuscripcionService suscripcionService;
    private TipoPlanService tipoPlanService;
    private PagoSuscripcionService pagoService;

    @Override
    public void init() throws ServletException {
        this.suscripcionService = new SuscripcionService(
            new SuscripcionRepositoryMySQL(), 
            new UsuarioRepositoryMySQL(), 
            new TipoPlanRepositoryMySQL(), 
            new PrecioHistoricoTPRepositoryMySQL(), 
            new PagoSuscripcionRepositoryMySQL()
        );
        this.tipoPlanService = new TipoPlanService(new TipoPlanRepositoryMySQL());
        this.pagoService = new PagoSuscripcionService(new PagoSuscripcionRepositoryMySQL());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        try {
            Suscripcion activa = suscripcionService.obtenerActivaPorUsuario(cliente.getNumero());
            request.setAttribute("suscripcionActiva", activa);

            List<PagoSuscripcion> pendientes = pagoService.obtenerPendientesPorUsuario(cliente.getNumero());
            request.setAttribute("pagosPendientes", pendientes);

            List<TipoPlan> planes = tipoPlanService.obtenerTodosLosTiposDePlan();
            request.setAttribute("listaPlanes", planes);

            request.getRequestDispatcher("/WEB-INF/views/misSuscripciones.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar la información: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/misSuscripciones.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        String accion = request.getParameter("accion");

        try {
            if ("contratar".equals(accion)) {
                int codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                
                // Llama exactamente a tu método. Cancela la anterior, da de alta y genera la factura
                suscripcionService.registrarOActualizarSuscripcion(cliente.getNumero(), codigoPlan);
                request.getSession().setAttribute("exito", "¡Plan procesado con éxito! Realiza el pago para activarlo.");
            
            } else if ("pagar".equals(accion)) {
                int codPlan = Integer.parseInt(request.getParameter("codPlan"));
                LocalDateTime fechaSub = LocalDateTime.parse(request.getParameter("fechaSub"));
                LocalDateTime fechaEmi = LocalDateTime.parse(request.getParameter("fechaEmi"));
                TipoPago tipoPago = TipoPago.valueOf(request.getParameter("tipoPago"));
                
                // Procesamos el cobro
                pagoService.registrarCobro(cliente.getNumero(), codPlan, fechaSub, fechaEmi, tipoPago);
                request.getSession().setAttribute("exito", "¡Pago procesado correctamente! Tu suscripción está al día.");
            }

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error inesperado procesando la operación.");
            e.printStackTrace();
        }
        
        // Redireccion para evitar el reenvío de formulario (Patrón PRG)
        response.sendRedirect(request.getContextPath() + "/mis-suscripciones-user");
    }
}