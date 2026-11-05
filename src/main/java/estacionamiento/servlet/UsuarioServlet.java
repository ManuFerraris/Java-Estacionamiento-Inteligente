package estacionamiento.servlet;

import estacionamiento.domain.Usuario;
import estacionamiento.domain.RolesUsuario;
import estacionamiento.repository.UsuarioRepository;
import estacionamiento.repository.AsignacionCocheraRepository;
import estacionamiento.repository.CocheraRepository;
import estacionamiento.repository.mysql.AsignacionCocheraRepositoryMySQL;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.service.AsignacionCocheraService;
import estacionamiento.service.UsuarioService;
import estacionamiento.util.AppLogger;
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
	private CocheraRepository cocheraRepo;
	private AsignacionCocheraService asignacionService;
	
	@Override
	public void init() throws ServletException {
		UsuarioRepository usuarioRepo = new UsuarioRepositoryMySQL();
		this.usuarioService = new UsuarioService(new UsuarioRepositoryMySQL());
		this.cocheraRepo = new CocheraRepositoryMySQL();
		AsignacionCocheraRepository asignacionRepo = new AsignacionCocheraRepositoryMySQL();
		this.asignacionService = new AsignacionCocheraService(asignacionRepo, usuarioRepo, this.cocheraRepo);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Usuario> usuarios = usuarioService.obtenerTodos();
			request.setAttribute("listaUsuarios", usuarios);
			
			// Cargamos las cocheras y las asignaciones vigentes para la vista
			request.setAttribute("listaCocheras", cocheraRepo.obtenerTodos());
			request.setAttribute("asignacionesActivas", asignacionService.listarAsignacionesActivas());
			
			request.getRequestDispatcher("/WEB-INF/views/usuarios.jsp").forward(request, response);
		} catch (Exception e) {
			AppLogger.error("Error al cargar la lista de usuarios y asignaciones", e);
			request.getSession().setAttribute("error", "Error al cargar los datos: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/views/usuarios.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
		try {
			String accion = request.getParameter("accion");
			if (accion == null || accion.trim().isEmpty()) {
				accion = "crear";
			}

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

				case "asignarCochera":
					int numEncargado = Integer.parseInt(request.getParameter("numeroUsuario"));
					int codCochera = Integer.parseInt(request.getParameter("codigoCochera"));
					asignacionService.asignarEncargadoACochera(numEncargado, codCochera);
					request.getSession().setAttribute("exito", "Cochera asignada al encargado correctamente.");
					break;

				case "finalizarAsignacion":
					int numEncFin = Integer.parseInt(request.getParameter("numeroUsuario"));
					int codCochFin = Integer.parseInt(request.getParameter("codigoCochera"));
					asignacionService.finalizarAsignacion(numEncFin, codCochFin);
					request.getSession().setAttribute("exito", "Se finalizó la asignación del encargado en esa cochera.");
					break;

				case "editar":
					int numeroEditar = Integer.parseInt(request.getParameter("numero"));
					Usuario usuarioEditado = new Usuario();
					usuarioEditado.setNombre(request.getParameter("nombre"));
					usuarioEditado.setApellido(request.getParameter("apellido"));
					usuarioEditado.setNumeroTelefono(request.getParameter("numeroTelefono"));
					usuarioEditado.setDireccion(request.getParameter("direccion"));
					usuarioEditado.setMail(request.getParameter("mail"));
					usuarioEditado.setMailRecuperacion(request.getParameter("mailRecuperacion"));
					usuarioEditado.setNombreUsuario(request.getParameter("nombreUsuario"));
                    
					String rolEditado = request.getParameter("rol");
					if (rolEditado != null && !rolEditado.isEmpty()) {
						usuarioEditado.setRol(RolesUsuario.valueOf(rolEditado));
					}
                    
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

					RolesUsuario rolElegido = RolesUsuario.valueOf(request.getParameter("rol"));
					nuevoUsuario.setRol(rolElegido);
                    
					String passCrear = request.getParameter("contrasenia");
					String confCrear = request.getParameter("confirmarContrasenia");
                    
					if (passCrear == null || passCrear.trim().isEmpty() || !passCrear.equals(confCrear)) {
						throw new IllegalArgumentException("Las contraseñas son obligatorias y deben coincidir.");
					}
					nuevoUsuario.setContrasenia(passCrear);
                    
					// 1. Guardamos el nuevo usuario en la BD (JPA le popula su ID 'numero')
					usuarioService.registrarUsuario(nuevoUsuario);
                    
					// 2. Si es ENCARGADO y seleccionaron una cochera inicial, creamos la relación N a M
					String idCocheraInicial = request.getParameter("codigoCocheraInicial");
					if (rolElegido == RolesUsuario.ENCARGADO && idCocheraInicial != null && !idCocheraInicial.isEmpty()) {
						asignacionService.asignarEncargadoACochera(nuevoUsuario.getNumero(), Integer.parseInt(idCocheraInicial));
					}
                    
					request.getSession().setAttribute("exito", "El usuario fue registrado correctamente.");
					break;
			}
			response.sendRedirect(request.getContextPath() + "/usuarios");
		} catch (IllegalArgumentException e) {
			request.getSession().setAttribute("error", e.getMessage());
			response.sendRedirect(request.getContextPath() + "/usuarios");
		} catch (Exception e) {
			AppLogger.error("Error inesperado en la gestión de usuarios (acción: " + request.getParameter("accion") + ")", e);
			request.getSession().setAttribute("error", "Error inesperado: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/usuarios");
		}
	}
}
