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
import java.util.List;

import estacionamiento.domain.RolesUsuario;
import estacionamiento.domain.Usuario;
import estacionamiento.dto.FilaReporteFinancieroDTO;
import estacionamiento.service.ReporteFinancieroService;
import estacionamiento.repository.mysql.ReporteFinancieroRepository;

@WebServlet("/reporte-financiero-oficina")
public class ReporteFinancieroServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    private ReporteFinancieroRepository reporteFincancieroRepo;
    private ReporteFinancieroService reporteService = new ReporteFinancieroService(reporteFincancieroRepo);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioSesion.getRol() == RolesUsuario.CLIENTE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
            return;
        }

        try {
            String fechaDesdeStr = request.getParameter("fechaDesde");
            String fechaHastaStr = request.getParameter("fechaHasta");
            String origenFiltro = request.getParameter("origenIngreso");

            LocalDateTime fechaDesde = null;
            LocalDateTime fechaHasta = null;

            if (fechaDesdeStr != null && !fechaDesdeStr.isEmpty()) {
                fechaDesde = LocalDate.parse(fechaDesdeStr).atStartOfDay();
            }
            if (fechaHastaStr != null && !fechaHastaStr.isEmpty()) {
                fechaHasta = LocalDate.parse(fechaHastaStr).atTime(LocalTime.MAX);
            }
            if (origenFiltro == null) {
                origenFiltro = "TODOS";
            }

            List<FilaReporteFinancieroDTO> resultados = reporteService.generarReporte(fechaDesde, fechaHasta, origenFiltro);
            
            double totalRecaudado = resultados.stream()
                .filter(f -> "APROBADO".equalsIgnoreCase(f.getEstadoPago()))
                .mapToDouble(f -> f.getMonto().doubleValue())
                .sum();

            request.setAttribute("listaReporte", resultados);
            request.setAttribute("totalRecaudado", totalRecaudado);
            
            request.setAttribute("filtroDesde", fechaDesdeStr);
            request.setAttribute("filtroHasta", fechaHastaStr);
            request.setAttribute("filtroOrigen", origenFiltro);

        } catch (Exception e) {
            request.setAttribute("error", "Error al procesar el reporte: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/reporteFinanciero.jsp").forward(request, response);
    }
}