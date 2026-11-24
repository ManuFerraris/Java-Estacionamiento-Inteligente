
package estacionamiento.servlet;

import estacionamiento.domain.Cochera;
import estacionamiento.repository.mysql.CocheraRepositoryMySQL;
import estacionamiento.service.CocheraService;
import estacionamiento.util.AppLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/cocheras-oficina")
public class CocheraServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CocheraService cocheraService;

    @Override
    public void init() throws ServletException {
        this.cocheraService =
            new CocheraService(new CocheraRepositoryMySQL());
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String mensajeExito =
            (String) request.getSession().getAttribute("exito");

        if (mensajeExito != null) {
            request.setAttribute("exito", mensajeExito);
            request.getSession().removeAttribute("exito");
        }

        try {
            List<Cochera> cocheras = cocheraService.obtenerTodas();
            request.setAttribute("listaCocheras", cocheras);

            request.getRequestDispatcher(
                "/WEB-INF/views/cocheras.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            AppLogger.error("Error al cargar la lista de cocheras", e);
            request.setAttribute(
                "error",
                "Error al cargar las cocheras: " + e.getMessage()
            );

            request.getRequestDispatcher(
                "/WEB-INF/views/cocheras.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String accion = request.getParameter("accion");

            if (accion == null || accion.trim().isEmpty()) {
                accion = "crear";
            }

            switch (accion) {

                case "crear": {
                    Cochera nuevaCochera = new Cochera();
                    nuevaCochera.setNombre(request.getParameter("nombre"));
                    nuevaCochera.setDescripcion(request.getParameter("descripcion"));
                    nuevaCochera.setDireccion(request.getParameter("direccion"));

                    cocheraService.guardar(nuevaCochera);

                    request.getSession().setAttribute(
                        "exito",
                        "La cochera fue registrada correctamente."
                    );
                    break;
                }

                case "editar": {
                    Integer codigo = obtenerCodigo(request);

                    Cochera cocheraEditada = new Cochera();
                    cocheraEditada.setNombre(request.getParameter("nombre"));
                    cocheraEditada.setDescripcion(request.getParameter("descripcion"));
                    cocheraEditada.setDireccion(request.getParameter("direccion"));

                    cocheraService.actualizar(codigo, cocheraEditada);

                    request.getSession().setAttribute(
                        "exito",
                        "Cochera actualizada correctamente."
                    );
                    break;
                }

                case "bajaLogica": {
                    Integer codigo = obtenerCodigo(request);

                    cocheraService.darDeBaja(codigo);

                    request.getSession().setAttribute(
                        "exito",
                        "La cochera fue dada de baja correctamente."
                    );
                    break;
                }

                case "altaLogica": {
                    Integer codigo = obtenerCodigo(request);

                    cocheraService.darDeAltaPostBaja(codigo);

                    request.getSession().setAttribute(
                        "exito",
                        "La cochera fue reactivada correctamente."
                    );
                    break;
                }

                default:
                    throw new IllegalArgumentException(
                        "La acción solicitada no es válida."
                    );
            }

            response.sendRedirect(
                request.getContextPath() + "/cocheras-oficina"
            );

        } catch (IllegalArgumentException e) {
            request.setAttribute(
                "error",
                "Error de validación: " + e.getMessage()
            );
            doGet(request, response);

        } catch (Exception e) {
            AppLogger.error(
                "Error inesperado en la gestión de cocheras",
                e
            );

            request.setAttribute(
                "error",
                "Error inesperado: " + e.getMessage()
            );
            doGet(request, response);
        }
    }

    private Integer obtenerCodigo(HttpServletRequest request) {
        String valor = request.getParameter("codigo");

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El código de la cochera es obligatorio."
            );
        }

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "El código de la cochera debe ser numérico."
            );
        }
    }
}
