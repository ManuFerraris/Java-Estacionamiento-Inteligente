package estacionamiento.servlet;

import estacionamiento.domain.Beneficio;
import estacionamiento.domain.TipoPlan;

import estacionamiento.repository.mysql.BeneficioRepositoryMySQL;
import estacionamiento.repository.mysql.TipoPlanRepositoryMySQL;
import estacionamiento.service.BeneficioService;
import estacionamiento.service.TipoPlanService;
import estacionamiento.util.AppLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/beneficios-oficina")
public class BeneficioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BeneficioService beneficioService;
    private TipoPlanService tipoPlanService;

    @Override
    public void init() throws ServletException {

        TipoPlanRepositoryMySQL tipoPlanRepository =
            new TipoPlanRepositoryMySQL();

        this.tipoPlanService =
            new TipoPlanService(tipoPlanRepository);

        this.beneficioService =
            new BeneficioService(
                new BeneficioRepositoryMySQL(),
                tipoPlanRepository
            );
    }

    // CONSULTA GENERAL
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String mensajeExito =
            (String) request.getSession().getAttribute("exito");

        if (mensajeExito != null) {
            request.setAttribute("exito", mensajeExito);
            request.getSession().removeAttribute("exito");
        }

        try {

            List<TipoPlan> planes =
                tipoPlanService.obtenerTodosLosTiposDePlan();

            request.setAttribute("listaTiposPlan", planes);

            List<Beneficio> beneficios =
                beneficioService.obtenerTodos();

            request.setAttribute("listaBeneficios", beneficios);

            request.getRequestDispatcher(
                "/WEB-INF/views/beneficios.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            AppLogger.error(
                "Error al cargar los beneficios para este tipo de plan", e
            );

            request.setAttribute(
                "error",
                "Error al cargar los beneficios: " + e.getMessage()
            );

            request.getRequestDispatcher(
                "/WEB-INF/views/beneficios.jsp"
            ).forward(request, response);
        }
    }

    // ALTA, MODIFICACIÓN, BAJA Y REACTIVACIÓN
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {

            if (accion == null || accion.trim().isEmpty()) {
                accion = "crear";
            }

            int codigoPlan;
            int numeroBeneficio;
            String descripcion;

            switch (accion) {

                case "crear":

                    codigoPlan = obtenerEntero(
                        request, "codigoPlanAlta"
                    );

                    descripcion =
                        request.getParameter("descripcion");

                    Beneficio nuevoBeneficio = new Beneficio();
                    nuevoBeneficio.setDescripcion(descripcion);

                    beneficioService.registrarBeneficio(
                        codigoPlan, nuevoBeneficio
                    );

                    request.getSession().setAttribute(
                        "exito",
                        "Beneficio registrado correctamente."
                    );

                    break;

                case "editar":

                    codigoPlan = obtenerEntero(
                        request, "codigoPlan"
                    );

                    numeroBeneficio = obtenerEntero(
                        request, "numero"
                    );

                    descripcion =
                        request.getParameter("descripcion");

                    Beneficio beneficioEditado = new Beneficio();
                    beneficioEditado.setDescripcion(descripcion);

                    beneficioService.actualizar(
                        codigoPlan,
                        numeroBeneficio,
                        beneficioEditado
                    );

                    request.getSession().setAttribute(
                        "exito",
                        "Beneficio actualizado correctamente."
                    );

                    break;

                case "bajaLogica":

                    codigoPlan = obtenerEntero(
                        request, "codigoPlan"
                    );

                    numeroBeneficio = obtenerEntero(
                        request, "numero"
                    );

                    beneficioService.darDeBaja(
                        codigoPlan, numeroBeneficio
                    );

                    request.getSession().setAttribute(
                        "exito",
                        "El beneficio fue dado de baja."
                    );

                    break;

                case "altaLogica":

                    codigoPlan = obtenerEntero(
                        request, "codigoPlan"
                    );

                    numeroBeneficio = obtenerEntero(
                        request, "numero"
                    );

                    beneficioService.darDeAltaPostBaja(
                        codigoPlan, numeroBeneficio
                    );

                    request.getSession().setAttribute(
                        "exito",
                        "El beneficio fue reactivado."
                    );

                    break;

                default:

                    throw new IllegalArgumentException(
                        "La acción solicitada no es válida."
                    );
            }

            response.sendRedirect(
                request.getContextPath() + "/beneficios-oficina"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                "error",
                "Error de validación: " + e.getMessage()
            );

            doGet(request, response);

        } catch (Exception e) {

            AppLogger.error(
                "Error inesperado en la gestión de beneficios. Acción: "
                + accion,
                e
            );

            request.setAttribute(
                "error",
                "Error inesperado al gestionar el beneficio."
            );

            doGet(request, response);
        }
    }

    // VALIDACIÓN DE PARÁMETROS NUMÉRICOS
    private int obtenerEntero(
            HttpServletRequest request,
            String parametro) {

        String valor = request.getParameter(parametro);

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El parámetro " + parametro + " es obligatorio."
            );
        }

        try {
            return Integer.parseInt(valor);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "El parámetro " + parametro + " debe ser numérico."
            );
        }
    }
}
