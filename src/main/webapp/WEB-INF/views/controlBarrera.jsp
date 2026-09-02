<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Terminal de Control - MR Estacionamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        body { background-color: #e9ecef; }
        .terminal-card { max-width: 650px; margin: 10vh auto; }
        #lectorQR { letter-spacing: 2px; }
        #lectorQR:focus { box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25); border-color: #0d6efd; }
        
        /* Ajustes para hacer el switch más grande y fácil de tocar en tablets */
        .form-check-input { width: 4em !important; height: 2em !important; cursor: pointer; }
        .modo-activo { color: #0d6efd !important; font-weight: 800 !important; }
        .modo-inactivo { color: #adb5bd !important; font-weight: 500 !important; }
    </style>
</head>
<body>

    <div class="container terminal-card">
        <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
            
            <div class="card-header bg-dark text-white text-center py-4">
                <h3 class="mb-1 fw-bold"><i class="bi bi-upc-scan me-2"></i>Control de Accesos</h3>
                <span class="text-white-50 small text-uppercase tracking-wide">Terminal Operador</span>
            </div>
            
            <div class="card-body p-5 text-center">
                
                <!-- SELECTOR DE MODO: ENTRADA / SALIDA -->
                <div class="d-flex justify-content-center align-items-center mb-5 bg-light py-3 rounded-4 border">
                    <span class="fs-5 modo-activo me-3" id="labelEntrada">INGRESO</span>
                    
                    <div class="form-check form-switch m-0 p-0 d-flex align-items-center">
                        <input class="form-check-input mx-3 mt-0" type="checkbox" role="switch" id="switchModo">
                    </div>
                    
                    <span class="fs-5 modo-inactivo ms-3" id="labelSalida">SALIDA</span>
                </div>
                
                <!-- Input del Lector QR -->
                <input type="text" id="lectorQR" class="form-control form-control-lg text-center mx-auto shadow-sm" 
                       placeholder="[ Esperando escaneo QR... ]" autofocus autocomplete="off" style="max-width: 400px;">
                
                <div id="resultadoEscaneo" class="mt-5" style="min-height: 120px;">
                    <div class="text-muted opacity-50">
                        <i class="bi bi-shield-lock" style="font-size: 3rem;"></i>
                        <p class="mt-2 small">Sistema en espera</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const switchModo = document.getElementById('switchModo');
        const labelEntrada = document.getElementById('labelEntrada');
        const labelSalida = document.getElementById('labelSalida');
        
        // Cambio visual de colores al tocar el Switch
        switchModo.addEventListener('change', function() {
            if (this.checked) {
                // Modo Salida
                labelSalida.classList.replace('modo-inactivo', 'modo-activo');
                labelEntrada.classList.replace('modo-activo', 'modo-inactivo');
            } else {
                // Modo Entrada
                labelEntrada.classList.replace('modo-inactivo', 'modo-activo');
                labelSalida.classList.replace('modo-activo', 'modo-inactivo');
            }
            // Devolver foco al input para que no deje de leer el lector USB
            document.getElementById('lectorQR').focus();
        });

        // Evento de lectura del QR
        document.getElementById("lectorQR").addEventListener("change", function(e) {
            let datosQR = e.target.value.split('|');
            
            if (datosQR.length >= 3) {
                // Evaluamos el switch para saber qué acción mandar al Servlet
                let operacionSeleccionada = switchModo.checked ? 'SALIDA' : 'INGRESO';
                procesarBarrera(operacionSeleccionada, datosQR[0], datosQR[1], datosQR[2]);
            } else {
                document.getElementById("resultadoEscaneo").innerHTML = 
                    `<div class="alert alert-warning py-3"><i class="bi bi-exclamation-triangle-fill me-2"></i>QR inválido.</div>`;
            }
            
            e.target.value = "";
            e.target.focus(); 
        });

        function procesarBarrera(operacion, patente, usuario, fechaDesde) {
            document.getElementById("resultadoEscaneo").innerHTML = 
                `<div class="spinner-border text-primary" role="status"></div>
                 <p class="mt-2 text-primary fw-bold">Validando patente ${patente}...</p>`;

            // Hacemos el POST mandando el nuevo parámetro 'operacion'
            fetch('<%= request.getContextPath() %>/api/control-barrera', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({
                    'operacion': operacion,
                    'patente': patente,
                    'numeroUsuario': usuario,
                    'fechaDesde': fechaDesde
                })
            })
            .then(response => {
                const status = response.status;
                return response.json().then(data => ({status, data}));
            })
            .then(res => {
                const div = document.getElementById("resultadoEscaneo");
                
                if (res.status === 200) {
                    div.innerHTML = `
                        <div class="alert alert-success border-success text-success p-4 rounded-4 shadow-sm">
                            <i class="bi bi-check-circle-fill d-block mb-2" style="font-size: 2.5rem;"></i>
                            <h4 class="fw-bold mb-1">ACCESO CONCEDIDO</h4>
                            <p class="mb-0">${res.data.mensaje}</p>
                        </div>`;
                } else if (res.status === 402) {
                    div.innerHTML = `
                        <div class="alert alert-danger border-danger p-4 rounded-4 shadow-sm">
                            <i class="bi bi-exclamation-octagon-fill text-danger d-block mb-2" style="font-size: 2.5rem;"></i>
                            <h4 class="fw-bold text-danger mb-2">TIEMPO EXCEDIDO</h4>
                            <div class="bg-white text-dark py-2 px-3 rounded-3 d-inline-block border border-danger mb-2">
                                <span class="fw-bold fs-5">Monto a cobrar: $${res.data.monto}</span>
                            </div>
                            <p class="small mb-0 text-danger-emphasis">${res.data.mensaje}</p>
                        </div>`;
                } else {
                    div.innerHTML = `
                        <div class="alert alert-warning p-4 rounded-4 shadow-sm">
                            <i class="bi bi-x-circle-fill d-block mb-2 text-warning" style="font-size: 2.5rem;"></i>
                            <h5 class="fw-bold text-dark mb-0">Operación Denegada</h5>
                            <p class="text-dark small mt-2">${res.data.error}</p>
                        </div>`;
                }
            })
            .catch(error => {
                document.getElementById("resultadoEscaneo").innerHTML = 
                    `<div class="alert alert-dark">No se pudo conectar con el servidor.</div>`;
            });
        }
    </script>
</body>
</html>