<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Municipalidad de Rosario - Ingreso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #ffffff;
            font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif;
        }

        /* Panel institucional izquierdo a pantalla completa */
        .brand-panel {
            background: linear-gradient(145deg, #092447 0%, #145388 52%, #1d70b8 100%);
            position: relative;
            overflow: hidden;
        }

        /* Formas geométricas sutiles en CSS puro */
        .brand-panel::before,
        .brand-panel::after {
            content: "";
            position: absolute;
            border-radius: 50%;
            pointer-events: none;
        }

        .brand-panel::before {
            width: 520px;
            height: 520px;
            background: rgba(255, 255, 255, 0.05);
            top: -150px;
            right: -120px;
        }

        .brand-panel::after {
            width: 400px;
            height: 400px;
            background: rgba(56, 189, 248, 0.12);
            bottom: -130px;
            left: -100px;
        }

        .brand-content {
            max-width: 620px;
            z-index: 1;
        }

        .brand-title {
            font-size: clamp(2.25rem, 3.2vw, 3.25rem);
            line-height: 1.12;
            letter-spacing: -0.02em;
        }

        .brand-subtitle {
            color: rgba(255, 255, 255, 0.82);
            font-size: 1.15rem;
            line-height: 1.5;
        }

        .brand-badge {
            background: rgba(255, 255, 255, 0.14);
            border: 1px solid rgba(255, 255, 255, 0.24);
            backdrop-filter: blur(4px);
            font-size: 0.9rem;
            letter-spacing: 0.05em;
        }

        .feature-icon {
            width: 52px;
            height: 52px;
            border-radius: 14px;
            background: rgba(255, 255, 255, 0.14);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            font-size: 1.45rem;
        }

        .feature-title {
            font-size: 1.1rem;
        }

        .feature-desc {
            color: rgba(255, 255, 255, 0.78);
            font-size: 0.98rem;
        }

        .brand-footer {
            color: rgba(255, 255, 255, 0.75);
            font-size: 0.95rem;
        }

        /* Formulario derecho con mejor escala */
        .form-wrapper {
            width: 100%;
            max-width: 460px;
        }

        .login-title {
            font-size: 2.15rem;
            letter-spacing: -0.02em;
        }

        .form-floating > .form-control {
            height: 3.85rem;
            min-height: 3.85rem;
            font-size: 1.05rem;
        }

        .form-floating > label {
            font-size: 1.02rem;
            padding: 1.1rem 0.9rem;
        }

        .form-control:focus {
            border-color: #145388;
            box-shadow: 0 0 0 0.2rem rgba(20, 83, 136, 0.15);
        }

        .btn-institucional {
            background-color: #145388;
            border-color: #145388;
            font-size: 1.1rem;
            transition: background-color 0.2s ease;
        }

        .btn-institucional:hover {
            background-color: #0b2e59;
            border-color: #0b2e59;
        }

        .btn-toggle-pass {
            z-index: 5;
            color: #6c757d;
            border: none;
            background: transparent;
        }

        .btn-toggle-pass:hover {
            color: #145388;
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100">

    <%@ include file="navbar.jsp" %>

    <main class="container-fluid p-0 flex-grow-1 d-flex">
        <div class="row g-0 w-100 flex-grow-1">

            <!-- COLUMNA IZQUIERDA: Panel Institucional -->
            <div class="col-lg-7 brand-panel text-white p-5 px-xl-5 d-none d-lg-flex flex-column justify-content-between">
                
                <!-- Cabecera superior -->
                <div class="position-relative" style="z-index: 1;">
                    <span class="badge rounded-pill brand-badge text-uppercase px-4 py-2">
                        <i class="bi bi-shield-check me-2 text-info"></i>Portal Ciudadano · Municipalidad de Rosario
                    </span>
                </div>

                <!-- Bloque central -->
                <div class="brand-content position-relative my-auto py-4">
                    <div class="d-flex align-items-center mb-4">
                        <i class="bi bi-buildings-fill text-info me-3" style="font-size: 4rem; line-height: 1;"></i>
                        <h1 class="brand-title fw-bold mb-0">Estacionamiento<br>Inteligente</h1>
                    </div>

                    <p class="brand-subtitle mb-5">
                        Gestioná tus cocheras municipales de forma ágil, segura y desde un solo lugar.
                    </p>

                    <div class="d-flex flex-column gap-4">
                        <div class="d-flex align-items-center gap-3">
                            <span class="feature-icon text-info"><i class="bi bi-calendar-check"></i></span>
                            <div>
                                <div class="feature-title fw-semibold">Reservas online en tiempo real</div>
                                <div class="feature-desc">Consultá disponibilidad por franja horaria al instante.</div>
                            </div>
                        </div>
                        <div class="d-flex align-items-center gap-3">
                            <span class="feature-icon text-info"><i class="bi bi-qr-code-scan"></i></span>
                            <div>
                                <div class="feature-title fw-semibold">Acceso rápido con pase QR</div>
                                <div class="feature-desc">Ingresá y salí de la barrera escaneando desde tu celular.</div>
                            </div>
                        </div>
                        <div class="d-flex align-items-center gap-3">
                            <span class="feature-icon text-info"><i class="bi bi-credit-card"></i></span>
                            <div>
                                <div class="feature-title fw-semibold">Pagos digitales y suscripciones</div>
                                <div class="feature-desc">Aboná señas, estadías y planes con acreditación automática.</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Pie inferior -->
                <div class="brand-footer position-relative pt-3 border-top border-white border-opacity-25 d-flex justify-content-between" style="z-index: 1;">
                    <span><i class="bi bi-geo-alt me-1"></i>Rosario, Santa Fe, Argentina</span>
                    <span>Sistema de Gestión de Cocheras</span>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Formulario de Acceso -->
            <div class="col-12 col-lg-5 bg-white d-flex flex-column justify-content-center align-items-center p-4 p-sm-5">
                <div class="form-wrapper">

                    <!-- Encabezado compacto solo para móviles -->
                    <div class="text-center d-lg-none mb-4">
                        <div class="d-inline-flex align-items-center justify-content-center rounded-circle mb-2"
                             style="width: 64px; height: 64px; background-color: #e8f1fa; color: #145388;">
                            <i class="bi bi-buildings-fill fs-2"></i>
                        </div>
                        <h4 class="fw-bold text-dark mb-0">Estacionamiento Inteligente</h4>
                        <p class="text-muted mb-0">Municipalidad de Rosario</p>
                    </div>

                    <div class="mb-4">
                        <h2 class="login-title fw-bold text-dark mb-2">Iniciar sesión</h2>
                        <p class="text-muted fs-6 mb-0">Ingresá tus credenciales para acceder al sistema.</p>
                    </div>

                    <% 
                        String error = (String) request.getAttribute("error");
                        if (error != null) { 
                    %>
                        <div class="alert alert-danger d-flex align-items-center py-2 px-3 rounded-3" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2 flex-shrink-0"></i>
                            <div><%= error %></div>
                        </div>
                    <% } %>

                    <% 
                        String exitoRegistro = (String) request.getAttribute("exitoRegistro");
                        if (exitoRegistro != null) { 
                    %>
                        <div class="alert alert-success d-flex align-items-center py-2 px-3 rounded-3" role="alert">
                            <i class="bi bi-check-circle-fill me-2 flex-shrink-0"></i>
                            <div><%= exitoRegistro %></div>
                        </div>
                    <% } %>

                    <form action="<%= request.getContextPath() %>/login" method="POST" id="formLogin">
                        
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control rounded-3" id="nombreUsuario" name="nombreUsuario"
                                   placeholder="Usuario" autocomplete="username" required autofocus>
                            <label for="nombreUsuario" class="text-muted">
                                <i class="bi bi-person me-2"></i>Usuario
                            </label>
                        </div>

                        <!-- Contraseña con botón de mostrar/ocultar -->
                        <div class="position-relative mb-4">
                            <div class="form-floating">
                                <input type="password" class="form-control rounded-3 pe-5" id="contrasenia" name="contrasenia"
                                       placeholder="Contraseña" autocomplete="current-password" required>
                                <label for="contrasenia" class="text-muted">
                                    <i class="bi bi-lock me-2"></i>Contraseña
                                </label>
                            </div>
                            <button type="button"
                                    class="btn btn-toggle-pass position-absolute top-50 end-0 translate-middle-y me-2 px-2 py-1"
                                    id="btnTogglePassword"
                                    aria-label="Mostrar contraseña"
                                    title="Mostrar u ocultar contraseña">
                                <i class="bi bi-eye fs-4" id="iconoPassword"></i>
                            </button>
                        </div>

                        <button type="submit" id="btnIngresar" class="btn btn-institucional btn-primary w-100 py-3 fw-semibold rounded-3 shadow-sm">
                            <span id="textoBtn">Ingresar</span>
                            <span id="spinnerBtn" class="spinner-border spinner-border-sm ms-2 d-none" role="status" aria-hidden="true"></span>
                        </button>

                        <hr class="my-4 text-muted opacity-25">

                        <div class="text-center fs-6">
                            <span class="text-muted">¿Todavía no tenés una cuenta?</span>
                            <a href="<%= request.getContextPath() %>/registro" class="text-decoration-none fw-semibold ms-1" style="color: #145388;">
                                Registrate aquí
                            </a>
                        </div>
                    </form>

                </div>
            </div>

        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // 1. Mostrar / Ocultar contraseña
        const inputPass = document.getElementById('contrasenia');
        const btnToggle = document.getElementById('btnTogglePassword');
        const iconoPass = document.getElementById('iconoPassword');

        btnToggle.addEventListener('click', function () {
            const estaOculta = inputPass.type === 'password';
            inputPass.type = estaOculta ? 'text' : 'password';

            iconoPass.classList.toggle('bi-eye', !estaOculta);
            iconoPass.classList.toggle('bi-eye-slash', estaOculta);
            btnToggle.setAttribute('aria-label', estaOculta ? 'Ocultar contraseña' : 'Mostrar contraseña');

            inputPass.focus();
        });

        // 2. Feedback visual al enviar el formulario (evita doble submit)
        document.getElementById('formLogin').addEventListener('submit', function () {
            const btn = document.getElementById('btnIngresar');
            const texto = document.getElementById('textoBtn');
            const spinner = document.getElementById('spinnerBtn');

            btn.disabled = true;
            texto.textContent = 'Ingresando...';
            spinner.classList.remove('d-none');
        });
    </script>
</body>
</html>