<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - Municipalidad de Rosario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            background-color: #ffffff;
            font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif;
        }

        /* Panel institucional izquierdo */
        .brand-panel {
            background: linear-gradient(145deg, #092447 0%, #145388 52%, #1d70b8 100%);
            position: relative;
            overflow: hidden;
        }

        .brand-panel::before,
        .brand-panel::after {
            content: "";
            position: absolute;
            border-radius: 50%;
            pointer-events: none;
        }

        .brand-panel::before {
            width: 460px;
            height: 460px;
            background: rgba(255, 255, 255, 0.05);
            top: -140px;
            right: -120px;
        }

        .brand-panel::after {
            width: 360px;
            height: 360px;
            background: rgba(56, 189, 248, 0.12);
            bottom: -120px;
            left: -90px;
        }

        .brand-content {
            max-width: 480px;
            z-index: 1;
        }

        .brand-title {
            font-size: clamp(2rem, 2.7vw, 2.85rem);
            line-height: 1.12;
            letter-spacing: -0.02em;
        }

        .brand-subtitle {
            color: rgba(255, 255, 255, 0.82);
            font-size: 1.08rem;
            line-height: 1.5;
        }

        .brand-badge {
            background: rgba(255, 255, 255, 0.14);
            border: 1px solid rgba(255, 255, 255, 0.24);
            backdrop-filter: blur(4px);
            font-size: 0.85rem;
            letter-spacing: 0.05em;
        }

        .step-badge {
            width: 38px;
            height: 38px;
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.14);
            display: inline-flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            font-weight: 700;
            font-size: 1rem;
        }

        .feature-title {
            font-size: 1.05rem;
        }

        .feature-desc {
            color: rgba(255, 255, 255, 0.78);
            font-size: 0.94rem;
        }

        .brand-footer {
            color: rgba(255, 255, 255, 0.75);
            font-size: 0.92rem;
        }

        /* Contenedor del formulario */
        .form-wrapper {
            width: 100%;
            max-width: 640px;
        }

        .register-title {
            font-size: 2rem;
            letter-spacing: -0.02em;
        }

        .section-label {
            font-size: 0.78rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.06em;
            color: #145388;
        }

        .form-floating > .form-control {
            height: 3.55rem;
            min-height: 3.55rem;
            font-size: 1rem;
        }

        .form-floating > label {
            font-size: 0.96rem;
            padding: 0.95rem 0.9rem;
        }

        .form-control:focus {
            border-color: #145388;
            box-shadow: 0 0 0 0.2rem rgba(20, 83, 136, 0.15);
        }

        .btn-institucional {
            background-color: #145388;
            border-color: #145388;
            font-size: 1.08rem;
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
            <div class="col-lg-5 brand-panel text-white p-5 d-none d-lg-flex flex-column justify-content-between">

                <div class="position-relative" style="z-index: 1;">
                    <span class="badge rounded-pill brand-badge text-uppercase px-3 py-2">
                        <i class="bi bi-shield-check me-2 text-info"></i>Portal Ciudadano · Municipalidad de Rosario
                    </span>
                </div>

                <div class="brand-content position-relative my-auto py-4">
                    <div class="d-flex align-items-center mb-3">
                        <i class="bi bi-person-badge-fill text-info me-3" style="font-size: 3.4rem; line-height: 1;"></i>
                        <h1 class="brand-title fw-bold mb-0">Sumate al<br>Portal Digital</h1>
                    </div>

                    <p class="brand-subtitle mb-5">
                        Creá tu cuenta gratuita en menos de un minuto y empezá a gestionar tus cocheras municipales.
                    </p>

                    <div class="d-flex flex-column gap-4">
                        <div class="d-flex align-items-start gap-3">
                            <span class="step-badge text-info">1</span>
                            <div>
                                <div class="feature-title fw-semibold">Registrá tus datos</div>
                                <div class="feature-desc">Usamos tu correo para enviarte confirmaciones y alertas de horario.</div>
                            </div>
                        </div>
                        <div class="d-flex align-items-start gap-3">
                            <span class="step-badge text-info">2</span>
                            <div>
                                <div class="feature-title fw-semibold">Cargá tus vehículos</div>
                                <div class="feature-desc">Asociá tus patentes para reservar en un par de clics.</div>
                            </div>
                        </div>
                        <div class="d-flex align-items-start gap-3">
                            <span class="step-badge text-info">3</span>
                            <div>
                                <div class="feature-title fw-semibold">Reservá y accedé con QR</div>
                                <div class="feature-desc">Pagá digitalmente y entrá a la cochera sin trámites en ventanilla.</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="brand-footer position-relative pt-3 border-top border-white border-opacity-25 d-flex justify-content-between" style="z-index: 1;">
                    <span><i class="bi bi-geo-alt me-1"></i>Rosario, Santa Fe</span>
                    <span>Estacionamiento Inteligente</span>
                </div>
            </div>

            <!-- COLUMNA DERECHA: Formulario de Registro -->
            <div class="col-12 col-lg-7 bg-white d-flex flex-column justify-content-center align-items-center p-4 p-sm-5">
                <div class="form-wrapper">

                    <!-- Encabezado compacto solo para móviles -->
                    <div class="text-center d-lg-none mb-4">
                        <div class="d-inline-flex align-items-center justify-content-center rounded-circle mb-2"
                             style="width: 60px; height: 60px; background-color: #e8f1fa; color: #145388;">
                            <i class="bi bi-person-badge-fill fs-2"></i>
                        </div>
                        <h4 class="fw-bold text-dark mb-0">Estacionamiento Inteligente</h4>
                        <p class="text-muted small mb-0">Municipalidad de Rosario</p>
                    </div>

                    <div class="mb-4">
                        <h2 class="register-title fw-bold text-dark mb-1">Crear cuenta</h2>
                        <p class="text-muted fs-6 mb-0">Completá tus datos para registrarte en el Portal del Ciudadano.</p>
                    </div>

                    <% 
                        String error = (String) request.getAttribute("error");
                        if (error != null) { 
                    %>
                        <div class="alert alert-danger d-flex align-items-center py-2 px-3 rounded-3 mb-4" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2 flex-shrink-0"></i>
                            <div><%= error %></div>
                        </div>
                    <% } %>

                    <form action="<%= request.getContextPath() %>/registro" method="POST" id="formRegistro">

                        <!-- SECCIÓN 1: Datos Personales -->
                        <div class="section-label mb-2">Datos personales</div>
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="text" class="form-control rounded-3" id="nombre" name="nombre"
                                           placeholder="Nombre" autocomplete="given-name" required autofocus>
                                    <label for="nombre" class="text-muted"><i class="bi bi-person me-2"></i>Nombre *</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="text" class="form-control rounded-3" id="apellido" name="apellido"
                                           placeholder="Apellido" autocomplete="family-name" required>
                                    <label for="apellido" class="text-muted"><i class="bi bi-person me-2"></i>Apellido *</label>
                                </div>
                            </div>
                        </div>

                        <div class="row g-3 mb-4">
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="tel" class="form-control rounded-3" id="numeroTelefono" name="numeroTelefono"
                                           placeholder="Teléfono" autocomplete="tel" required>
                                    <label for="numeroTelefono" class="text-muted"><i class="bi bi-telephone me-2"></i>Teléfono *</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="text" class="form-control rounded-3" id="direccion" name="direccion"
                                           placeholder="Dirección" autocomplete="street-address" required>
                                    <label for="direccion" class="text-muted"><i class="bi bi-geo-alt me-2"></i>Dirección *</label>
                                </div>
                            </div>
                        </div>

                        <!-- SECCIÓN 2: Contacto y Acceso -->
                        <div class="section-label mb-2">Contacto y credenciales</div>
                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="email" class="form-control rounded-3" id="mail" name="mail"
                                           placeholder="Correo electrónico" autocomplete="email" required>
                                    <label for="mail" class="text-muted"><i class="bi bi-envelope me-2"></i>Correo electrónico *</label>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-floating">
                                    <input type="email" class="form-control rounded-3" id="mailRecuperacion" name="mailRecuperacion"
                                           placeholder="Correo de recuperación" required>
                                    <label for="mailRecuperacion" class="text-muted"><i class="bi bi-envelope-check me-2"></i>Correo de recuperación *</label>
                                </div>
                            </div>
                        </div>

                        <div class="mb-3">
                            <div class="form-floating">
                                <input type="text" class="form-control rounded-3" id="nombreUsuario" name="nombreUsuario"
                                       placeholder="Nombre de usuario" autocomplete="username" required>
                                <label for="nombreUsuario" class="text-muted"><i class="bi bi-at me-2"></i>Nombre de usuario *</label>
                            </div>
                        </div>

                        <div class="row g-3 mb-2">
                            <div class="col-md-6">
                                <div class="position-relative">
                                    <div class="form-floating">
                                        <input type="password" class="form-control rounded-3 pe-5" id="contrasenia" name="contrasenia"
                                               placeholder="Contraseña" autocomplete="new-password" required minlength="6">
                                        <label for="contrasenia" class="text-muted"><i class="bi bi-lock me-2"></i>Contraseña *</label>
                                    </div>
                                    <button type="button"
                                            class="btn btn-toggle-pass position-absolute top-50 end-0 translate-middle-y me-2 px-2 py-1"
                                            data-target="contrasenia"
                                            aria-label="Mostrar contraseña"
                                            title="Mostrar u ocultar contraseña">
                                        <i class="bi bi-eye fs-5"></i>
                                    </button>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="position-relative">
                                    <div class="form-floating">
                                        <input type="password" class="form-control rounded-3 pe-5" id="confirmarContrasenia" name="confirmarContrasenia"
                                               placeholder="Confirmar contraseña" autocomplete="new-password" required minlength="6">
                                        <label for="confirmarContrasenia" class="text-muted"><i class="bi bi-lock-fill me-2"></i>Confirmar contraseña *</label>
                                    </div>
                                    <button type="button"
                                            class="btn btn-toggle-pass position-absolute top-50 end-0 translate-middle-y me-2 px-2 py-1"
                                            data-target="confirmarContrasenia"
                                            aria-label="Mostrar confirmación de contraseña"
                                            title="Mostrar u ocultar contraseña">
                                        <i class="bi bi-eye fs-5"></i>
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!-- Ayuda dinámica de contraseña -->
                        <div class="d-flex justify-content-between align-items-center mb-4 px-1">
                            <small class="text-muted">Mínimo 6 caracteres.</small>
                            <small id="feedbackPassword" class="fw-semibold"></small>
                        </div>

                        <button type="submit" id="btnRegistrar" class="btn btn-institucional btn-primary w-100 py-3 fw-semibold rounded-3 shadow-sm">
                            <span id="textoBtn"><i class="bi bi-person-plus me-2"></i>Crear cuenta</span>
                            <span id="spinnerBtn" class="spinner-border spinner-border-sm ms-2 d-none" role="status" aria-hidden="true"></span>
                        </button>

                        <hr class="my-4 text-muted opacity-25">

                        <div class="text-center fs-6">
                            <span class="text-muted">¿Ya tenés una cuenta?</span>
                            <a href="<%= request.getContextPath() %>/login" class="text-decoration-none fw-semibold ms-1" style="color: #145388;">
                                Iniciar sesión
                            </a>
                        </div>
                    </form>

                </div>
            </div>

        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // 1. Toggle reutilizable para ambos campos de contraseña
        document.querySelectorAll('.btn-toggle-pass').forEach(function (btn) {
            btn.addEventListener('click', function () {
                const input = document.getElementById(btn.dataset.target);
                const icono = btn.querySelector('i');
                const estaOculta = input.type === 'password';

                input.type = estaOculta ? 'text' : 'password';
                icono.classList.toggle('bi-eye', !estaOculta);
                icono.classList.toggle('bi-eye-slash', estaOculta);
                btn.setAttribute('aria-label', estaOculta ? 'Ocultar contraseña' : 'Mostrar contraseña');
                input.focus();
            });
        });

        // 2. Indicador en vivo de coincidencia de contraseñas
        const inputPass = document.getElementById('contrasenia');
        const inputConf = document.getElementById('confirmarContrasenia');
        const feedbackPass = document.getElementById('feedbackPassword');

        function verificarCoincidencia() {
            if (!inputConf.value) {
                feedbackPass.textContent = '';
                feedbackPass.className = 'fw-semibold';
                return;
            }
            if (inputPass.value === inputConf.value) {
                feedbackPass.innerHTML = '<i class="bi bi-check-circle-fill me-1"></i>Las contraseñas coinciden';
                feedbackPass.className = 'fw-semibold text-success';
            } else {
                feedbackPass.innerHTML = '<i class="bi bi-x-circle-fill me-1"></i>No coinciden';
                feedbackPass.className = 'fw-semibold text-danger';
            }
        }

        inputPass.addEventListener('input', verificarCoincidencia);
        inputConf.addEventListener('input', verificarCoincidencia);

        // 3. Validación al enviar + feedback de carga
        document.getElementById('formRegistro').addEventListener('submit', function (event) {
            if (inputPass.value !== inputConf.value) {
                event.preventDefault();
                Swal.fire({
                    icon: 'warning',
                    title: 'Verificá tu contraseña',
                    text: 'Las contraseñas ingresadas no coinciden.',
                    confirmButtonColor: '#145388'
                });
                return;
            }

            const btn = document.getElementById('btnRegistrar');
            const texto = document.getElementById('textoBtn');
            const spinner = document.getElementById('spinnerBtn');

            btn.disabled = true;
            texto.textContent = 'Creando cuenta...';
            spinner.classList.remove('d-none');
        });
    </script>
</body>
</html>