package estacionamiento.servlet;

import estacionamiento.domain.Usuario;
import estacionamiento.domain.RolesUsuario;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private UsuarioService usuarioService;
	
	@Override
	public void init() throws ServletException {
		this.usuarioService = new UsuarioService(new UsuarioRepositoryMySQL());
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mensajeExito = (String) request.getSession().getAttribute("exito");
    	if (mensajeExito != null) {
    		System.out.println("DEBUG en consola. Mensaje: " + mensajeExito);
    	    request.setAttribute("exito", mensajeExito);
    	    request.getSession().removeAttribute("exito");
    	}
    	try {
    		List<Usuario> usuarios = usuarioService.obtenerTodos();
    		request.setAttribute("listaUsuarios", usuarios);
    		request.getRequestDispatcher("/WEB-INF/views/usuarios.jsp").forward(request, response);
    	}catch(Exception e) {
    		request.setAttribute("error", "Error al cargar los usuarios: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/usuarios.jsp").forward(request, response);
    	}
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
    	try {
            // Capturamos la acción solicitada (viene de los input hidden del HTML)
            String accion = request.getParameter("accion");
            if (accion == null || accion.trim().isEmpty()) {
                accion = "crear";
            }

            // Inferimos la accion
            switch (accion) {
	            case "altaLogica":
	                int numeroAlta = Integer.parseInt(request.getParameter("numero"));
	                usuarioService.darDeAltaPostBaja(numeroAlta);
	                request.getSession().setAttribute("exito", "El usuario fue reactivado correctamente.");
	                break;
	                
                case "bajaLogica":
                    int numeroBaja = Integer.parseInt(request.getParameter("numero"));
                    usuarioService.darDeBaja(numeroBaja);
                    request.getSession().setAttribute("exito", "El usuario fue dado de baja correctamente.");
                    break;

                case "editar":
                    int numeroEditar = Integer.parseInt(request.getParameter("numero"));
                    Usuario usuarioEditado = new Usuario();
                    usuarioEditado.setNombre(request.getParameter("nombre"));
                    usuarioEditado.setApellido(request.getParameter("apellido"));
                    usuarioEditado.setNumeroTelefono(request.getParameter("numeroTelefono"));
                    usuarioEditado.setDireccion(request.getParameter("direccion"));
                    usuarioEditado.setMail(request.getParameter("mail"));
                    //usuarioEditado.setFechaBaja(request.getParameter("fechaBaja"));
                    usuarioEditado.setMailRecuperacion(request.getParameter("mailRecuperacion"));
                    usuarioEditado.setNombreUsuario(request.getParameter("nombreUsuario"));
                    
                    // Parseo del Enum Rol
                    String rolEditado = request.getParameter("rol");
                    if (rolEditado != null && !rolEditado.isEmpty()) {
                        usuarioEditado.setRol(RolesUsuario.valueOf(rolEditado));
                    }
                    
                    // Lógica segura de contraseña para edición
                    String passEdit = request.getParameter("contrasenia");
                    String confEdit = request.getParameter("confirmarContrasenia");
                    
                    if (passEdit != null && !passEdit.trim().isEmpty()) {
                        if (!passEdit.equals(confEdit)) {
                            throw new IllegalArgumentException("Las contraseñas no coinciden.");
                        }
                        usuarioEditado.setContrasenia(passEdit);
                    }
                    
                    usuarioService.actualizar(numeroEditar, usuarioEditado);
                    request.getSession().setAttribute("exito", "Usuario actualizado correctamente.");
                    break;

                case "crear":
                default:
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setNombre(request.getParameter("nombre"));
                    nuevoUsuario.setApellido(request.getParameter("apellido"));
                    nuevoUsuario.setNumeroTelefono(request.getParameter("numeroTelefono"));
                    nuevoUsuario.setDireccion(request.getParameter("direccion"));
                    nuevoUsuario.setMail(request.getParameter("mail"));
                    nuevoUsuario.setMailRecuperacion(request.getParameter("mailRecuperacion"));
                    nuevoUsuario.setNombreUsuario(request.getParameter("nombreUsuario"));

                    // Parseo del Enum Rol (Es obligatorio en el alta)
                    nuevoUsuario.setRol(RolesUsuario.valueOf(request.getParameter("rol")));
                    
                    // Validación dura de contraseñas en el backend
                    String passCrear = request.getParameter("contrasenia");
                    String confCrear = request.getParameter("confirmarContrasenia");
                    
                    if (passCrear == null || passCrear.trim().isEmpty() || !passCrear.equals(confCrear)) {
                        throw new IllegalArgumentException("Las contraseñas son obligatorias y deben coincidir.");
                    }
                    nuevoUsuario.setContrasenia(passCrear);
                    
                    usuarioService.registrarUsuario(nuevoUsuario);
                    request.getSession().setAttribute("exito", "El usuario fue registrado correctamente.");
                    break;
            }
            response.sendRedirect(request.getContextPath() + "/usuarios");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Error de validación: " + e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
        }
    }
}
