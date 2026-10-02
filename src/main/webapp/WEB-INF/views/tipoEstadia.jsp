<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.TipoEstadia" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Tipos de Estadía</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

<%@ include file="navbar.jsp" %>

    <div class="container mt-3">
    <a href="<%= request.getContextPath() %>/dashboard-oficina" class="btn btn-sm btn-outline-secondary mb-3 shadow-sm">
            <i class="bi bi-arrow-left-circle me-1"></i>Volver al Menú Principal
        </a>
        <h1 class="mb-4 text-secondary">Configuración de Tipos de Estadía</h1>

        <div class="row">
            <!-- Formulario de Alta -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4 border-0">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="bi bi-clock-history me-2"></i>Nuevo Tipo</h5>
                    </div>
                    <div class="card-body">
                        <!-- action apunta al Servlet que acabamos de crear -->
                        <form action="tipoEstadia" method="POST">
                            <input type="hidden" name="accion" value="crear">
                            
                            <div class="mb-3">
                                <label class="form-label text-muted fw-bold">Descripción</label>
                                <input type="text" class="form-control" name="descripcion" placeholder="Ej: Por Hora, Mensual..." required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label text-muted fw-bold">Cupo Máximo</label>
                                <input type="number" class="form-control" name="cupo" placeholder="Ej: 50" min="1" required>
                            </div>
                            
                            <button type="submit" class="btn btn-success w-100">
                                <i class="bi bi-save me-2"></i>Guardar Tipo de Estadía
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Tabla de Listado -->
            <div class="col-md-8">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white border-bottom">
                        <h5 class="mb-0 text-dark"><i class="bi bi-list-ul me-2"></i>Tipos de Estadía Registrados</h5>
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-striped table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-3">Número (ID)</th>
                                    <th>Descripción</th>
                                    <th>Cupo Disponible</th>
                                </tr>
                            </thead>
                            <tbody class="align-middle">
                                <%
                                    List<TipoEstadia> lista = (List<TipoEstadia>) request.getAttribute("listaTiposEstadia");
                                    if (lista != null && !lista.isEmpty()) {
                                        for (TipoEstadia te : lista) {
                                %>
                                            <tr>
                                                <td class="ps-3 fw-bold text-primary">#<%= te.getNumero() %></td>
                                                <td><%= te.getDescripcion() %></td>
                                                <td><span class="badge bg-info text-dark"><%= te.getCupo() %> vehículos</span></td>
                                            </tr>
                                <%      }
                                    } else { %>
                                        <tr>
                                            <td colspan="3" class="text-center text-muted py-4">
                                                No hay tipos de estadía configurados en el sistema.
                                            </td>
                                        </tr>
                                <%  } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Script de Alertas -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        <% if (request.getAttribute("error") != null) { %>
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: '<%= request.getAttribute("error") %>'
            });
        <% } %>
        <% if (request.getAttribute("exito") != null) { %>
            Swal.fire({
                icon: 'success',
                title: '¡Éxito!',
                text: '<%= request.getAttribute("exito") %>'
            });
        <% } %>
    </script>
</body>
</html>