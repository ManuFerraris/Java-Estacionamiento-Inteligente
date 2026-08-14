<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Cochera" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Cocheras</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

    <div class="container mt-5">
        <h1 class="mb-4">Administración de Cocheras</h1>

        <div class="row">
            <!-- COLUMNA IZQUIERDA: Formulario de Alta -->
            <div class="col-md-4">
                <div class="card shadow-sm mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">Registrar Nueva Cochera</h5>
                    </div>
                    <div class="card-body">
                        <form action="<%= request.getContextPath() %>/cocheras" method="POST">
                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre de la Cochera</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" required>
                            </div>
                            <div class="mb-3">
                                <label for="descripcion" class="form-label">Descripción</label>
                                <input type="text" class="form-control" id="descripcion" name="descripcion">
                            </div>
                            <div class="mb-3">
                                <label for="direccion" class="form-label">Dirección</label>
                                <input type="text" class="form-control" id="direccion" name="direccion" required>
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
                                    <th>Código</th>
                                    <th>Nombre</th>
                                    <th>Descripcion</th>
                                    <th>Direccion</th>
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
                                                <td><%= cochera.getNombre() %></td>
                                                <td><%= cochera.getDescripcion() %></td>
                                                <td><%= cochera.getDireccion() %></td>
                                            </tr>
                                <%
                                        }
                                    } else {
                                %>
                                        <tr>
                                            <td colspan="4" class="text-center text-muted">No hay cocheras registradas.</td>
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

    <!-- 3. Lógica dinámica para disparar SweetAlert -->
    <% 
        // Capturamos los posibles mensajes que envía el Servlet
        String error = (String) request.getAttribute("error");
        String exito = (String) request.getAttribute("exito");
    %>
    
    <script>
        // Si hay un error, SweetAlert lanza el popup rojo
        <% if (error != null && !error.isEmpty()) { %>
            Swal.fire({
                icon: 'error',
                title: 'Hubo un problema',
                text: `<%= error %>`,  // <-- Ojo acá: usamos backticks (`) en lugar de comillas simples
                confirmButtonColor: '#d33'
            });
        <% } %>

        // Si hay éxito, SweetAlert lanza el popup verde
        <% if (exito != null && !exito.isEmpty()) { %>
            Swal.fire({
                icon: 'success',
                title: '¡Operación Exitosa!',
                text: `<%= exito %>`,
                confirmButtonColor: '#198754'
            });
        <% } %>
    </script>
</body>
</html>