package estacionamiento.servlet;

import java.io.IOException;

import estacionamiento.domain.Usuario;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private UsuarioService usuarioService;

    @Override
    public void init() throws ServletException {
        this.usuarioService = new UsuarioService(new UsuarioRepositoryMySQL());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Si el usuario ya está logueado, no tiene sentido que vea el Sign Up
        if (request.getSession(false) != null && request.getSession(false).getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String contrasenia = request.getParameter("contrasenia");
            String confirmarContrasenia = request.getParameter("confirmarContrasenia");

            if (contrasenia == null || !contrasenia.equals(confirmarContrasenia)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden.");
            }

            Usuario nuevoCliente = new Usuario();
            nuevoCliente.setNombre(request.getParameter("nombre"));
            nuevoCliente.setApellido(request.getParameter("apellido"));
            nuevoCliente.setNombreUsuario(request.getParameter("nombreUsuario"));
            nuevoCliente.setMail(request.getParameter("mail"));
            nuevoCliente.setMailRecuperacion(request.getParameter("mailRecuperacion"));
            nuevoCliente.setNumeroTelefono(request.getParameter("numeroTelefono"));
            nuevoCliente.setDireccion(request.getParameter("direccion"));
            nuevoCliente.setContrasenia(contrasenia);
            
            // Llamamos al método especializado que fuerza el rol CLIENTE
            usuarioService.registrarCliente(nuevoCliente);
            
            // Si todo sale bien, lo mandamos al login con un mensaje de éxito
            request.setAttribute("exitoRegistro", "¡Cuenta creada con éxito! Ya puedes iniciar sesión.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Ocurrió un error inesperado. Intente nuevamente.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
        }
    }
}
