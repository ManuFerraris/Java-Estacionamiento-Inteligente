<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="estacionamiento.domain.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Portal - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f4f7fa;
            font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif;
        }

        /* Banner de bienvenida con la misma identidad que Login/Registro */
        .hero-portal {
            background: linear-gradient(135deg, #092447 0%, #145388 55%, #1d70b8 100%);
            position: relative;
            overflow: hidden;
        }

        .hero-portal::before {
            content: "";
            position: absolute;
            width: 340px;
            height: 340px;
            border-radius: 50%;
            background: rgba(56, 189, 248, 0.12);
            top: -140px;
            right: -60px;
            pointer-events: none;
        }

        .hero-badge {
            background: rgba(255, 255, 255, 0.14);
            border: 1px solid rgba(255, 255, 255, 0.24);
            font-size: 0.78rem;
            letter-spacing: 0.05em;
        }

        .icono-fondo {
            font-size: 9rem;
            position: absolute;
            right: 24px;
            bottom: -36px;
            opacity: 0.1;
            transform: rotate(-10deg);
            pointer-events: none;
        }

        /* Tarjetas de servicios */
        .service-card {
            border: 1px solid rgba(15, 23, 42, 0.06);
            transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease;
        }

        .service-card:hover {
            transform: translateY(-6px);
            box-shadow: 0 0.85rem 1.75rem rgba(11, 46, 89, 0.12) !important;
            border-color: rgba(20, 83, 136, 0.35);
        }

        .service-icon-box {
            width: 64px;
            height: 64px;
            border-radius: 16px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 1.85rem;
        }

        .service-cta {
            color: #145388;
            font-size: 0.9rem;
        }

        .service-cta i {
            transition: transform 0.2s ease;
            display: inline-block;
        }

        .service-card:hover .service-cta i {
            transform: translateX(4px);
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <!-- Navbar dinámico -->
    <%@ include file="navbar.jsp" %>

    <%
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        String nombreMostrar = (u != null && u.getNombre() != null) ? u.getNombre() : "Ciudadano";
    %>

    <main class="container px-4 py-4 py-md-5 flex-grow-1">

        <!-- Tarjeta de Bienvenida -->
        <div class="row mb-5">
            <div class="col-12">
                <div class="card border-0 shadow rounded-4 hero-portal text-white">
                    <div class="card-body p-4 p-md-5">
                        <div class="position-relative" style="z-index: 1;">
                            <span class="badge rounded-pill hero-badge text-uppercase px-3 py-2 mb-3">
                                <i class="bi bi-shield-check me-1 text-info"></i>Portal del Ciudadano · Municipalidad de Rosario
                            </span>
                            <h1 class="fw-bold mb-2 h2">¡Hola, <%= nombreMostrar %>!</h1>
                            <p class="mb-0 fs-5" style="color: rgba(255, 255, 255, 0.82); max-width: 640px;">
                                Gestioná tus reservas, suscripciones y vehículos en las cocheras municipales desde un solo lugar.
                            </p>
                        </div>
                        <i class="bi bi-p-circle-fill icono-fondo text-white"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Encabezado de Servicios -->
        <div class="d-flex align-items-center justify-content-between mb-4">
            <h5 class="text-dark fw-bold mb-0">
                <i class="bi bi-grid-fill me-2" style="color: #145388;"></i>Mis Servicios
            </h5>
            <span class="text-muted small d-none d-sm-inline">Seleccioná una opción para comenzar</span>
        </div>

        <!-- Grilla 4x1 en escritorio / 2x2 en tablet -->
        <div class="row g-4">

            <!-- Tarjeta 1: Mis Reservas -->
            <div class="col-sm-6 col-lg-3">
                <a href="<%= request.getContextPath() %>/mis-reservas-user" class="text-decoration-none d-block h-100">
                    <div class="card shadow-sm rounded-4 h-100 service-card bg-white">
                        <div class="card-body p-4 d-flex flex-column">
                            <div class="service-icon-box bg-primary bg-opacity-10 text-primary mb-3">
                                <i class="bi bi-calendar-check-fill"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Mis Reservas</h5>
                            <p class="text-muted small mb-4 flex-grow-1">
                                Reservá cocheras por hora o estadía, pagá online y generá tu pase QR.
                            </p>
                            <div class="service-cta fw-semibold pt-2 border-top d-flex align-items-center justify-content-between">
                                <span>Ir a reservas</span>
                                <i class="bi bi-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Tarjeta 2: Mis Suscripciones -->
            <div class="col-sm-6 col-lg-3">
                <a href="<%= request.getContextPath() %>/mis-suscripciones-user" class="text-decoration-none d-block h-100">
                    <div class="card shadow-sm rounded-4 h-100 service-card bg-white">
                        <div class="card-body p-4 d-flex flex-column">
                            <div class="service-icon-box bg-success bg-opacity-10 text-success mb-3">
                                <i class="bi bi-card-checklist"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Mis Suscripciones</h5>
                            <p class="text-muted small mb-4 flex-grow-1">
                                Consultá tus planes vigentes, beneficios activos e historial de pagos.
                            </p>
                            <div class="service-cta fw-semibold pt-2 border-top d-flex align-items-center justify-content-between">
                                <span>Ver planes</span>
                                <i class="bi bi-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Tarjeta 3: Mis Vehículos -->
            <div class="col-sm-6 col-lg-3">
                <a href="<%= request.getContextPath() %>/mis-vehiculos-user" class="text-decoration-none d-block h-100">
                    <div class="card shadow-sm rounded-4 h-100 service-card bg-white">
                        <div class="card-body p-4 d-flex flex-column">
                            <div class="service-icon-box bg-info bg-opacity-10 text-info mb-3">
                                <i class="bi bi-car-front-fill"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Mis Vehículos</h5>
                            <p class="text-muted small mb-4 flex-grow-1">
                                Administrá tus patentes registradas para agilizar cada reserva.
                            </p>
                            <div class="service-cta fw-semibold pt-2 border-top d-flex align-items-center justify-content-between">
                                <span>Gestionar autos</span>
                                <i class="bi bi-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Tarjeta 4: Mi Perfil -->
            <div class="col-sm-6 col-lg-3">
                <a href="<%= request.getContextPath() %>/perfil-user" class="text-decoration-none d-block h-100">
                    <div class="card shadow-sm rounded-4 h-100 service-card bg-white">
                        <div class="card-body p-4 d-flex flex-column">
                            <div class="service-icon-box bg-secondary bg-opacity-10 text-secondary mb-3">
                                <i class="bi bi-person-vcard-fill"></i>
                            </div>
                            <h5 class="text-dark fw-bold mb-2">Mi Perfil</h5>
                            <p class="text-muted small mb-4 flex-grow-1">
                                Actualizá tus datos personales, correo de contacto y contraseña.
                            </p>
                            <div class="service-cta fw-semibold pt-2 border-top d-flex align-items-center justify-content-between">
                                <span>Editar perfil</span>
                                <i class="bi bi-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

        </div>
    </main>

    <!-- Scripts de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>