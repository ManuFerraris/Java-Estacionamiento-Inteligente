package estacionamiento.servlet;

import estacionamiento.domain.PrecioHistoricoTP;
import estacionamiento.domain.TipoPlan;
import estacionamiento.repository.mysql.PrecioHistoricoTPRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.service.PrecioHistoricoTPService;
import estacionamiento.service.TipoPlanService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/preciosHistoricos-oficina")
public class PrecioHistoricoTPServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private PrecioHistoricoTPService precioService;
    private TipoPlanService tipoPlanService;
    
    @Override
    public void init() throws ServletException {
        this.tipoPlanService = new TipoPlanService(new TipoPlanRepositoryMySQL());
        this.precioService = new PrecioHistoricoTPService(new PrecioHistoricoTPRepositoryMySQL(), new TipoPlanRepositoryMySQL());
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

            List<PrecioHistoricoTP> precios = precioService.obtenerTodos();
            request.setAttribute("listaPrecios", precios);
            
            request.getRequestDispatcher("/WEB-INF/views/preciosHistoricosTP.jsp").forward(request, response);
        } catch(Exception e) {
            request.setAttribute("error", "Error al cargar la pantalla: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/preciosHistoricosTP.jsp").forward(request, response);
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
            
            int codigoPlan;
            BigDecimal precio;
            LocalDateTime fechaDesde;
            
            switch (accion) {
                case "eliminar":
                    codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    // En el form de eliminar, el input se llama "fechaDesde"
                    fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesde"));
                    
                    precioService.eliminar(codigoPlan, fechaDesde);
                    request.getSession().setAttribute("exito", "El registro histórico fue eliminado físicamente.");
                    break;
                
                case "editar":
                    codigoPlan = Integer.parseInt(request.getParameter("codigoPlanHidden"));
                    fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesdeHidden"));
                    precio = new BigDecimal(request.getParameter("precio"));
                    
                    precioService.actualizar(codigoPlan, fechaDesde, precio);
                    request.getSession().setAttribute("exito", "Precio histórico actualizado correctamente.");
                    break;
                
                case "crear":
                default:
                    codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    precio = new BigDecimal(request.getParameter("precio"));

                    precioService.registrarPrecio(codigoPlan, precio);
                    request.getSession().setAttribute("exito", "Nuevo precio histórico registrado.");
                    break;
            }
            
            response.sendRedirect(request.getContextPath() + "/preciosHistoricos-oficina");
            
        } catch(IllegalArgumentException e) {
            request.setAttribute("error", "Error de validación: " + e.getMessage());
            doGet(request, response);
        } catch(Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
        }
    }
}