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
import jakarta.servlet.http.HttpSession;

@WebServlet("/perfil-user")
public class PerfilUserServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private UsuarioService usuarioService;

    @Override
    public void init() throws ServletException {
        this.usuarioService = new UsuarioService(new UsuarioRepositoryMySQL());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/perfilUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        try {
            // Validamos las contraseñas en el backend antes de armar el objeto
            String nuevaContrasenia = request.getParameter("contrasenia");
            String confirmarContrasenia = request.getParameter("confirmarContrasenia");

            if (nuevaContrasenia != null && !nuevaContrasenia.trim().isEmpty()) {
                if (!nuevaContrasenia.equals(confirmarContrasenia)) {
                    throw new IllegalArgumentException("Las contraseñas nuevas no coinciden.");
                }
            }

            // Armamos el objeto con los datos modificados
            Usuario usuarioEditado = new Usuario();
            usuarioEditado.setNombre(request.getParameter("nombre"));
            usuarioEditado.setApellido(request.getParameter("apellido"));
            usuarioEditado.setMail(request.getParameter("mail"));
            usuarioEditado.setNumeroTelefono(request.getParameter("numeroTelefono"));
            usuarioEditado.setDireccion(request.getParameter("direccion"));
            
            // Si el campo de contraseña viene vacío, tu UsuarioService ya sabe que debe conservar la vieja
            usuarioEditado.setContrasenia(nuevaContrasenia);

            // Estos datos son inmutables para el cliente, los copiamos de la sesión actual
            usuarioEditado.setNombreUsuario(usuarioLogueado.getNombreUsuario());
            usuarioEditado.setRol(usuarioLogueado.getRol());
            usuarioEditado.setMailRecuperacion(usuarioLogueado.getMailRecuperacion());

            usuarioService.actualizar(usuarioLogueado.getNumero(), usuarioEditado);

            usuarioLogueado.setNombre(usuarioEditado.getNombre());
            usuarioLogueado.setApellido(usuarioEditado.getApellido());
            usuarioLogueado.setMail(usuarioEditado.getMail());
            usuarioLogueado.setNumeroTelefono(usuarioEditado.getNumeroTelefono());
            usuarioLogueado.setDireccion(usuarioEditado.getDireccion());
            
            session.setAttribute("usuarioLogueado", usuarioLogueado);
            session.setAttribute("exito", "¡Tu perfil ha sido actualizado correctamente!");

        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("error", "Error inesperado al actualizar el perfil.");
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/perfil-user");
    }
}