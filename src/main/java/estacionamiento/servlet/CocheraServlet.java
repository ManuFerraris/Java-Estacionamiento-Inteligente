package estacionamiento.servlet;

import estacionamiento.domain.Cochera;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;
import estacionamiento.service.CocheraService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//La anotación @WebServlet mapea la URL. Cuando alguien entra a "http://localhost:8080/.../cocheras", este Servlet responde.
@WebServlet("/cocheras-oficina")
public class CocheraServlet extends HttpServlet {

private static final long serialVersionUID = 1L;
    
    private CocheraService cocheraService;

    @Override
    public void init() throws ServletException {
        this.cocheraService = new CocheraService(new CocheraRepositoryMySQL());
    }

    // Método GET: Se ejecuta cuando el usuario entra a la página para VER los datos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	// Para que el mensaje de exito no aparezca en un refresco de pantalla luego de hacer un alta
    	String mensajeExito = (String) request.getSession().getAttribute("exito");
    	if (mensajeExito != null) {
    		System.out.println("¡DEBUG en consola. Mensaje!: " + mensajeExito);
    	    // Lo pasamos al request para que el JSP lo lea con SweetAlert
    	    request.setAttribute("exito", mensajeExito);
    	    // Aca es donde lo borramos de la sesión para que no vuelva a salir si apretan F5
    	    request.getSession().removeAttribute("exito");
    	}
    	
        try {
            List<Cochera> cocheras = cocheraService.obtenerTodas();
            
            // Guardamos la lista en la solicitud (Request) para que la vista JSP la pueda leer
            request.setAttribute("listaCocheras", cocheras);
            
            // Despachamos (redireccionamos internamente) hacia la vista JSP
            request.getRequestDispatcher("/WEB-INF/views/cocheras.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar las cocheras: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/cocheras.jsp").forward(request, response);
        }
    }

    // Método POST: Se ejecuta cuando el usuario presiona "Guardar Cochera" en el formulario
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
                case "bajaLogica":
                    int codigoBaja = Integer.parseInt(request.getParameter("codigo"));
                    cocheraService.darDeBaja(codigoBaja);
                    request.getSession().setAttribute("exito", "La cochera fue dada de baja correctamente.");
                    break;

                case "editar":
                    int codigoEditar = Integer.parseInt(request.getParameter("codigo"));
                    Cochera cocheraEditada = new Cochera();
                    cocheraEditada.setNombre(request.getParameter("nombre"));
                    cocheraEditada.setDescripcion(request.getParameter("descripcion"));
                    cocheraEditada.setDireccion(request.getParameter("direccion"));
                    
                    cocheraService.actualizar(codigoEditar, cocheraEditada);
                    request.getSession().setAttribute("exito", "Cochera actualizada correctamente.");
                    break;

                case "crear":
                default:
                    Cochera nuevaCochera = new Cochera();
                    nuevaCochera.setNombre(request.getParameter("nombre"));
                    nuevaCochera.setDescripcion(request.getParameter("descripcion"));
                    nuevaCochera.setDireccion(request.getParameter("direccion"));
                    
                    cocheraService.guardar(nuevaCochera);
                    request.getSession().setAttribute("exito", "La cochera fue registrada correctamente.");
                    break;
            }

            // 3. Redirección PRG (Post-Redirect-Get) para evitar re-envíos con F5
            response.sendRedirect(request.getContextPath() + "/cocheras-oficina");
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Error de validación: " + e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
        }
    }
}
