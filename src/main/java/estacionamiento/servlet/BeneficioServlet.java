package estacionamiento.servlet;

import estacionamiento.domain.Beneficio;
import estacionamiento.domain.TipoPlan;

import estacionamiento.repository.mysql.BeneficioRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.service.BeneficioService;
import estacionamiento.service.TipoPlanService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/beneficios")
public class BeneficioServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private BeneficioService beneficioService;
	private TipoPlanService tipoPlanService;
	
	@Override
	public void init() throws ServletException {
        this.tipoPlanService = new TipoPlanService(new TipoPlanRepositoryMySQL());
        this.beneficioService = new BeneficioService(new BeneficioRepositoryMySQL(), new TipoPlanRepositoryMySQL());
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
    		List<TipoPlan> planes = tipoPlanService.obtenerTodosLosTiposDePlan();
            request.setAttribute("listaTiposPlan", planes);
            
    		List<Beneficio> beneficios = beneficioService.obtenerTodos();
    		request.setAttribute("listaBeneficios", beneficios);
    		
    		request.getRequestDispatcher("/WEB-INF/views/beneficios.jsp").forward(request, response);
    	}catch(Exception e) {
    		request.setAttribute("error", "Error al cargar los beneficios para este tipo de plan: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/beneficios.jsp").forward(request, response);
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
            int numeroBeneficio;
            String descripcion;
            
            switch (accion) {
            	case "altaLogica":
            		codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    numeroBeneficio = Integer.parseInt(request.getParameter("numero"));
                    
                    beneficioService.darDeAltaPostBaja(codigoPlan, numeroBeneficio);
                    request.getSession().setAttribute("exito", "El beneficio fue reactivado.");
                    break;
            
            	case "bajaLogica":
            		codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    numeroBeneficio = Integer.parseInt(request.getParameter("numero"));
                    
                    beneficioService.darDeBaja(codigoPlan, numeroBeneficio);
                    request.getSession().setAttribute("exito", "El beneficio fue dado de baja.");
                    break;
            	
            	case "editar":
            		codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    numeroBeneficio = Integer.parseInt(request.getParameter("numero"));
                    descripcion = request.getParameter("descripcion");
                    
                    Beneficio beneficioEditado = new Beneficio();
                    beneficioEditado.setDescripcion(descripcion);
                    
                    beneficioService.actualizar(codigoPlan, numeroBeneficio, beneficioEditado);
                    request.getSession().setAttribute("exito", "Beneficio actualizado correctamente.");
                    break;
            	
            	case "crear":
                default:
                	codigoPlan = Integer.parseInt(request.getParameter("codigoPlan"));
                    descripcion = request.getParameter("descripcion");
                    
                    Beneficio nuevoBeneficio = new Beneficio();
                    nuevoBeneficio.setDescripcion(descripcion);
                    
                    beneficioService.registrarBeneficio(codigoPlan, nuevoBeneficio);
                    request.getSession().setAttribute("exito", "Beneficio registrado correctamente.");
                	break;
            }
            response.sendRedirect(request.getContextPath() + "/beneficios");
		}catch(IllegalArgumentException e) {
			request.setAttribute("error", "Error de validación: " + e.getMessage());
            doGet(request, response);
		}catch(Exception e) {
			request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
		}
	}
	
}
