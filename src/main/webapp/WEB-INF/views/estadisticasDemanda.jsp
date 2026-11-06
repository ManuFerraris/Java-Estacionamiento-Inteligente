
<%@ page import="java.util.List" %>
<%@ page import="estacionamiento.dto.EstadisticaVehiculoDTO" %>
<%@ page import="estacionamiento.dto.EstadisticaEstadoDTO" %>
<%@ page import="estacionamiento.dto.EstadisticaResumenDTO" %>

<%
    EstadisticaResumenDTO resumen =
        (EstadisticaResumenDTO) request.getAttribute("resumen");

    List<EstadisticaVehiculoDTO> porVehiculo =
        (List<EstadisticaVehiculoDTO>) request.getAttribute("estadisticasPorVehiculo");

    List<EstadisticaEstadoDTO> porEstado =
        (List<EstadisticaEstadoDTO>) request.getAttribute("estadisticasPorEstado");

    String filtroDesde = (String) request.getAttribute("filtroDesde");
    String filtroHasta = (String) request.getAttribute("filtroHasta");
    String filtroTipo = (String) request.getAttribute("filtroTipoVehiculo");
    String filtroEstado = (String) request.getAttribute("filtroEstado");

    String error = (String) request.getAttribute("error");
    
    if (porVehiculo == null) porVehiculo = java.util.Collections.emptyList();
    if (porEstado == null) porEstado = java.util.Collections.emptyList();

    long total = resumen != null ? resumen.getTotalReservas() : 0;
    long finalizadas = resumen != null ? resumen.getReservasFinalizadas() : 0;
    long canceladas = resumen != null ? resumen.getReservasCanceladas() : 0;
    long pendientes = resumen != null ? resumen.getReservasPendientes() : 0;
    long noPresentadas = resumen != null ? resumen.getReservasNoPresentadas() : 0;
    double ratio = resumen != null ? resumen.getRatioConcrecion() : 0.0;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Estadísticas de Demanda</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        .stat-card {
            transition: transform .2s ease-in-out;
        }

        .stat-card:hover {
            transform: translateY(-4px);
        }

        .stat-value {
            font-size: 2rem;
            font-weight: 700;
        }

        .chart-container {
            position: relative;
            height: 320px;
        }
    </style>
</head>

<body class="bg-light">

<%@ include file="navbar.jsp" %>

