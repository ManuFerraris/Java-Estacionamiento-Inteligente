package estacionamiento.servlet;

import java.io.IOException;
import java.util.List;

import estacionamiento.domain.TipoVehiculo;
import estacionamiento.domain.Usuario;
import estacionamiento.domain.Vehiculo;

import estacionamiento.repository.mysql.TipoVehiculoRepositoryMySQL;
import estacionamiento.repository.mysql.VehiculoRepositoryMySQL;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/mis-vehiculos-user")
public class MisVehiculosServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private VehiculoRepositoryMySQL vehiculoRepo;
    private TipoVehiculoRepositoryMySQL tipoVehiculoRepo;

    @Override
    public void init() throws ServletException {
        this.vehiculoRepo = new VehiculoRepositoryMySQL();
        this.tipoVehiculoRepo = new TipoVehiculoRepositoryMySQL(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        try {
            List<TipoVehiculo> tiposVehiculo = tipoVehiculoRepo.obtenerTodos();
            request.setAttribute("tiposVehiculo", tiposVehiculo);

            List<Vehiculo> misVehiculos = vehiculoRepo.buscarPorUsuario(cliente.getNumero());
            request.setAttribute("misVehiculos", misVehiculos);

            request.getRequestDispatcher("/WEB-INF/views/misVehiculos.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar tu flota de vehículos.");
            request.getRequestDispatcher("/WEB-INF/views/misVehiculos.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        String accion = request.getParameter("accion");
        
        try {
            if ("agregar".equals(accion)) {
                String patente = request.getParameter("patente").toUpperCase().trim();
                String descripcion = request.getParameter("descripcion");
                Integer idTipoVehiculo = Integer.parseInt(request.getParameter("idTipoVehiculo"));

                if (vehiculoRepo.buscarPorPatente(patente) != null) {
                    throw new IllegalArgumentException("La patente " + patente + " ya se encuentra registrada en el sistema.");
                }

                TipoVehiculo tipoSeleccionado = tipoVehiculoRepo.buscarPorClave(idTipoVehiculo);
                if (tipoSeleccionado == null) {
                    throw new IllegalArgumentException("El tipo de vehículo seleccionado no es válido.");
                }

                Vehiculo nuevoVehiculo = new Vehiculo(patente, descripcion, tipoSeleccionado);
                nuevoVehiculo.setUsuario(cliente); // Relacion con el usuario

                vehiculoRepo.guardar(nuevoVehiculo);
                request.getSession().setAttribute("exito", "Vehículo registrado correctamente.");

            } else if ("eliminar".equals(accion)) {
                String patente = request.getParameter("patente");
                
                Vehiculo v = vehiculoRepo.buscarPorPatente(patente);
                if (v != null && v.getUsuario().getNumero().equals(cliente.getNumero())) {
                    vehiculoRepo.eliminar(patente);
                    request.getSession().setAttribute("exito", "Vehículo eliminado de tu flota.");
                } else {
                    throw new IllegalArgumentException("No tienes permisos para eliminar este vehículo.");
                }
            }

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Ocurrió un error inesperado. Verifica si el vehículo tiene reservas asociadas.");
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/mis-vehiculos-user");
    }
}