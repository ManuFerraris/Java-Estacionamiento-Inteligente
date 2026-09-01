<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.domain.Vehiculo" %>
<%@ page import="estacionamiento.domain.TipoVehiculo" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Vehículos - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container px-4 mb-5">
        
        <div class="d-flex justify-content-between align-items-center mb-4 mt-3">
            <h2 class="text-secondary fw-bold"><i class="bi bi-car-front me-2 text-primary"></i>Mi Cochera Virtual</h2>
        </div>

        <% 
            String error = (String) session.getAttribute("error");
            String exito = (String) session.getAttribute("exito");
        %>

        <div class="row g-4">
            
            <!-- COLUMNA IZQUIERDA: Formulario de Alta -->
            <div class="col-lg-4">
                <div class="card border-0 shadow-sm rounded-4 sticky-top" style="top: 20px;">
                    <div class="card-header bg-primary text-white py-3 rounded-top-4">
                        <h5 class="mb-0 fw-bold"><i class="bi bi-plus-circle me-2"></i>Registrar Vehículo</h5>
                    </div>
                    <div class="card-body p-4">
                        <form action="<%= request.getContextPath() %>/mis-vehiculos-user" method="POST">
                            <input type="hidden" name="accion" value="agregar">
                            
                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Patente</label>
                                <input type="text" class="form-control text-uppercase" name="patente" placeholder="Ej: AB123CD" required maxlength="10">
                            </div>

                            <div class="mb-3">
                                <label class="form-label text-muted small fw-bold">Descripción / Modelo</label>
                                <input type="text" class="form-control" name="descripcion" placeholder="Ej: Toyota Corolla Blanco" required>
                            </div>

                            <div class="mb-4">
                                <label class="form-label text-muted small fw-bold">Tipo de Vehículo</label>
                                <select class="form-select" name="idTipoVehiculo" required>
                                    <option value="" disabled selected>Selecciona una categoría...</option>
                                    <% 
                                        List<TipoVehiculo> tipos = (List<TipoVehiculo>) request.getAttribute("tiposVehiculo");
                                        if (tipos != null && !tipos.isEmpty()) {
                                            for(TipoVehiculo tv : tipos) {
                                    %>
                                        <option value="<%= tv.getNumero() %>"><%= tv.getNombre() %></option>
                                    <% 
                                            }
                                        } 
                                    %>
                                </select>
                            </div>

                            <button type="submit" class="btn btn-primary w-100 py-2 fw-bold">
                                <i class="bi bi-save me-2"></i>Guardar Vehículo
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Listado de Flota -->
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm rounded-4 h-100">
                    <div class="card-body p-4">
                        <h5 class="fw-bold text-dark mb-4"><i class="bi bi-list-ul me-2 text-secondary"></i>Vehículos Registrados</h5>
                        
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light text-muted small">
                                    <tr>
                                        <th>Patente</th>
                                        <th>Descripción</th>
                                        <th>Categoría</th>
                                        <th class="text-end">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                        List<Vehiculo> misVehiculos = (List<Vehiculo>) request.getAttribute("misVehiculos");
                                        if (misVehiculos != null && !misVehiculos.isEmpty()) {
                                            for(Vehiculo v : misVehiculos) {
                                    %>
                                    <tr>
                                        <td><span class="badge bg-dark fs-6 px-3 py-2 border border-secondary"><%= v.getPatente() %></span></td>
                                        <td class="fw-bold text-secondary"><%= v.getDescripcion() %></td>
                                        <td><%= v.getTipoVehiculo().getNombre() %></td>
                                        <td class="text-end">
                                            <form action="<%= request.getContextPath() %>/mis-vehiculos-user" method="POST" class="d-inline">
                                                <input type="hidden" name="accion" value="eliminar">
                                                <input type="hidden" name="patente" value="<%= v.getPatente() %>">
                                                <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('¿Estás seguro de eliminar este vehículo?');">
                                                    <i class="bi bi-trash"></i> Eliminar
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                    <% 
                                            }
                                        } else {
                                    %>
                                    <tr>
                                        <td colspan="4" class="text-center py-5">
                                            <i class="bi bi-car-front text-muted fs-1 d-block mb-3"></i>
                                            <span class="text-muted">Aún no tienes vehículos registrados en tu perfil.</span>
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

    <script>
        <% if (error != null) { %>
            Swal.fire({ icon: 'error', title: 'Operación denegada', text: `<%= error %>` });
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <% if (exito != null) { %>
            Swal.fire({ icon: 'success', title: '¡Completado!', text: `<%= exito %>` });
            <% session.removeAttribute("exito"); %>
        <% } %>
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>