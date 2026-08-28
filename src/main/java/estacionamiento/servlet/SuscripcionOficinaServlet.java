package estacionamiento.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.Suscripcion;
import estacionamiento.domain.TipoPlan;
import estacionamiento.domain.Usuario;
import estacionamiento.repository.mysql.SuscripcionRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.repository.mysql.UsuarioRepositoryMySQL;
import estacionamiento.service.SuscripcionService;
import estacionamiento.service.TipoPlanService;
import estacionamiento.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/suscripciones-oficina")
public class SuscripcionOficinaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private SuscripcionService suscripcionService;
    private UsuarioService usuarioService;
    private TipoPlanService tipoPlanService;

    @Override
    public void init() throws ServletException {
        UsuarioRepositoryMySQL usuarioRepo = new UsuarioRepositoryMySQL();
        TipoPlanRepositoryMySQL planRepo = new TipoPlanRepositoryMySQL();
        
        this.usuarioService = new UsuarioService(usuarioRepo);
        this.tipoPlanService = new TipoPlanService(planRepo);
        this.suscripcionService = new SuscripcionService(new SuscripcionRepositoryMySQL(), usuarioRepo, planRepo);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensajeExito = (String) request.getSession().getAttribute("exito");
        if (mensajeExito != null) {
            request.setAttribute("exito", mensajeExito);
            request.getSession().removeAttribute("exito");
        }

        try {
            // Mandamos los datos para llenar los <select> del formulario
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            List<TipoPlan> planes = tipoPlanService.obtenerTodosLosTiposDePlan();
            List<Suscripcion> suscripciones = suscripcionService.obtenerTodas();

            request.setAttribute("listaUsuarios", usuarios);
            request.setAttribute("listaTiposPlan", planes);
            request.setAttribute("listaSuscripciones", suscripciones);

            request.getRequestDispatcher("/WEB-INF/views/suscripcionesOficina.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar la pantalla: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/suscripcionesOficina.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String accion = request.getParameter("accion");
            if (accion == null || accion.trim().isEmpty()) {
                accion = "crear";
            }

            int numeroUsuario;
            int codigoPlan;

            switch (accion) {
                case "cancelar":
                    numeroUsuario = Integer.parseInt(request.getParameter("numeroUsuario"));
                    codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    LocalDateTime fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesde"));
                    
                    suscripcionService.cancelarSuscripcionManual(numeroUsuario, codigoPlan, fechaDesde);
                    request.getSession().setAttribute("exito", "Suscripción cancelada correctamente.");
                    break;

                case "crear":
                default:
                    numeroUsuario = Integer.parseInt(request.getParameter("numeroUsuario"));
                    codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    
                    suscripcionService.registrarOActualizarSuscripcion(numeroUsuario, codigoPlan);
                    request.getSession().setAttribute("exito", "Suscripción asignada con éxito.");
                    break;
            }

            response.sendRedirect(request.getContextPath() + "/suscripciones-oficina");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Error de validación: " + e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
        }
    }
}
