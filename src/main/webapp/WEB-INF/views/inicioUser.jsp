<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="estacionamiento.domain.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Portal - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        .hover-card {
            transition: all 0.3s ease-in-out;
            cursor: pointer;
        }
        .hover-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15) !important;
            border-color: #198754 !important; /* Borde verde al pasar el mouse */
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

    <!-- Reutilizamos el Navbar dinámico -->
    <%@ include file="navbar.jsp" %>

    <div class="container px-4 mb-5">
        
        <% Usuario u = (Usuario) session.getAttribute("usuarioLogueado"); %>

        <!-- Tarjeta de Bienvenida -->
        <div class="row mt-2 mb-4">
            <div class="col-12">
                <div class="card border-0 shadow-sm rounded-4 bg-success text-white overflow-hidden position-relative">
                    <div class="card-body p-4 p-md-5">
                        <div class="position-relative z-1">
                            <h2 class="fw-bold mb-2">¡Hola, <%= u.getNombre() %>!</h2>
                            <p class="mb-0 fs-5 opacity-75">Bienvenido a tu portal ciudadano para la gestión de estacionamientos.</p>
                        </div>
                        <i class="bi bi-p-circle-fill icono-fondo text-white z-0"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Módulo de Servicios -->
        <h5 class="text-secondary fw-bold mb-3"><i class="bi bi-grid-fill me-2 text-success"></i>Mis Servicios</h5>
        
        <div class="row g-4 mb-5">
            <!-- Tarjeta 1: Suscripciones del Cliente -->
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/mis-suscripciones-user" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-success bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-card-checklist fs-1 text-success"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Mis Suscripciones</h5>
                            <p class="text-muted small mb-0">Consulta tus planes vigentes y el historial de pagos realizados.</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Tarjeta 2: Reservas (Para el futuro módulo) -->
            <div class="col-md-4">
                <a href="#" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-info bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-car-front-fill fs-1 text-info"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Mis Reservas</h5>
                            <p class="text-muted small mb-0">Gestiona estadías diarias o por hora y registra salidas parciales.</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Tarjeta 3: Perfil de Usuario -->
            <div class="col-md-4">
                <a href="<%= request.getContextPath() %>/perfil-user" class="text-decoration-none">
                    <div class="card border-0 shadow-sm rounded-4 h-100 hover-card">
                        <div class="card-body p-4 text-center">
                            <div class="bg-secondary bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                                <i class="bi bi-person-vcard fs-1 text-secondary"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Mi Perfil</h5>
                            <p class="text-muted small mb-0">Actualiza tus datos de contacto, contraseña e información personal.</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>

    </div>

    <!-- Scripts de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>