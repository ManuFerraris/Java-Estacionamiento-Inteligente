package estacionamiento.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PagoSuscripcion;
import estacionamiento.domain.TipoPago;
import estacionamiento.repository.mysql.PagoSuscripcionRepositoryMySQL;
import estacionamiento.service.PagoSuscripcionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pagos-suscripciones-oficina")
public class PagoSuscripcionOficinaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private PagoSuscripcionService pagoService;

    @Override
    public void init() throws ServletException {
        this.pagoService = new PagoSuscripcionService(new PagoSuscripcionRepositoryMySQL());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensajeExito = (String) request.getSession().getAttribute("exito");
        if (mensajeExito != null) {
            request.setAttribute("exito", mensajeExito);
            request.getSession().removeAttribute("exito");
        }

        try {
            List<PagoSuscripcion> pagos = pagoService.obtenerTodos();
            request.setAttribute("listaPagos", pagos);
            request.getRequestDispatcher("/WEB-INF/views/pagosSuscripcionesOficina.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar los comprobantes: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/pagosSuscripcionesOficina.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accion = request.getParameter("accion");
            
            // Claves compartidas para ambas acciones
            int numUsuario = Integer.parseInt(request.getParameter("numeroUsuario"));
            int codPlan = Integer.parseInt(request.getParameter("codigoPlan"));
            LocalDateTime fechaDesdeSub = LocalDateTime.parse(request.getParameter("fechaDesdeSuscripcion"));
            LocalDateTime fechaHoraEmision = LocalDateTime.parse(request.getParameter("fechaHoraEmision"));

            switch (accion) {
                case "cobrar":
                    TipoPago tipoPago = TipoPago.valueOf(request.getParameter("tipoPago"));
                    pagoService.registrarCobro(numUsuario, codPlan, fechaDesdeSub, fechaHoraEmision, tipoPago);
                    request.getSession().setAttribute("exito", "Pago registrado exitosamente en el sistema.");
                    break;

                case "anular":
                    pagoService.anularComprobante(numUsuario, codPlan, fechaDesdeSub, fechaHoraEmision);
                    request.getSession().setAttribute("exito", "El comprobante fue anulado.");
                    break;
            }

            response.sendRedirect(request.getContextPath() + "/pagos-suscripciones-oficina");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Validación: " + e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
        }
    }
}