<div class="container-fluid mt-5 px-4 mb-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="text-dark fw-bold">
                <i class="bi bi-pie-chart-fill text-primary me-2"></i>
                Demanda y Uso del Espacio Urbano
            </h2>
            <p class="text-muted">
                Análisis de reservas por categoría de vehículo y estado.
            </p>
        </div>

        <a href="<%= request.getContextPath() %>/reportes-oficina"
           class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left me-1"></i> Volver
        </a>
    </div>

    <!-- FILTROS -->
    <div class="card border-0 shadow-sm rounded-4 mb-4">
        <div class="card-body p-4">

            <form action="<%= request.getContextPath() %>/reporte-demanda-oficina"
                  method="GET" class="row g-3 align-items-end">

                <div class="col-md-3">
                    <label class="form-label fw-bold text-muted">Desde</label>
                    <input type="date" name="desde" class="form-control"
                           value="<%= filtroDesde != null ? filtroDesde : "" %>">
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold text-muted">Hasta</label>
                    <input type="date" name="hasta" class="form-control"
                           value="<%= filtroHasta != null ? filtroHasta : "" %>">
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold text-muted">Tipo de vehículo</label>
                    <select name="tipoVehiculo" class="form-select">
                        <option value="">Todos los vehículos</option>
                        <option value="Auto" <%= "Auto".equals(filtroTipo) ? "selected" : "" %>>Auto</option>
                        <option value="Moto" <%= "Moto".equals(filtroTipo) ? "selected" : "" %>>Moto</option>
                        <option value="Pick-up" <%= "Pick-up".equals(filtroTipo) ? "selected" : "" %>>Pick-up</option>
                    </select>
                </div>

                <div class="col-md-3">
                    <label class="form-label fw-bold text-muted">Estado</label>
                    <select name="estado" class="form-select">
                        <option value="">Todos los estados</option>
                        <option value="FINALIZADA" <%= "FINALIZADA".equals(filtroEstado) ? "selected" : "" %>>Finalizada</option>
                        <option value="CANCELADA" <%= "CANCELADA".equals(filtroEstado) ? "selected" : "" %>>Cancelada</option>
                        <option value="PENDIENTE" <%= "PENDIENTE".equals(filtroEstado) ? "selected" : "" %>>Pendiente</option>
                        <option value="NO_PRESENTADO" <%= "NO_PRESENTADO".equals(filtroEstado) ? "selected" : "" %>>No presentado</option>
                    </select>
                </div>

                <div class="col-12 d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-funnel-fill me-1"></i> Aplicar filtros
                    </button>

                    <a href="<%= request.getContextPath() %>/reporte-demanda-oficina"
                       class="btn btn-outline-secondary">
                        Limpiar filtros
                    </a>
                </div>

            </form>
        </div>
    </div>

    <% if (error != null) { %>
        <div class="alert alert-danger">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <%= error %>
        </div>
    <% } %>

    <!-- INDICADORES -->
    <div class="row g-3 mb-4">

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card border-0 shadow-sm rounded-4 h-100 stat-card">
                <div class="card-body p-3">
                    <div class="text-muted small">Total de reservas</div>
                    <div class="stat-value text-primary"><%= total %></div>
                    <i class="bi bi-calendar-check text-primary"></i>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card border-0 shadow-sm rounded-4 h-100 stat-card">
                <div class="card-body p-3">
                    <div class="text-muted small">Finalizadas</div>
                    <div class="stat-value text-success"><%= finalizadas %></div>
                    <i class="bi bi-check-circle text-success"></i>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card border-0 shadow-sm rounded-4 h-100 stat-card">
                <div class="card-body p-3">
                    <div class="text-muted small">Canceladas</div>
                    <div class="stat-value text-danger"><%= canceladas %></div>
                    <i class="bi bi-x-circle text-danger"></i>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card border-0 shadow-sm rounded-4 h-100 stat-card">
                <div class="card-body p-3">
                    <div class="text-muted small">Pendientes</div>
                    <div class="stat-value text-warning"><%= pendientes %></div>
                    <i class="bi bi-hourglass-split text-warning"></i>
                </div>
            </div>
        </div>

        <div class="col-xl col-md-4 col-sm-6">
            <div class="card border-0 shadow-sm rounded-4 h-100 stat-card">
                <div class="card-body p-3">
                    <div class="text-muted small">No presentadas</div>
                    <div class="stat-value text-secondary"><%= noPresentadas %></div>
                    <i class="bi bi-person-x text-secondary"></i>
                </div>
            </div>
        </div>

    </div>

    <!-- RATIO -->
    <div class="card border-0 shadow-sm rounded-4 mb-4">
        <div class="card-body p-4">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <div>
                    <h5 class="fw-bold mb-1">Ratio de concreción</h5>
                    <p class="text-muted small mb-0">
                        Proporción de reservas finalizadas sobre finalizadas y canceladas.
                    </p>
                </div>
                <h2 class="text-success fw-bold mb-0">
                    <%= String.format(java.util.Locale.US, "%.1f", ratio) %>%
                </h2>
            </div>

            <div class="progress" style="height: 12px;">
                <div class="progress-bar bg-success"
                     role="progressbar"
                     style="width: <%= Math.max(0, Math.min(100, ratio)) %>%">
                </div>
            </div>
        </div>
    </div>

    <!-- GRÁFICOS -->
    <div class="row g-4">

        <div class="col-lg-6">
            <div class="card border-0 shadow-sm rounded-4 h-100">
                <div class="card-body p-4">
                    <h5 class="fw-bold mb-3">Demanda por tipo de vehículo</h5>
                    <div class="chart-container">
                        <canvas id="graficoVehiculos"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="card border-0 shadow-sm rounded-4 h-100">
                <div class="card-body p-4">
                    <h5 class="fw-bold mb-3">Reservas por estado</h5>
                    <div class="chart-container">
                        <canvas id="graficoEstados"></canvas>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>

<script>
    const etiquetasVehiculos = [
        <% for (int i = 0; i < porVehiculo.size(); i++) { %>
            "<%= porVehiculo.get(i).getTipoVehiculo().replace("\\", "\\\\").replace("\"", "\\\"") %>"<%= i < porVehiculo.size() - 1 ? "," : "" %>
        <% } %>
    ];

    const datosVehiculos = [
        <% for (int i = 0; i < porVehiculo.size(); i++) { %>
            <%= porVehiculo.get(i).getCantidadReservas() %><%= i < porVehiculo.size() - 1 ? "," : "" %>
        <% } %>
    ];

    const etiquetasEstados = [
        <% for (int i = 0; i < porEstado.size(); i++) { %>
            "<%= porEstado.get(i).getEstado() %>"<%= i < porEstado.size() - 1 ? "," : "" %>
        <% } %>
    ];

    const datosEstados = [
        <% for (int i = 0; i < porEstado.size(); i++) { %>
            <%= porEstado.get(i).getCantidadReservas() %><%= i < porEstado.size() - 1 ? "," : "" %>
        <% } %>
    ];

    new Chart(document.getElementById("graficoVehiculos"), {
        type: "bar",
        data: {
            labels: etiquetasVehiculos,
            datasets: [{
                label: "Cantidad de reservas",
                data: datosVehiculos,
                backgroundColor: ["#0d6efd", "#198754", "#ffc107"],
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: true, ticks: { precision: 0 } }
            }
        }
    });

    new Chart(document.getElementById("graficoEstados"), {
        type: "doughnut",
        data: {
            labels: etiquetasEstados,
            datasets: [{
                data: datosEstados,
                backgroundColor: ["#198754", "#dc3545", "#ffc107", "#6c757d"]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
</script>

</body>
</html>
