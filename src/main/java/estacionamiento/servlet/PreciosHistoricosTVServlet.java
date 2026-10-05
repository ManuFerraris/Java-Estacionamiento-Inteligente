package estacionamiento.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import estacionamiento.domain.PrecioHistoricoTV;
import estacionamiento.domain.TipoVehiculo;
import estacionamiento.service.PrecioHistoricoTVService;
import estacionamiento.service.TipoVehiculoService;
import estacionamiento.repository.mysql.TipoVehiculoRepositoryMySQL;
import estacionamiento.repository.mysql.PrecioHistoricoTVRepositoryMySQL;

@WebServlet("/preciosHistoricosTV-oficina")
public class PreciosHistoricosTVServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TipoVehiculoRepositoryMySQL tvMySQL = new TipoVehiculoRepositoryMySQL();
    private PrecioHistoricoTVRepositoryMySQL phTVMySQL = new PrecioHistoricoTVRepositoryMySQL();
    private PrecioHistoricoTVService precioService = new PrecioHistoricoTVService(phTVMySQL, tvMySQL);
    private TipoVehiculoService tipoVehiculoService = new TipoVehiculoService(tvMySQL);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {

            List<TipoVehiculo> listaTiposVehiculo = tipoVehiculoService.obtenerTodosLosTiposDeVehiculo();
            List<PrecioHistoricoTV> listaPrecios = precioService.obtenerTodosOrdenadosPorFechaDesc();

            request.setAttribute("listaTiposVehiculo", listaTiposVehiculo);
            request.setAttribute("listaPrecios", listaPrecios);

        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar los datos: " + e.getMessage());
        }
    	
        request.getRequestDispatcher("/WEB-INF/views/preciosHistoricosTV.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String accion = request.getParameter("accion");

        try {
            if ("crear".equals(accion)) {
                Integer idTipo = Integer.parseInt(request.getParameter("numeroTipoVehiculo"));
                BigDecimal precio = new BigDecimal(request.getParameter("precio"));
                
                precioService.registrarNuevoPrecio(idTipo, precio);
                request.getSession().setAttribute("exito", "El nuevo precio histórico fue registrado correctamente.");

            } else if ("editar".equals(accion)) {
                Integer idTipo = Integer.parseInt(request.getParameter("numeroTipoVehiculoHidden"));
                // El JSP manda la fecha generada por LocalDateTime.toString(), la parseamos directo
                LocalDateTime fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesdeHidden"));
                BigDecimal nuevoPrecio = new BigDecimal(request.getParameter("precio"));
                
                precioService.actualizarPrecio(idTipo, fechaDesde, nuevoPrecio);
                request.getSession().setAttribute("exito", "El precio fue corregido exitosamente.");

            } else if ("eliminar".equals(accion)) {
                Integer idTipo = Integer.parseInt(request.getParameter("numeroTipoVehiculo"));
                LocalDateTime fechaDesde = LocalDateTime.parse(request.getParameter("fechaDesde"));
                
                precioService.eliminarPrecioFisico(idTipo, fechaDesde);
                request.getSession().setAttribute("exito", "El registro fue eliminado permanentemente de la base de datos.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Error en el formato de los números ingresados.");
        } catch (Exception e) {
            request.setAttribute("error", "Error en la operación: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/preciosHistoricosTV-oficina");
        return;
    }
}