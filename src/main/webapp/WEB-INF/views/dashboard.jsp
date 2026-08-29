<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="estacionamiento.domain.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Control - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        /* Efectos visuales de elevación para las tarjetas interactivas */
        .hover-card {
            transition: all 0.3s ease-in-out;
            cursor: pointer;
        }
        .hover-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15) !important;
            border-color: #0d6efd !important;
        }
        .icono-fondo {
            font-size: 8rem;
            position: absolute;
            right: -10px;
            bottom: -30px;
            opacity: 0.15;
            transform: rotate(-10deg);
        }
    </style>
</head>
<body class="bg-light">

    <%@ include file="navbar.jsp" %>

    <div class="container px-4 mb-5">
        
        <% Usuario u = (Usuario) session.getAttribute("usuarioLogueado"); %>

        <!-- Tarjeta de Bienvenida Principal -->
        <div class="row mt-2 mb-4">
            <div class="col-12">
                <div class="card border-0 shadow-sm rounded-4 bg-primary text-white overflow-hidden position-relative">
                    <div class="card-body p-4 p-md-5">
                        <div class="position-relative z-1">
                            <h2 class="fw-bold mb-2">¡Hola, <%= u.getNombre() %>!</h2>
                            <p class="mb-0 fs-5 opacity-75">Bienvenido al panel de administración del Sistema Inteligente de Estacionamiento.</p>
                            <span class="badge bg-light text-primary mt-3 px-3 py-2 rounded-pill shadow-sm">
                                Rol actual: <%= u.getRol().name() %>
                            </span>
                        </div>
                        <i class="bi bi-car-front-fill icono-fondo text-white z-0"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Módulo de Accesos Rápidos -->
        <h5 class="text-secondary fw-bold mb-3"><i class="bi bi-grid-fill me-2 text-primary"></i>Accesos Rápidos</h5>
        
        <div class="row g-4 mb-5">
            <!-- Tarjeta 1: Usuarios -->
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/usuarios" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-primary bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-people-fill fs-1 text-primary"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Gestión de Usuarios</h5>
                            <p class="text-muted small mb-0">Altas, bajas, modificación y auditoría de cuentas del sistema.</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Tarjeta 2: Suscripciones -->
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/suscripciones-oficina" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-card-checklist fs-1 text-success"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Suscripciones</h5>
                            <p class="text-muted small mb-0">Asignación de planes, control de vencimientos y cancelaciones.</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Tarjeta 3: Caja / Pagos -->
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/pagos-suscripciones-oficina" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-warning bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-cash-coin fs-1 text-warning"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Caja / Pagos</h5>
                            <p class="text-muted small mb-0">Cobro de comprobantes pendientes y registro de facturación.</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>

    </div>

    <!-- Scripts de Bootstrap (Necesario para el botón hamburguesa del Navbar) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>