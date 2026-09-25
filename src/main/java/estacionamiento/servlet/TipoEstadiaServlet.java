package estacionamiento.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.TipoEstadiaRepository;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;

@WebServlet("/tipoEstadia")
public class TipoEstadiaServlet extends HttpServlet {
    
    private TipoEstadiaRepository tipoEstadiaRepository;

    @Override
    public void init() throws ServletException {
        // Inicializamos directamente el repositorio MySQL que compartiste
        this.tipoEstadiaRepository = new TipoEstadiaRepositoryMySQL();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Buscamos todos los registros y los enviamos a la vista
            List<TipoEstadia> lista = tipoEstadiaRepository.obtenerTodos();
            request.setAttribute("listaTiposEstadia", lista);
            
            request.getRequestDispatcher("/WEB-INF/views/tipoEstadia.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar los datos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/tipoEstadia.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        try {
            if ("crear".equals(accion)) {
                String descripcion = request.getParameter("descripcion");
                int cupo = Integer.parseInt(request.getParameter("cupo"));
                
                // Instanciamos el objeto. El 'numero' se auto-genera en BD, por lo que usamos el constructor vacío.
                TipoEstadia nuevoTipo = new TipoEstadia();
                nuevoTipo.setDescripcion(descripcion);
                nuevoTipo.setCupo(cupo);
                
                tipoEstadiaRepository.guardar(nuevoTipo);
                request.setAttribute("exito", "Tipo de estadía registrado correctamente.");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error en la operación: " + e.getMessage());
        }
        
        // Recargamos la pantalla con la lista actualizada
        doGet(request, response);
    }
}