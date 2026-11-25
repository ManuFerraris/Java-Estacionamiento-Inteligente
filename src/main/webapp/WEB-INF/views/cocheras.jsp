
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Cochera" %>

<%!
    private String escaparHTML(Object valor) {
        if (valor == null) return "";
        return valor.toString()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Gestión de Cocheras</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container mt-5">

        <h1 class="mb-4 text-secondary">Administración de Cocheras</h1>

        <div class="row">

            <!-- FORMULARIO -->
            <div class="col-md-4">

                <div class="card shadow-sm mb-4 border-0">

                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0" id="tituloFormulario">
                            <i class="bi bi-plus-circle me-2"></i>
                            Registrar Nueva Cochera
                        </h5>
                    </div>

                    <div class="card-body">

                        <form action="<%= request.getContextPath() %>/cocheras-oficina"
                              method="POST" id="formCochera">

                            <input type="hidden" name="accion" id="accionForm" value="crear">
                            <input type="hidden" name="codigo" id="codigoCochera">

                            <div class="mb-3">
                                <label for="nombre" class="form-label text-muted fw-bold">
                                    Nombre de la Cochera
                                </label>
                                <input type="text" class="form-control"
                                       id="nombre" name="nombre"
                                       placeholder="Ej. Estacionamiento Centro"
                                       maxlength="50" required>
                            </div>

                            <div class="mb-3">
                                <label for="descripcion" class="form-label text-muted fw-bold">
                                    Descripción
                                </label>
                                <input type="text" class="form-control"
                                       id="descripcion" name="descripcion"
                                       placeholder="Ej. Abierto 24hs, solo autos"
                                       maxlength="255" required>
                            </div>

                            <div class="mb-3">
                                <label for="direccion" class="form-label text-muted fw-bold">
                                    Dirección
                                </label>
                                <input type="text" class="form-control"
                                       id="direccion" name="direccion"
                                       placeholder="Ej. San Martín 1234"
                                       maxlength="50" required>
                            </div>

                            <button type="submit" class="btn btn-success w-100 mb-2"
                                    id="btnGuardar">
                                <i class="bi bi-save me-2"></i>
                                Guardar Cochera
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

            <!-- LISTADO -->
            <div class="col-md-8">

                <div class="card shadow-sm border-0">

                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark">
                            <i class="bi bi-list-ul me-2"></i>
                            Listado de Cocheras
                        </h5>
                    </div>

                    <div class="card-body p-0">

                        <div class="table-responsive">

                            <table class="table table-striped table-hover mb-0">

                                <thead class="table-light">
                                    <tr>
                                        <th class="ps-3">Código</th>
                                        <th>Nombre</th>
                                        <th>Descripción</th>
                                        <th>Dirección</th>
                                        <th>Estado</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                </thead>

                                <tbody class="align-middle">

                                    <%
                                        List<Cochera> lista =
                                            (List<Cochera>) request.getAttribute("listaCocheras");

                                        if (lista != null && !lista.isEmpty()) {
                                            for (Cochera cochera : lista) {

                                                boolean inactiva = cochera.getFechaBaja() != null;
                                    %>

                                    <tr>

                                        <td class="ps-3 fw-bold text-secondary">
                                            <%= cochera.getCodigo() %>
                                        </td>

                                        <td class="fw-semibold">
                                            <%= escaparHTML(cochera.getNombre()) %>
                                        </td>

                                        <td>
                                            <%= escaparHTML(cochera.getDescripcion()) %>
                                        </td>

                                        <td>
                                            <%= escaparHTML(cochera.getDireccion()) %>
                                        </td>

                                        <td>
                                            <% if (inactiva) { %>
                                                <span class="badge bg-danger">
                                                    <i class="bi bi-dash-circle me-1"></i>
                                                    Inactiva
                                                </span>
                                            <% } else { %>
                                                <span class="badge bg-success">
                                                    <i class="bi bi-check-circle me-1"></i>
                                                    Activa
                                                </span>
                                            <% } %>
                                        </td>

                                        <td>
                                            <div class="d-flex justify-content-center gap-2">

                                                <% if (!inactiva) { %>

                                                    <!-- EDITAR -->
                                                    <button type="button"
                                                            class="btn btn-warning btn-sm text-dark btn-editar"
                                                            title="Editar Cochera"
                                                            data-codigo="<%= cochera.getCodigo() %>"
                                                            data-nombre="<%= escaparHTML(cochera.getNombre()) %>"
                                                            data-descripcion="<%= escaparHTML(cochera.getDescripcion()) %>"
                                                            data-direccion="<%= escaparHTML(cochera.getDireccion()) %>">

                                                        <i class="bi bi-pencil-square"></i>
                                                    </button>

                                                    <!-- BAJA LÓGICA -->
                                                    <form action="<%= request.getContextPath() %>/cocheras-oficina"
                                                          method="POST" class="m-0">

                                                        <input type="hidden" name="accion" value="bajaLogica">
                                                        <input type="hidden" name="codigo"
                                                               value="<%= cochera.getCodigo() %>">

                                                        <button type="submit"
                                                                class="btn btn-outline-danger btn-sm"
                                                                title="Dar de Baja"
                                                                onclick="return confirm('¿Seguro que deseas dar de baja esta cochera?');">

                                                            <i class="bi bi-ban"></i>
                                                        </button>

                                                    </form>

                                                <% } else { %>

                                                    <!-- REACTIVAR -->
                                                    <form action="<%= request.getContextPath() %>/cocheras-oficina"
                                                          method="POST" class="m-0">

                                                        <input type="hidden" name="accion" value="altaLogica">
                                                        <input type="hidden" name="codigo"
                                                               value="<%= cochera.getCodigo() %>">

                                                        <button type="submit"
                                                                class="btn btn-outline-success btn-sm"
                                                                title="Reactivar Cochera"
                                                                onclick="return confirm('¿Deseas reactivar esta cochera?');">

                                                            <i class="bi bi-arrow-counterclockwise"></i>
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
                                        <td colspan="6" class="text-center text-muted py-4">
                                            <i class="bi bi-inbox fs-2 d-block mb-2"></i>
                                            No hay cocheras registradas en el sistema.
                                        </td>
                                    </tr>

                                    <% } %>

                                </tbody>

                            </table>

                        </div>
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

        // Cargar datos para editar
        document.querySelectorAll(".btn-editar").forEach(function(boton) {

            boton.addEventListener("click", function() {

                document.getElementById("nombre").value =
                    this.dataset.nombre;

                document.getElementById("descripcion").value =
                    this.dataset.descripcion;

                document.getElementById("direccion").value =
                    this.dataset.direccion;

                document.getElementById("codigoCochera").value =
                    this.dataset.codigo;

                document.getElementById("accionForm").value = "editar";

                document.getElementById("tituloFormulario").innerHTML =
                    '<i class="bi bi-pencil-square me-2"></i>Editar Cochera';

                let btn = document.getElementById("btnGuardar");

                btn.innerHTML =
                    '<i class="bi bi-arrow-clockwise me-2"></i>Actualizar Cochera';

                btn.className =
                    "btn btn-warning w-100 mb-2 fw-bold text-dark";

                document.getElementById("btnCancelar").classList.remove("d-none");

                window.scrollTo({
                    top: 0,
                    behavior: "smooth"
                });

            });

        });

        // Cancelar edición
        function cancelarEdicion() {

            document.getElementById("formCochera").reset();

            document.getElementById("codigoCochera").value = "";
            document.getElementById("accionForm").value = "crear";

            document.getElementById("tituloFormulario").innerHTML =
                '<i class="bi bi-plus-circle me-2"></i>Registrar Nueva Cochera';

            let btn = document.getElementById("btnGuardar");

            btn.innerHTML =
                '<i class="bi bi-save me-2"></i>Guardar Cochera';

            btn.className = "btn btn-success w-100 mb-2";

            document.getElementById("btnCancelar").classList.add("d-none");
        }

        <% if (error != null && !error.isEmpty()) { %>

            Swal.fire({
                icon: "error",
                title: "Hubo un problema",
                text: "<%= escaparHTML(error) %>",
                confirmButtonColor: "#d33"
            });

        <% } %>

        <% if (exito != null && !exito.isEmpty()) { %>

            Swal.fire({
                icon: "success",
                title: "¡Operación Exitosa!",
                text: "<%= escaparHTML(exito) %>",
                confirmButtonColor: "#198754"
            });

        <% } %>

    </script>

</body>
</html>
