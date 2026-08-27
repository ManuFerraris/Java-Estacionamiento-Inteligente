package estacionamiento.servlet;

import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.service.TipoPlanService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/tipoPlanes")
public class TipoPlanServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private TipoPlanService tipoPlanService;
    
    @Override
    public void init() throws ServletException {
        this.tipoPlanService = new TipoPlanService(new TipoPlanRepositoryMySQL());
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensajeExito = (String) request.getSession().getAttribute("exito");
        if (mensajeExito != null) {
            request.setAttribute("exito", mensajeExito);
            request.getSession().removeAttribute("exito");
        }
        
        try {
            List<TipoPlan> planes = tipoPlanService.obtenerTodosLosTiposDePlan();
            request.setAttribute("listaTiposPlan", planes);
            request.getRequestDispatcher("/WEB-INF/views/tipoPlanes.jsp").forward(request, response);
        } catch(Exception e) {
            request.setAttribute("error", "Error al cargar los planes: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/tipoPlanes.jsp").forward(request, response);
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
                case "bajaLogica":
                    int codigoBaja = Integer.parseInt(request.getParameter("codigo"));
                    tipoPlanService.darDeBaja(codigoBaja);
                    request.getSession().setAttribute("exito", "El plan fue dado de baja correctamente.");
                    break;
                    
                case "altaLogica":
                    int codigoAlta = Integer.parseInt(request.getParameter("codigo"));
                    tipoPlanService.darDeAltaPostBaja(codigoAlta);
                    request.getSession().setAttribute("exito", "El plan fue reactivado correctamente.");
                    break;

                case "editar":
                    int codigoEditar = Integer.parseInt(request.getParameter("codigo"));
                    TipoPlan planEditado = new TipoPlan();
                    planEditado.setNombre(request.getParameter("nombre"));
                    planEditado.setDetalle(request.getParameter("detalle"));
                    
                    tipoPlanService.actualizarTipoPlan(codigoEditar, planEditado);
                    request.getSession().setAttribute("exito", "Plan actualizado correctamente.");
                    break;

                case "crear":
                default:
                    TipoPlan nuevoPlan = new TipoPlan();
                    nuevoPlan.setNombre(request.getParameter("nombre"));
                    nuevoPlan.setDetalle(request.getParameter("detalle"));
                    
                    tipoPlanService.registrarTipoPlan(nuevoPlan);
                    request.getSession().setAttribute("exito", "El plan fue registrado correctamente.");
                    break;
            }
            
            response.sendRedirect(request.getContextPath() + "/tipoPlanes");
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Error de validación: " + e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
        }
    }
}