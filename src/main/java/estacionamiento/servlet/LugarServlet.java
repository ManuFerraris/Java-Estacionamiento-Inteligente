package estacionamiento.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import estacionamiento.domain.Cochera;
import estacionamiento.domain.Lugar;
import estacionamiento.domain.TipoEstadia;
import estacionamiento.service.LugarService;
import estacionamiento.service.CocheraService;
import estacionamiento.repository.mysql.LugarRepositoryMySQL;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;

@WebServlet("/LugarServlet")
public class LugarServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private LugarService lugarService;
    private CocheraService cocheraService;
    private TipoEstadiaRepositoryMySQL tipoEstadiaRepo;
    
    @Override
    public void init() throws ServletException {
        this.lugarService = new LugarService(new LugarRepositoryMySQL());
        this.cocheraService = new CocheraService(new CocheraRepositoryMySQL()); 
        this.tipoEstadiaRepo = new TipoEstadiaRepositoryMySQL();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String mensajeExito = (String) request.getSession().getAttribute("exito");
        String mensajeError = (String) request.getSession().getAttribute("error");
        
        if (mensajeExito != null) {
            request.setAttribute("exito", mensajeExito);
            request.getSession().removeAttribute("exito");
        }
        if (mensajeError != null) {
            request.setAttribute("error", mensajeError);
            request.getSession().removeAttribute("error");
        }

        try {
            List<Lugar> listaLugares = lugarService.obtenerTodosLosLugares();
            List<Cochera> listaCocheras = cocheraService.obtenerTodas(); 
            List<TipoEstadia> listaTipos = tipoEstadiaRepo.obtenerTodos();
            
            request.setAttribute("listaLugares", listaLugares);
            request.setAttribute("listaCocheras", listaCocheras); 
            request.setAttribute("listaTiposEstadia", listaTipos);
            
            request.getRequestDispatcher("/WEB-INF/views/lugares.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/lugares.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion == null) accion = "crear";
        
        try {
            switch (accion) {
                case "bajaLogica":
                	Integer codigoBaja = Integer.parseInt(request.getParameter("codigoLugar"));
                	lugarService.darDeBajaLugar(codigoBaja);
                    request.getSession().setAttribute("exito", "El lugar ha sido marcado como inactivo.");
                    break;

                case "editar":
                	Integer codigoEditar = Integer.parseInt(request.getParameter("codigoLugarEdit"));
                    int pisoEdit = Integer.parseInt(request.getParameter("numeroPiso"));
                    String descEdit = request.getParameter("descripcion");
                    int cocheraIdEdit = Integer.parseInt(request.getParameter("codigoCochera"));
                    
                    Cochera cocheraEdit = cocheraService.buscarPorCodigo(cocheraIdEdit);
                    Lugar lugarActualizado = new Lugar(codigoEditar, descEdit, pisoEdit, cocheraEdit);
                    
                    lugarService.actualizarLugar(codigoEditar, lugarActualizado);
                    request.getSession().setAttribute("exito", "El lugar se actualizó correctamente.");
                    break;

                case "crear":
                default:
                    int numeroPiso = Integer.parseInt(request.getParameter("numeroPiso"));
                    String descripcion = request.getParameter("descripcion");
                    int codigoCochera = Integer.parseInt(request.getParameter("codigoCochera"));
                    
                    Cochera cocheraSeleccionada = cocheraService.buscarPorCodigo(codigoCochera); 
                    Lugar nuevoLugar = new Lugar(null, descripcion, numeroPiso, cocheraSeleccionada);
                    
                    lugarService.registrarLugar(nuevoLugar);
                    request.getSession().setAttribute("exito", "Lugar registrado correctamente.");
                    break;
            }
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", "Error de validación: " + e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error inesperado: " + e.getMessage());
        }
        
        // Patrón PRG
        response.sendRedirect(request.getContextPath() + "/LugarServlet");
    }
}