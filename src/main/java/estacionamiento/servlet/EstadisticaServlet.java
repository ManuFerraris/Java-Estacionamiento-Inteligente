package estacionamiento.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import estacionamiento.domain.RolesUsuario;
import estacionamiento.domain.Usuario;
import estacionamiento.dto.EstadisticaResumenDTO;
import estacionamiento.service.EstadisticaService;
import estacionamiento.util.AppLogger;

@WebServlet("/reporte-demanda-oficina")
public class EstadisticaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private EstadisticaService estadisticaService = new EstadisticaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioSesion.getRol() != RolesUsuario.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
            return;
        }

        try {
            String desdeStr = request.getParameter("desde");
            String hastaStr = request.getParameter("hasta");
            String tipoVehiculo = request.getParameter("tipoVehiculo");
            String estado = request.getParameter("estado");

            LocalDateTime desde = null;
            LocalDateTime hasta = null;

            if (desdeStr != null && !desdeStr.isEmpty()) {
                desde = LocalDate.parse(desdeStr).atStartOfDay();
            }

            if (hastaStr != null && !hastaStr.isEmpty()) {
                hasta = LocalDate.parse(hastaStr).atTime(LocalTime.MAX);
            }

            if (tipoVehiculo != null && tipoVehiculo.isEmpty()) {
                tipoVehiculo = null;
            }

            if (estado != null && estado.isEmpty()) {
                estado = null;
            }

            EstadisticaResumenDTO resumen = estadisticaService.obtenerResumen(
                    desde, hasta, tipoVehiculo, estado);

            request.setAttribute("resumen", resumen);

            request.setAttribute("estadisticasPorVehiculo",
                    estadisticaService.obtenerReservasPorTipoVehiculo(desde, hasta, estado));

            request.setAttribute("estadisticasPorEstado",
                    estadisticaService.obtenerReservasPorEstado(desde, hasta, tipoVehiculo));

            request.setAttribute("filtroDesde", desdeStr);
            request.setAttribute("filtroHasta", hastaStr);
            request.setAttribute("filtroTipoVehiculo", tipoVehiculo);
            request.setAttribute("filtroEstado", estado);

        } catch (Exception e) {
            AppLogger.error("Error al generar las estadísticas de demanda", e);
            request.setAttribute("error",
                    "Error al procesar las estadísticas: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/estadisticasDemanda.jsp")
                .forward(request, response);
    }
}