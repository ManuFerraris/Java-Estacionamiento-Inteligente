package estacionamiento.servlet;

import java.io.IOException;
import java.util.List;

import estacionamiento.domain.Suscripcion;
import estacionamiento.repository.SuscripcionRepository;
import estacionamiento.repository.mysql.SuscripcionRepositoryMySQL;
// import estacionamiento.repository.memoria.SuscripcionRepositoryMemoria; 

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/padron-morosidad")
public class PadronServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private SuscripcionRepository repo;

    @Override
    public void init() throws ServletException {
        // Inicializamos el repositorio de MySQL. 
        // Si quieres probar en memoria, cámbialo por: new SuscripcionRepositoryMemoria();
        this.repo = new SuscripcionRepositoryMySQL(); 
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Capturamos los parámetros que envía el usuario desde los <select> del JSP
        String filtroSuscripcion = req.getParameter("filtroSuscripcion");
        String filtroPago = req.getParameter("filtroPago");

        // 2. Ejecutamos la búsqueda dinámica en la base de datos (o memoria)
        List<Suscripcion> padron = repo.filtrarPadron(filtroSuscripcion, filtroPago);

        // 3. Enviamos la lista filtrada a la vista
        req.setAttribute("padron", padron);
        
        // 4. Enviamos también los filtros para que los <select> mantengan la opción elegida
        req.setAttribute("filtroSuscripcion", filtroSuscripcion);
        req.setAttribute("filtroPago", filtroPago);

        // 5. Redirigimos al archivo JSP (que debe estar suelto en src/main/webapp)
        req.getRequestDispatcher("/padron.jsp").forward(req, resp);
    }
}