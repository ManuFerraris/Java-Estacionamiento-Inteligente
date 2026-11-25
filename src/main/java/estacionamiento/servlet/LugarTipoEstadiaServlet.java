
package estacionamiento.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import estacionamiento.service.LugarTipoEstadiaService;

import estacionamiento.repository.mysql.LugarRepositoryMySQL;
import estacionamiento.repository.mysql.TipoEstadiaRepositoryMySQL;
import estacionamiento.repository.mysql.LugarTipoEstadiaRepositoryMySQL;
import estacionamiento.repository.mysql.ReservaRepositoryMySQL;

import estacionamiento.util.AppLogger;
import estacionamiento.domain.LugarTipoEstadia;
@WebServlet("/LugarTipoEstadiaServlet")
public class LugarTipoEstadiaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private LugarTipoEstadiaService service;

    @Override
    public void init() throws ServletException {

        this.service = new LugarTipoEstadiaService(
            new LugarTipoEstadiaRepositoryMySQL(),
            new LugarRepositoryMySQL(),
            new TipoEstadiaRepositoryMySQL(),
            new ReservaRepositoryMySQL()
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if ("asignar".equals(accion)) {

                int codigoLugar = Integer.parseInt(
                    request.getParameter("codigoLugar")
                );

                int numeroTipoEstadia = Integer.parseInt(
                    request.getParameter("numeroTipoEstadia")
                );

                String fechaString = request.getParameter("fechaDesde");

                if (fechaString == null || fechaString.isBlank()) {
                    throw new IllegalArgumentException(
                        "La fecha de inicio es obligatoria."
                    );
                }

                LocalDateTime fechaDesde = LocalDateTime.parse(fechaString);

                service.asignarTipoEstadiaALugar(
                    codigoLugar,
                    numeroTipoEstadia,
                    fechaDesde
                );

                request.getSession().setAttribute(
                    "exito",
                    "El tipo de estadía se asignó al lugar correctamente."
                );

            } else if ("baja".equals(accion)) {

                int codigoLugar = Integer.parseInt(
                    request.getParameter("codigoLugar")
                );

                int numeroTipoEstadia = Integer.parseInt(
                    request.getParameter("numeroTipoEstadia")
                );

                String fechaString = request.getParameter("fechaDesde");

                LocalDateTime fechaDesde = LocalDateTime.parse(fechaString);

                service.darDeBaja(
                    codigoLugar,
                    numeroTipoEstadia,
                    fechaDesde
                );

                request.getSession().setAttribute(
                    "exito",
                    "La asignación se dio de baja correctamente."
                );

            } else {
                throw new IllegalArgumentException("Acción no válida.");
            }

        } catch (Exception e) {

            AppLogger.error(
                "Error al gestionar la asignación de lugar y tipo de estadía",
                e
            );

            request.getSession().setAttribute(
                "error",
                "Error al gestionar la asignación: " + e.getMessage()
            );
        }

        response.sendRedirect(
            request.getContextPath() + "/LugarServlet"
        );
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int codigoLugar = Integer.parseInt(
                request.getParameter("codigoLugar")
            );

            List<LugarTipoEstadia> asignaciones =
                service.obtenerPorLugar(codigoLugar);

            request.setAttribute("asignaciones", asignaciones);
            request.setAttribute("codigoLugar", codigoLugar);

            request.getRequestDispatcher(
                "/WEB-INF/views/lugar-tipo-estadia.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            AppLogger.error("Error al consultar asignaciones del lugar", e);
            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "No se pudieron consultar las asignaciones."
            );
        }
    }}
