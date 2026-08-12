<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Cochera" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Cocheras</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <h1 class="mb-4">Administración de Cocheras</h1>

        <% 
            String error = (String) request.getAttribute("error");
            if (error != null && !error.isEmpty()) { 
        %>
            <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">Registrar Nueva Cochera</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/cocheras" method="POST">
                            <div class="mb-3">
                                <label for="numero" class="form-label">Número de Cochera</label>
                                <input type="number" class="form-control" id="numero" name="numero" required>
                            </div>
                            <button type="submit" class="btn btn-success w-100">Guardar Cochera</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Tabla con el listado -->
            <div class="col-md-8">
                <div class="card shadow-sm">
                    <div class="card-header bg-secondary text-white">
                        <h5 class="mb-0">Listado de Cocheras</h5>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Número / Código</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    List<Cochera> lista = (List<Cochera>) request.getAttribute("listaCocheras");
                                    if (lista != null && !lista.isEmpty()) {
                                        for (Cochera cochera : lista) {
                                %>
                                            <tr>
                                                <td><%= cochera.getCodigo() %></td>
                                                <td><span class="badge bg-success">Disponible</span></td>
                                            </tr>
                                <%
                                        }
                                    } else {
                                %>
                                        <tr>
                                            <td colspan="2" class="text-center text-muted">No hay cocheras registradas.</td>
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

</body>
</html>