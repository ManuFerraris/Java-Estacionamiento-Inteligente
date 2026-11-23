package estacionamiento.servlet;

import java.io.IOException;

import estacionamiento.domain.RolesUsuario;
import estacionamiento.domain.Usuario;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.service.UsuarioService;
import estacionamiento.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private UsuarioService usuarioService;

    @Override
    public void init() throws ServletException {
        this.usuarioService = new UsuarioService(new UsuarioRepositoryMySQL());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Si ya está logueado, lo sacamos del login
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
            redirigirSegunRol(u, request, response);
            return;
        }
        
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasenia = request.getParameter("contrasenia");

        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(nombreUsuario, contrasenia);
            
            // Creamos la sesión y guardamos el objeto EXACTAMENTE con la clave que busca el Filtro
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuarioAutenticado);
            
            redirigirSegunRol(usuarioAutenticado, request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } catch (Exception e) {
        	AppLogger.error("Error inesperado en el servidor al intentar autenticar al usuario: " + nombreUsuario, e);
            request.setAttribute("error", "Error inesperado en el servidor.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
    
    private void redirigirSegunRol(Usuario u, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if (u.getRol() == RolesUsuario.ADMIN || u.getRol() == RolesUsuario.TRABAJADOR) {
            // Empleados administrativos de la municipalidad
            response.sendRedirect(request.getContextPath() + "/dashboard-oficina");
        } else if (u.getRol() == RolesUsuario.ENCARGADO) {
            // Operador físico de la cochera
            response.sendRedirect(request.getContextPath() + "/panel-encargado");
        } else {
            // Ciudadano / Cliente
            response.sendRedirect(request.getContextPath() + "/inicio-user");
        }
    }
}
