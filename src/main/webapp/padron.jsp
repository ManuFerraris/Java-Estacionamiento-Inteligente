<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Control de Morosidad</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; text-align: left; }
        th, td { padding: 10px; border: 1px solid #ccc; }
        th { background-color: #f4f4f4; }
        .texto-rojo { color: red; font-weight: bold; }
        .texto-verde { color: green; font-weight: bold; }
        .btn-bloqueo { background-color: #d9534f; color: white; border: none; padding: 5px 10px; cursor: pointer; }
        .btn-cobro { background-color: #f0ad4e; color: white; border: none; padding: 5px 10px; cursor: pointer; }
        .filtros { background-color: #e9ecef; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
    </style>
</head>
<body>

    <h2>Padrón de Suscriptores y Control de Morosidad</h2>

    <!-- Panel de Filtros Dinámicos -->
    <div class="filtros">
        <form action="padron-morosidad" method="GET">
            <label>Estado Suscripción:</label>
            <select name="filtroSuscripcion">
                <option value="">-- Todos --</option>
                <option value="ACTIVA" ${filtroSuscripcion == 'ACTIVA' ? 'selected' : ''}>Activa</option>
                <option value="PAUSADA" ${filtroSuscripcion == 'PAUSADA' ? 'selected' : ''}>Pausada</option>
                <option value="CANCELADA" ${filtroSuscripcion == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
                <option value="VENCIDA" ${filtroSuscripcion == 'VENCIDA' ? 'selected' : ''}>Vencida</option>
            </select>

            <label style="margin-left: 15px;">Último Pago:</label>
            <select name="filtroPago">
                <option value="">-- Todos --</option>
                <option value="PENDIENTE" ${filtroPago == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                <option value="APROBADO" ${filtroPago == 'APROBADO' ? 'selected' : ''}>Aprobado</option>
                <option value="PAGADO" ${filtroPago == 'PAGADO' ? 'selected' : ''}>Pagado</option>
                <option value="VENCIDO" ${filtroPago == 'VENCIDO' ? 'selected' : ''}>Vencido</option>
                <option value="RECHAZADO" ${filtroPago == 'RECHAZADO' ? 'selected' : ''}>Rechazado</option>
            </select>

            <button type="submit" style="margin-left: 15px;">Filtrar Padrón</button>
        </form>
    </div>

    <!-- Resultados -->
    <c:choose>
        <c:when test="${empty padron}">
            <p><strong>No se encontraron suscriptores con los filtros seleccionados.</strong></p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>N° Usuario</th>
                    <th>Tipo Plan (Código)</th>
                    <th>Suscripción</th>
                    <th>Último Pago</th>
                    <th>Acciones Operativas</th>
                </tr>
                <c:forEach var="sub" items="${padron}">
                    <tr>
                        <!-- Usamos los identificadores reales de tus clases -->
                        <td>${sub.usuario.numero}</td>
                        <td>${sub.tipoPlan.codigo}</td>
                        
                        <!-- Resaltado visual de morosidad -->
                        <td class="${sub.estado == 'VENCIDA' ? 'texto-rojo' : 'texto-verde'}">
                            ${sub.estado}
                        </td>
                        <td class="${(sub.ultimoPago == 'RECHAZADO' || sub.ultimoPago == 'VENCIDO') ? 'texto-rojo' : ''}">
                            ${sub.ultimoPago}
                        </td>
                        
                        <!-- Botonera condicional -->
                        <td>
                            <c:if test="${sub.ultimoPago == 'RECHAZADO' || sub.ultimoPago == 'VENCIDO' || sub.estado == 'VENCIDA'}">
                                <button class="btn-bloqueo">Bloquear Acceso</button>
                                <button class="btn-cobro">Gestionar Cobro</button>
                            </c:if>
                            <c:if test="${sub.estado == 'ACTIVA' && (sub.ultimoPago == 'APROBADO' || sub.ultimoPago == 'PAGADO')}">
                                <button disabled>Acceso Habilitado</button>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>