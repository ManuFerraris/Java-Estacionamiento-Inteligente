
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Beneficio" %>
<%@ page import="estacionamiento.domain.TipoPlan" %>

<%!
    private String escaparAtributo(String valor) {
        if (valor == null) return "";

        return valor
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
    }
%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Gestión de Beneficios</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container-fluid mt-5 px-4">

        <h1 class="mb-4 text-secondary">
            Administración de Beneficios por Plan
        </h1>

        <div class="row">

            <!-- FORMULARIO DE ALTA Y EDICIÓN -->
            <div class="col-lg-4 col-md-12">

                <div class="card shadow-sm mb-4 border-0">

                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0" id="tituloFormulario">
                            <i class="bi bi-star-fill me-2"></i>
                            Registrar Nuevo Beneficio
                        </h5>
                    </div>

                    <div class="card-body">

                        <form action="<%= request.getContextPath() %>/beneficios-oficina"
                              method="POST"
                              id="formBeneficio">

                            <input type="hidden"
                                   name="accion"
                                   id="accionForm"
                                   value="crear">

                            <input type="hidden"
                                   name="numero"
                                   id="numeroBeneficio"
                                   value="">

                            <input type="hidden"
                                   name="codigoPlan"
                                   id="codigoPlanHidden"
                                   value="">

                            <div class="mb-3">

                                <label for="codigoPlan"
                                       class="form-label text-muted fw-bold">
                                    Tipo de Plan Asociado
                                </label>

                                <select class="form-select"
                                        id="codigoPlan"
                                        name="codigoPlanAlta"
                                        required>

                                    <option value="" disabled selected>
                                        Seleccione un plan...
                                    </option>

                                    <%
                                        List<TipoPlan> listaPlanes =
                                            (List<TipoPlan>) request.getAttribute("listaTiposPlan");

                                        if (listaPlanes != null) {
                                            for (TipoPlan plan : listaPlanes) {
                                                if (plan.getFechaBaja() == null) {
                                    %>

                                        <option value="<%= plan.getCodigo() %>">
                                            <%= plan.getNombre() %>
                                        </option>

                                    <%
                                                }
                                            }
                                        }
                                    %>

                                </select>

                            </div>

                            <div class="mb-3">

                                <label for="descripcion"
                                       class="form-label text-muted fw-bold">
                                    Descripción del Beneficio
                                </label>

                                <textarea class="form-control"
                                          id="descripcion"
                                          name="descripcion"
                                          rows="3"
                                          placeholder="Ej. Lavado de carrocería gratis una vez al mes..."
                                          required></textarea>

                            </div>

                            <button type="submit"
                                    class="btn btn-success w-100 mb-2 mt-2"
                                    id="btnGuardar">

                                <i class="bi bi-save me-2"></i>
                                Guardar Beneficio

                            </button>

                            <button type="button"
                                    class="btn btn-secondary w-100 d-none"
                                    id="btnCancelar"
                                    onclick="cancelarEdicion()">

                                <i class="bi bi-x-circle me-2"></i>
                                Cancelar Edición

                            </button>

                        </form>

                    </div>
                </div>
            </div>

            <!-- LISTADO DE BENEFICIOS -->
            <div class="col-lg-8 col-md-12">

                <div class="card shadow-sm border-0">

                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark">
                            <i class="bi bi-list-stars me-2"></i>
                            Listado de Beneficios
                        </h5>
                    </div>

                    <div class="card-body p-0 table-responsive">

                        <table class="table table-striped table-hover mb-0">

                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Plan</th>
                                    <th>N° Beneficio</th>
                                    <th>Descripción</th>
                                    <th>Estado</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>

                            <tbody class="align-middle">

                                <%
                                    List<Beneficio> lista =
                                        (List<Beneficio>) request.getAttribute("listaBeneficios");

                                    if (lista != null && !lista.isEmpty()) {

                                        for (Beneficio b : lista) {
                                            int codigoPlanBeneficio =
                                                b.getTipoPlan().getCodigo();
                                %>

                                    <tr>

                                        <td class="ps-3 fw-bold text-primary">
                                            <%= b.getTipoPlan() != null
                                                ? b.getTipoPlan().getNombre()
                                                : "Sin Plan" %>
                                        </td>

                                        <td class="fw-semibold text-secondary">
                                            #<%= b.getNumero() %>
                                        </td>

                                        <td>
                                            <span title="<%= escaparAtributo(b.getDescripcion()) %>">

                                                <%= b.getDescripcion() != null &&
                                                    b.getDescripcion().length() > 50
                                                    ? b.getDescripcion().substring(0, 47) + "..."
                                                    : (b.getDescripcion() != null
                                                        ? b.getDescripcion() : "-") %>

                                            </span>
                                        </td>

                                        <td>

                                            <% if (b.getFechaBaja() != null) { %>

                                                <span class="badge bg-danger">
                                                    <i class="bi bi-dash-circle me-1"></i>
                                                    Inactivo
                                                </span>

                                            <% } else { %>

                                                <span class="badge bg-success">
                                                    <i class="bi bi-check-circle me-1"></i>
                                                    Activo
                                                </span>

                                            <% } %>

                                        </td>

                                        <td>

                                            <div class="d-flex justify-content-center gap-2">

                                                <!-- EDITAR -->
                                                <button type="button"
                                                        class="btn btn-warning btn-sm text-dark btn-editar-beneficio"
                                                        title="Editar Beneficio"
                                                        data-codigo-plan="<%= codigoPlanBeneficio %>"
                                                        data-numero="<%= b.getNumero() %>"
                                                        data-descripcion="<%= escaparAtributo(b.getDescripcion()) %>">

                                                    <i class="bi bi-pencil-square"></i>

                                                </button>

                                                <% if (b.getFechaBaja() != null) { %>

                                                    <!-- REACTIVAR -->
                                                    <form action="<%= request.getContextPath() %>/beneficios-oficina"
                                                          method="POST"
                                                          class="m-0">

                                                        <input type="hidden"
                                                               name="accion"
                                                               value="altaLogica">

                                                        <input type="hidden"
                                                               name="codigoPlan"
                                                               value="<%= codigoPlanBeneficio %>">

                                                        <input type="hidden"
                                                               name="numero"
                                                               value="<%= b.getNumero() %>">

                                                        <button type="submit"
                                                                class="btn btn-outline-success btn-sm"
                                                                title="Reactivar Beneficio"
                                                                onclick="return confirm('¿Seguro que deseas volver a habilitar este beneficio?');">

                                                            <i class="bi bi-check-circle"></i>

                                                        </button>

                                                    </form>

                                                <% } else { %>

                                                    <!-- BAJA LÓGICA -->
                                                    <form action="<%= request.getContextPath() %>/beneficios-oficina"
                                                          method="POST"
                                                          class="m-0">

                                                        <input type="hidden"
                                                               name="accion"
                                                               value="bajaLogica">

                                                        <input type="hidden"
                                                               name="codigoPlan"
                                                               value="<%= codigoPlanBeneficio %>">

                                                        <input type="hidden"
                                                               name="numero"
                                                               value="<%= b.getNumero() %>">

                                                        <button type="submit"
                                                                class="btn btn-outline-danger btn-sm"
                                                                title="Dar de Baja"
                                                                onclick="return confirm('¿Seguro que deseas dar de baja este beneficio?');">

                                                            <i class="bi bi-ban"></i>

                                                        </button>

                                                    </form>

                                                <% } %>

                                            </div>

                                        </td>

                                    </tr>

                                <%
                                        }

                                    } else {
                                %>

                                    <tr>
                                        <td colspan="5"
                                            class="text-center text-muted py-4">

                                            <i class="bi bi-inbox fs-2 d-block mb-2"></i>

                                            No hay beneficios registrados para ningún plan.

                                        </td>
                                    </tr>

                                <%
                                    }
                                %>

                            </tbody>

                        </table>

                    </div>
                </div>
            </div>

        </div>
    </div>

    <%
        String error = (String) request.getAttribute("error");
        String exito = (String) request.getAttribute("exito");
    %>

    <script>

        // CARGAR DATOS PARA EDITAR
        function cargarDatosEdicion(codigoPlan, numero, descripcion) {

            document.getElementById('accionForm').value = 'editar';

            document.getElementById('numeroBeneficio').value = numero;
            document.getElementById('codigoPlanHidden').value = codigoPlan;

            document.getElementById('descripcion').value = descripcion;

            // Seleccionamos el plan original.
            let selectPlan = document.getElementById('codigoPlan');

            selectPlan.value = codigoPlan;

            // No permitimos cambiar el plan durante la edición.
            selectPlan.disabled = true;

            // Actualizamos el título.
            document.getElementById('tituloFormulario').innerHTML =
                '<i class="bi bi-pencil-square me-2"></i>Editar Beneficio';

            // Actualizamos el botón.
            let btn = document.getElementById('btnGuardar');

            btn.innerHTML =
                '<i class="bi bi-arrow-clockwise me-2"></i>Actualizar Beneficio';

            btn.className =
                'btn btn-warning w-100 mb-2 mt-2 fw-bold text-dark';

            document.getElementById('btnCancelar').classList.remove('d-none');

            document.getElementById('formBeneficio').scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }


        // ASOCIAR EVENTOS A LOS BOTONES EDITAR
        document.querySelectorAll('.btn-editar-beneficio').forEach(boton => {

            boton.addEventListener('click', function() {

                cargarDatosEdicion(
                    this.dataset.codigoPlan,
                    this.dataset.numero,
                    this.dataset.descripcion
                );

            });

        });


        // CANCELAR EDICIÓN
        function cancelarEdicion() {

            document.getElementById('accionForm').value = 'crear';

            document.getElementById('numeroBeneficio').value = '';
            document.getElementById('codigoPlanHidden').value = '';

            document.getElementById('descripcion').value = '';

            let selectPlan = document.getElementById('codigoPlan');

            selectPlan.value = '';
            selectPlan.disabled = false;

            document.getElementById('tituloFormulario').innerHTML =
                '<i class="bi bi-star-fill me-2"></i>Registrar Nuevo Beneficio';

            let btn = document.getElementById('btnGuardar');

            btn.innerHTML =
                '<i class="bi bi-save me-2"></i>Guardar Beneficio';

            btn.className =
                'btn btn-success w-100 mb-2 mt-2';

            document.getElementById('btnCancelar').classList.add('d-none');
        }


        // ANTES DE ENVIAR EL FORMULARIO
        document.getElementById('formBeneficio').addEventListener('submit', function() {

            let accion = document.getElementById('accionForm').value;

            if (accion === 'editar') {

                // El plan se envía mediante el campo oculto.
                document.getElementById('codigoPlan').disabled = true;

            } else {

                // Para el alta, copiamos el plan seleccionado al campo oculto.
                document.getElementById('codigoPlanHidden').disabled = true;
                document.getElementById('codigoPlan').disabled = false;
            }

        });

    </script>

    <% if (error != null && !error.isEmpty()) { %>
        <script>
            Swal.fire({
                icon: 'error',
                title: 'Hubo un problema',
                text: '<%= escaparAtributo(error) %>',
                confirmButtonColor: '#d33'
            });
        </script>
    <% } %>

    <% if (exito != null && !exito.isEmpty()) { %>
        <script>
            Swal.fire({
                icon: 'success',
                title: '¡Operación Exitosa!',
                text: '<%= escaparAtributo(exito) %>',
                confirmButtonColor: '#198754'
            });
        </script>
    <% } %>

</body>
</html>
