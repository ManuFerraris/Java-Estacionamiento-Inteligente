<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Centro de Reportes - Municipalidad</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        .hover-card {
            transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
        }
        .hover-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
        }
    </style>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-5 px-4">
        <div class="mb-5">
            <h2 class="text-dark fw-bold"><i class="bi bi-graph-up-arrow text-primary me-2"></i>Centro de Reportes y Estadísticas</h2>
            <p class="text-muted fs-5">Panel de control de gestión y auditoría del sistema de estacionamiento medido.</p>
        </div>

        <div class="row g-4">
            
            <!-- TARJETA 1: reporte Financiero -->
            <div class="col-lg-4 col-md-6">
                <a href="<%= request.getContextPath() %>/reporte-financiero-oficina" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-cash-stack fs-1 text-success"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Recaudación y Flujo de Caja</h5>
                            <p class="text-muted small mb-0">Auditoría de ingresos por reservas eventuales y pagos de suscripciones mensuales.</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- TARJETA 2: Demanda y Uso Urbano -->
            <div class="col-lg-4 col-md-6">
                <a href="<%= request.getContextPath() %>/reporte-demanda-oficina" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-pie-chart-fill fs-1 text-primary"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Demanda y Uso del Espacio</h5>
                            <p class="text-muted small mb-0">Métricas de ocupación urbana por categoría de vehículo y estado de reservas.</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- TARJETA 3: Padrón y Morosidad -->
            <div class="col-lg-4 col-md-6">
                <a href="<%= request.getContextPath() %>/reporte-morosidad-oficina" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-danger bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-person-lines-fill fs-1 text-danger"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Padrón y Morosidad</h5>
                            <p class="text-muted small mb-0">Control operativo de suscriptores activos y detección de usuarios con deudas pendientes.</p>
                        </div>
                    </div>
                </a>
            </div>

        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>