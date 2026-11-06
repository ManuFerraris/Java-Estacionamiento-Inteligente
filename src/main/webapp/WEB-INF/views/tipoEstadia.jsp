
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.TipoEstadia" %>

<%!
    private String escaparHTML(String valor) {
        if (valor == null) return "";
        return valor.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    String error = (String) request.getAttribute("error");
    String exito = (String) session.getAttribute("exito");
    session.removeAttribute("exito");
%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Gestión de Tipos de Estadía</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container mt-3">

        <a href="<%= request.getContextPath() %>/dashboard-oficina"
           class="btn btn-sm btn-outline-secondary mb-3 shadow-sm">
            <i class="bi bi-arrow-left-circle me-1"></i>
            Volver al Menú Principal
        </a>

        <h1 class="mb-4 text-secondary">
            Configuración de Tipos de Estadía
        </h1>

        <div class="row">

            <!-- FORMULARIO DE ALTA Y EDICIÓN -->
            <div class="col-md-4">

                <div class="card shadow-sm mb-4 border-0">

                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0" id="tituloFormulario">
                            <i class="bi bi-clock-history me-2"></i>
                            Nuevo Tipo
                        </h5>
                    </div>

                    <div class="card-body">

                        <form action="<%= request.getContextPath() %>/tipoEstadia"
                              method="POST"
                              id="formTipoEstadia">

                            <input type="hidden"
                                   name="accion"
                                   id="accion"
                                   value="crear">

                            <input type="hidden"
                                   name="numero"
                                   id="numero">

                            <div class="mb-3">

                                <label for="descripcion"
                                       class="form-label text-muted fw-bold">
                                    Descripción
                                </label>

                                <input type="text"
                                       class="form-control"
                                       id="descripcion"
                                       name="descripcion"
                                       placeholder="Ej: Por Hora, Mensual..."
                                       required>
                            </div>

                            <div class="mb-3">

                                <label for="cupo"
                                       class="form-label text-muted fw-bold">
                                    Cupo Máximo
                                </label>

                                <input type="number"
                                       class="form-control"
                                       id="cupo"
                                       name="cupo"
                                       placeholder="Ej: 50"
                                       min="1"
                                       required>
                            </div>

                            <button type="submit"
                                    class="btn btn-success w-100 mb-2"
                                    id="btnGuardar">

                                <i class="bi bi-save me-2"></i>
                                Guardar Tipo de Estadía
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
                            Tipos de Estadía Registrados
                        </h5>
                    </div>

                    <div class="card-body p-0 table-responsive">

                        <table class="table table-striped table-hover mb-0">

                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Número (ID)</th>
                                    <th>Descripción</th>
                                    <th>Cupo Disponible</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>

                            <tbody class="align-middle">

                                <%
                                    List<TipoEstadia> lista =
                                        (List<TipoEstadia>) request.getAttribute("listaTiposEstadia");

                                    if (lista != null && !lista.isEmpty()) {

                                        for (TipoEstadia te : lista) {
                                %>

                                <tr>

                                    <td class="ps-3 fw-bold text-primary">
                                        #<%= te.getNumero() %>
                                    </td>

                                    <td>
                                        <%= escaparHTML(te.getDescripcion()) %>
                                    </td>

                                    <td>
                                        <span class="badge bg-info text-dark">
                                            <%= te.getCupo() %> vehículos
                                        </span>
                                    </td>

                                    <td>
                                        <div class="d-flex justify-content-center gap-2">

                                            <!-- EDITAR -->
                                            <button type="button"
                                                    class="btn btn-warning btn-sm"
                                                    title="Editar"
                                                    data-numero="<%= te.getNumero() %>"
                                                    data-descripcion="<%= escaparHTML(te.getDescripcion()) %>"
                                                    data-cupo="<%= te.getCupo() %>"
                                                    onclick="cargarEdicion(this)">

                                                <i class="bi bi-pencil-square"></i>
                                            </button>

                                            <!-- ELIMINAR -->
                                            <form action="<%= request.getContextPath() %>/tipoEstadia"
                                                  method="POST"
                                                  class="m-0"
                                                  onsubmit="return confirmarEliminacion(event, this)">

                                                <input type="hidden"
                                                       name="accion"
                                                       value="eliminar">

                                                <input type="hidden"
                                                       name="numero"
                                                       value="<%= te.getNumero() %>">

                                                <button type="submit"
                                                        class="btn btn-outline-danger btn-sm"
                                                        title="Eliminar">

                                                    <i class="bi bi-trash"></i>
                                                </button>

                                            </form>

                                        </div>
                                    </td>

                                </tr>

                                <%
                                        }
                                    } else {
                                %>

                                <tr>
                                    <td colspan="4"
                                        class="text-center text-muted py-4">

                                        <i class="bi bi-inbox fs-2 d-block mb-2"></i>

                                        No hay tipos de estadía configurados.
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

    <!-- MENSAJES -->
    <div id="mensajeError"
     data-mensaje="<%= escaparHTML(error) %>"
     style="display:none;"></div>

<div id="mensajeExito"
     data-mensaje="<%= escaparHTML(exito) %>"
     style="display:none;"></div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>

        function cargarEdicion(boton) {

            document.getElementById("numero").value =
                boton.dataset.numero;

            document.getElementById("descripcion").value =
                boton.dataset.descripcion;

            document.getElementById("cupo").value =
                boton.dataset.cupo;

            document.getElementById("accion").value = "editar";

            document.getElementById("tituloFormulario").innerHTML =
                '<i class="bi bi-pencil-square me-2"></i>Editar Tipo de Estadía';

            document.getElementById("btnGuardar").innerHTML =
                '<i class="bi bi-arrow-clockwise me-2"></i>Actualizar Tipo de Estadía';

            document.getElementById("btnGuardar").className =
                "btn btn-warning w-100 mb-2";

            document.getElementById("btnCancelar").classList.remove("d-none");

            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        }

        function cancelarEdicion() {

            document.getElementById("formTipoEstadia").reset();

            document.getElementById("numero").value = "";
            document.getElementById("accion").value = "crear";

            document.getElementById("tituloFormulario").innerHTML =
                '<i class="bi bi-clock-history me-2"></i>Nuevo Tipo';

            document.getElementById("btnGuardar").innerHTML =
                '<i class="bi bi-save me-2"></i>Guardar Tipo de Estadía';

            document.getElementById("btnGuardar").className =
                "btn btn-success w-100 mb-2";

            document.getElementById("btnCancelar").classList.add("d-none");
        }

        function confirmarEliminacion(event, formulario) {

            event.preventDefault();

            Swal.fire({
                title: "¿Eliminar tipo de estadía?",
                text: "Se intentará eliminar el registro seleccionado.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#6c757d",
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar"
            }).then((resultado) => {

                if (resultado.isConfirmed) {
                    formulario.submit();
                }

            });

            return false;
        }

        const error = document.getElementById("mensajeError")
            .dataset.mensaje;

        const exito = document.getElementById("mensajeExito")
            .dataset.mensaje;

        if (error) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: error
            });
        }

        if (exito) {
            Swal.fire({
                icon: "success",
                title: "¡Éxito!",
                text: exito
            });
        }

    </script>

</body>
</html>
