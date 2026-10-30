package estacionamiento.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import estacionamiento.domain.RolesUsuario;
import estacionamiento.domain.Usuario;

@WebServlet("/reportes-oficina")
public class ReportesDashboardServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");

	     if (usuarioSesion.getRol() != RolesUsuario.ADMIN) {
	         response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Área exclusiva de Administración.");
	         return;
	     }

        request.getRequestDispatcher("/WEB-INF/views/dashboardReportes.jsp").forward(request, response);
    }
}