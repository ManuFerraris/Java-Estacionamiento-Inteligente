package estacionamiento.servlet;

import estacionamiento.domain.Cochera;
import estacionamiento.repository.CocheraRepositoryMySQL;
import estacionamiento.service.CocheraService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//La anotación @WebServlet mapea la URL. Cuando alguien entra a "http://localhost:8080/.../cocheras", este Servlet responde.
@WebServlet("/cocheras")
public class CocheraServlet extends HttpServlet {

private static final long serialVersionUID = 1L;
    
    private CocheraService cocheraService;

    @Override
    public void init() throws ServletException {
        // Inicializamos las capas de Servicio y Repositorio manuales
        this.cocheraService = new CocheraService(new CocheraRepositoryMySQL());
    }

    // Método GET: Se ejecuta cuando el usuario entra a la página para VER los datos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 1. Pedimos los datos a la capa de servicio
            List<Cochera> cocheras = cocheraService.obtenerTodas();
            
            // 2. Guardamos la lista en la solicitud (Request) para que la vista JSP la pueda leer
            request.setAttribute("listaCocheras", cocheras);
            
            // 3. Despachamos (redireccionamos internamente) hacia la vista JSP
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
            // 1. Leemos el dato enviado desde el formulario HTML (<input name="numero">)
            String numeroStr = request.getParameter("numero");
            int numero = Integer.parseInt(numeroStr);

            // 2. Creamos la entidad y llamamos al servicio para ejecutar las validaciones
            Cochera nuevaCochera = new Cochera();
            nuevaCochera.setCodigo(numero);
            
            cocheraService.guardar(nuevaCochera);

            // 3. Redirección (Patrón Post/Redirect/Get) para evitar re-envíos duplicados si el usuario refresca la página
            response.sendRedirect(request.getContextPath() + "/cocheras");

        } catch (IllegalArgumentException e) {
            // Si la regla de negocio falla (ej. cochera duplicada), re-enviamos el error al JSP
            request.setAttribute("error", e.getMessage());
            doGet(request, response); // Recargamos la lista y mostramos el mensaje
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            doGet(request, response);
        }
    }
}
