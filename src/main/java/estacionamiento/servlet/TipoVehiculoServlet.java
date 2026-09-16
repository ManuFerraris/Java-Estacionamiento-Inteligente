package estacionamiento.servlet;

import java.io.IOException;
import java.util.List;

import estacionamiento.domain.TipoVehiculo;
import estacionamiento.repository.mysql.TipoVehiculoRepositoryMySQL;
import estacionamiento.service.TipoVehiculoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tiposVehiculo-oficina")
public class TipoVehiculoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TipoVehiculoService tipoVehiculoService;

    @Override
    public void init() throws ServletException {
        this.tipoVehiculoService =
                new TipoVehiculoService(
                        new TipoVehiculoRepositoryMySQL()
                );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<TipoVehiculo> tiposVehiculo =
                    tipoVehiculoService.obtenerTodosLosTiposDeVehiculo();

            request.setAttribute(
                    "listaTiposVehiculo",
                    tiposVehiculo
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/tiposVehiculo.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            request.setAttribute(
                    "error",
                    "Error al cargar los tipos de vehículo: "
                    + e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/tiposVehiculo.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String accion = request.getParameter("accion");

            if (accion == null || accion.trim().isEmpty()) {
                accion = "crear";
            }

            switch (accion) {

                case "crear":

                    String nombreCrear =
                            request.getParameter("nombre");

                    TipoVehiculo nuevoTipoVehiculo =
                            new TipoVehiculo();

                    nuevoTipoVehiculo.setNombre(nombreCrear);

                    tipoVehiculoService.registrarTipoVehiculo(
                            nuevoTipoVehiculo
                    );

                    request.getSession().setAttribute(
                            "exito",
                            "Tipo de vehículo registrado correctamente."
                    );

                    break;


                case "editar":

                    int numeroEditar =
                            Integer.parseInt(
                                    request.getParameter("numero")
                            );

                    String nombreEditar =
                            request.getParameter("nombre");

                    TipoVehiculo tipoVehiculoEditar =
                            new TipoVehiculo();

                    tipoVehiculoEditar.setNombre(nombreEditar);

                    tipoVehiculoService.actualizarTipoVehiculo(
                            numeroEditar,
                            tipoVehiculoEditar
                    );

                    request.getSession().setAttribute(
                            "exito",
                            "Tipo de vehículo actualizado correctamente."
                    );

                    break;


                case "eliminar":

                    int numeroEliminar =
                            Integer.parseInt(
                                    request.getParameter("numero")
                            );

                    tipoVehiculoService.eliminarTipoVehiculo(
                            numeroEliminar
                    );

                    request.getSession().setAttribute(
                            "exito",
                            "Tipo de vehículo eliminado correctamente."
                    );

                    break;


                default:

                    throw new IllegalArgumentException(
                            "Acción no válida: " + accion
                    );
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/tiposVehiculo-oficina"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    "Error de validación: " + e.getMessage()
            );

            doGet(request, response);

        } catch (Exception e) {

            request.setAttribute(
                    "error",
                    "Error inesperado: " + e.getMessage()
            );

            doGet(request, response);
        }
    }
}