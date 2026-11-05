package estacionamiento.servlet;

import java.io.IOException;

import estacionamiento.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Destruye la sesión por completo
            }
            response.sendRedirect(request.getContextPath() + "/login");
            
        } catch (Exception e) {
            AppLogger.error("Error inesperado al intentar cerrar la sesión del usuario", e);
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
