
package estacionamiento.servlet;

import java.io.IOException;
import java.util.List;

import estacionamiento.domain.TipoEstadia;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;
import estacionamiento.service.TipoEstadiaService;
import estacionamiento.util.AppLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tipoEstadia")
public class TipoEstadiaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TipoEstadiaService tipoEstadiaService;

    @Override
    public void init() throws ServletException {
        this.tipoEstadiaService = new TipoEstadiaService(
            new TipoEstadiaRepositoryMySQL()
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<TipoEstadia> lista =
                tipoEstadiaService.obtenerTodos();

            request.setAttribute("listaTiposEstadia", lista);

        } catch (Exception e) {
            AppLogger.error(
                "Error al cargar los tipos de estadía", e
            );
            request.setAttribute(
                "error", "Error al cargar los datos: " + e.getMessage()
            );
        }

        request.getRequestDispatcher(
            "/WEB-INF/views/tipoEstadia.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String accion = request.getParameter("accion");

            switch (accion == null ? "" : accion) {

                case "crear": {
                    String descripcion =
                        request.getParameter("descripcion");

                    int cupo = Integer.parseInt(
                        request.getParameter("cupo")
                    );

                    TipoEstadia nuevoTipo = new TipoEstadia();
                    nuevoTipo.setDescripcion(descripcion);
                    nuevoTipo.setCupo(cupo);

                    tipoEstadiaService.registrarTipoEstadia(nuevoTipo);

                    request.getSession().setAttribute(
                        "exito",
                        "Tipo de estadía registrado correctamente."
                    );
                    break;
                }

                case "editar": {
                    int numero = Integer.parseInt(
                        request.getParameter("numero")
                    );

                    String descripcion =
                        request.getParameter("descripcion");

                    int cupo = Integer.parseInt(
                        request.getParameter("cupo")
                    );

                    TipoEstadia nuevosDatos = new TipoEstadia();
                    nuevosDatos.setDescripcion(descripcion);
                    nuevosDatos.setCupo(cupo);

                    tipoEstadiaService.actualizarTipoEstadia(
                        numero, nuevosDatos
                    );

                    request.getSession().setAttribute(
                        "exito",
                        "Tipo de estadía actualizado correctamente."
                    );
                    break;
                }

                case "eliminar": {
                    int numero = Integer.parseInt(
                        request.getParameter("numero")
                    );

                    tipoEstadiaService.eliminarTipoEstadia(numero);

                    request.getSession().setAttribute(
                        "exito",
                        "Tipo de estadía eliminado correctamente."
                    );
                    break;
                }

                default:
                    throw new IllegalArgumentException(
                        "Acción no válida: " + accion
                    );
            }

            response.sendRedirect(
                request.getContextPath() + "/tipoEstadia"
            );

        } catch (NumberFormatException e) {
            request.setAttribute(
                "error", "El número y el cupo deben ser valores válidos."
            );
            doGet(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);

        } catch (Exception e) {

            System.err.println("ERROR AL GESTIONAR TIPO DE ESTADIA:");
            e.printStackTrace();

            request.setAttribute(
                "error",
                "No se pudo completar la operación: " + e.getMessage()
            );

            doGet(request, response);
        }
    }
}