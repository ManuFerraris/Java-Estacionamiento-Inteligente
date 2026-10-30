<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="estacionamiento.dto.FilaReporteFinancieroDTO" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reporte Financiero</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>
    
    <div class="container-fluid mt-5 px-4 mb-5">
        
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="text-dark fw-bold"><i class="bi bi-cash-stack text-success me-2"></i>Recaudación y Flujo de Caja</h2>
                <p class="text-muted">Auditoría de ingresos monetarios del sistema.</p>
            </div>
            <a href="<%= request.getContextPath() %>/reportes-oficina" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i>Volver al Panel
            </a>
        </div>

        <!-- Panel de Filtros -->
        <div class="card border-0 shadow-sm rounded-4 mb-4">
            <div class="card-body p-4">
                <form action="<%= request.getContextPath() %>/reporte-financiero-oficina" method="GET" class="row g-3 align-items-end">
                    
                    <div class="col-md-3">
                        <label for="fechaDesde" class="form-label fw-bold text-muted">Fecha Desde</label>
                        <input type="date" class="form-control" id="fechaDesde" name="fechaDesde" 
                               value="<%= request.getAttribute("filtroDesde") != null ? request.getAttribute("filtroDesde") : "" %>">
                    </div>
                    
                    <div class="col-md-3">
                        <label for="fechaHasta" class="form-label fw-bold text-muted">Fecha Hasta</label>
                        <input type="date" class="form-control" id="fechaHasta" name="fechaHasta" 
                               value="<%= request.getAttribute("filtroHasta") != null ? request.getAttribute("filtroHasta") : "" %>">
                    </div>
                    
                    <div class="col-md-4">
                        <label for="origenIngreso" class="form-label fw-bold text-muted">Origen del Ingreso</label>
                        <% String origen = (String) request.getAttribute("filtroOrigen"); %>
                        <select class="form-select" id="origenIngreso" name="origenIngreso">
                            <option value="TODOS" <%= "TODOS".equals(origen) ? "selected" : "" %>>Todos los ingresos</option>
                            <option value="RESERVAS" <%= "RESERVAS".equals(origen) ? "selected" : "" %>>Solo Reservas Eventuales</option>
                            <option value="SUSCRIPCIONES" <%= "SUSCRIPCIONES".equals(origen) ? "selected" : "" %>>Solo Suscripciones</option>
                        </select>
                    </div>
                    
                    <div class="col-md-2 d-grid">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-funnel-fill me-1"></i>Filtrar
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <% String error = (String) request.getAttribute("error");
           if (error != null) { %>
            <div class="alert alert-danger"><i class="bi bi-exclamation-triangle-fill me-2"></i><%= error %></div>
        <% } %>

        <!-- Tabla de Resultados -->
        <div class="card border-0 shadow-sm rounded-4">
            <div class="card-header bg-white border-bottom p-4 d-flex justify-content-between align-items-center">
                <h5 class="mb-0 fw-bold">Detalle de Operaciones</h5>
                <h4 class="text-success fw-bold mb-0">Total Recaudado: $<%= request.getAttribute("totalRecaudado") %></h4>
            </div>
            <div class="card-body p-0 table-responsive">
                <table class="table table-striped table-hover mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-4">Fecha y Hora</th>
                            <th>Origen</th>
                            <th>Concepto</th>
                            <th>Estado</th>
                            <th class="text-end pe-4">Monto</th>
                        </tr>
                    </thead>
                    <tbody class="align-middle">
                        <% 
                        List<FilaReporteFinancieroDTO> lista = (List<FilaReporteFinancieroDTO>) request.getAttribute("listaReporte");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                        if (lista != null && !lista.isEmpty()) {
                            for (FilaReporteFinancieroDTO fila : lista) {
                                String colorBadge = "APROBADO".equalsIgnoreCase(fila.getEstadoPago()) ? "bg-success" : 
                                                    "PENDIENTE".equalsIgnoreCase(fila.getEstadoPago()) ? "bg-warning text-dark" : "bg-danger";
                        %>
                                <tr>
                                    <td class="ps-4 text-muted"><%= fila.getFechaOperacion() != null ? fila.getFechaOperacion().format(formatter) : "-" %></td>
                                    <td><span class="badge bg-secondary"><%= fila.getOrigenIngreso() %></span></td>
                                    <td><%= fila.getConcepto() %></td>
                                    <td><span class="badge <%= colorBadge %>"><%= fila.getEstadoPago() %></span></td>
                                    <td class="text-end pe-4 fw-bold text-dark">$<%= fila.getMonto() %></td>
                                </tr>
                        <%  }
                        } else { %>
                            <tr>
                                <td colspan="5" class="text-center py-5 text-muted">
                                    <i class="bi bi-search fs-1 d-block mb-3"></i>
                                    No se encontraron ingresos para los filtros seleccionados.
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</body>
</html>