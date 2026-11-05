package estacionamiento.servlet;

import java.io.IOException;

import estacionamiento.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/inicio-user")
public class InicioUserServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
            // A futuro, acá cargaremos la lista de suscripciones activas del cliente para mostrarlas en el dashboard
            request.getRequestDispatcher("/WEB-INF/views/inicioUser.jsp").forward(request, response);
            
        } catch (Exception e) {
            AppLogger.error("Error crítico al cargar el dashboard del usuario (inicio-user)", e);
            request.setAttribute("error", "Error al cargar la página principal.");
            request.getRequestDispatcher("/WEB-INF/views/inicioUser.jsp").forward(request, response);
        }
    }
}