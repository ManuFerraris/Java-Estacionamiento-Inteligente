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
import estacionamiento.util.AppLogger;

@WebServlet("/terminal-barrera")
public class TerminalBarreraServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // Seguridad: Solo los empleados de la municipalidad deberían poder abrir la terminal
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioSesion.getRol() == RolesUsuario.CLIENTE) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado. Exclusivo para personal operativo.");
            return;
        }

        try {
            // Despachamos al JSP de la terminal
            request.getRequestDispatcher("/WEB-INF/views/controlBarrera.jsp").forward(request, response);
        } catch (Exception e) {
            AppLogger.error("Error al cargar la terminal de control de barrera", e);
            request.setAttribute("error", "Error al cargar la vista de control.");
            request.getRequestDispatcher("/WEB-INF/views/controlBarrera.jsp").forward(request, response);
        }
    }
}