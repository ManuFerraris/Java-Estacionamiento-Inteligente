# **PROPUESTA: Estacionamiento Inteligente**

## **Grupo:**

### **Integrantes:**
- 51937 - Ferraris Manuel
- 48952 - Franco Natalia
- 50430 - Munné María Lucía

## **Descripción**
 El estacionamiento cuenta con la posibilidad de brindar el servicio a clientes de forma por hora, media estadia y estadia completa. Dependiendo de este tiempo de estacionamiento sera la forma de administrar el estacionamiento, ya que si es por hora, el cliente es asignado a un estacionamiento para su vehículo y el mismo se va con la llave, a diferencia de si es media estadía o  estadía completa ya que el cliente debe dejar la llave porque el vehículo puede ser trasladado de estacionamiento.
Del vehículo se registra nro de patente, tipo de vehículo (moto, auto, utilitario, chata, camioneta); nombre, apellido, nro de telefono y direccion del propietario; y en el momento de registrar todos estos datos se calcula el monto de la estadía y se paga en el momento (efectivo, transferencia, debito o credito).
Puede ocurrir que el cliente en una estadía (de cualquier tipo menos la “x hora”) retire el vehículo y luego vuelva. El sistema debe contemplar esos casos con una fecha y hora de salida parcial y regreso dentro del transcurso de la estadía para no asignar más vehículos de lo que permite la capacidad física.

## **Entidades**
<img width="1372" height="1120" alt="image" src="https://github.com/user-attachments/assets/52551d39-3767-4a9d-ad49-515169292c60" />

- **Usuario**: numero, apellido, contraseña, direccion, fecha_baja, mail, mail_recuperacion, nombre, nombre_usuario, numero_telefono, rol.
- **Vehiculo**: patente, descripcion.
- **TipoVehiculo**: numero, nombre.
- **TipoEstadia**: numero, descripcion, cupo.
-
- **TipoPlan**: codigo, fecha_baja, detalle.
- **Pago**: numero, fecha_hora, monto, tipoPago, estado, id_mercado_pago.
- **Pago_Suscripcion**: id_pago_suscripcion, estado, fecha_hora_emision, fecha_hora_pago, id_mercado_paga, monto, tipo_pago, fecha_desde, numero.
- **Cochera**: codigo, nombre, direccion, descripcion.
- **Lugar**: codigo, numero_piso, descripcion.
- **PrecioHistoricoTV**: fecha_desde, precio.
- **PrecioHistoricoTP**: fechaDesde, precio.
- **Beneficio**: numero, fecha_creacion, fecha_baja, descripcion.
- **HistoricoSalidas**: fecha_hora_salida_parcial, fecha_hora_regreso_parcial, fecha_hora_regreso_real.
- **lugar_tipoEstadia**: fechaDesde.
- **Suscripcion**: estado, fecha_desde, fecha_hasta.
- **Reserva**: fechaDesde, notifiacion_tolerancia_enviada, notifiacion_vencimiento_enviada bit, estado, fecha_hasta_real, fecha_hasta_tentativa, senia.
- **Cochera**: codigo, descripcion, direccion, nombre.
- 
 
## **Alcance Funcional**

## **Alcance Funcional**

### _Regularidad:_
| Requerimiento | Detalle / Listado de casos incluidos |
|---|---|
| **ABMC Simple** | TipoVehiculo, TipoPlan, TipoEstadia, Cochera. |
| **ABMC Dependiente** | Vehiculo, Usuario, Lugar (Plazas), PrecioHistoricoTV, PrecioHistoricoTP. |
| **CU NO-ABMC** | Registro de clientes, Gestión del Perfil de Usuario, Simulación de Terminal de Barrera (Ingresos/Egresos). |
| **Listados Simples** | Listado de Tipos de Vehículos y sus históricos de precios. <br> Listado de Tipos de Planes y sus históricos de precios. <br> Listado de Cocheras y asignaciones activas. |

### _Aprobación Directa:_
| Requerimiento | Detalle / Listado de casos incluidos |
|---|---|
| **ABMC Completo** | Todas las entidades del modelo de dominio integradas con borrado lógico y validaciones relacionales. |
| **CU "Complejo" (Nivel Resumen)** | **Gestión Integral de Reservas y Suscripciones:** Cálculo dinámico de tarifas, control de capacidad física (salidas parciales), y procesamiento de pagos. <br> **Asignación Operativa:** Relación N a M para asignar Encargados a Cocheras. |
| **Listados Complejos** | **Reporte Financiero (Dashboard):** Filtros por rango de fechas y origen (Reservas/Suscripciones) calculando la recaudación total. <br> Listado general de Suscripciones (Activas/Canceladas) asociadas a usuarios. <br> Listado de Usuarios con sus roles y cocheras asignadas. |
| **Niveles de Acceso** | `CLIENTE`, `ENCARGADO` (Operativa en cochera), `TRABAJADOR` (Municipal), `ADMIN` (Gestión total municipal). |
| **Requerimientos Extra (Obligatorios y Agregados)** | - **Integración API Mercado Pago:** Checkout y Webhooks de acreditación automática. <br> - **Seguridad:** Encriptación de contraseñas con BCrypt. <br> - **Trazabilidad:** Sistema centralizado de logs (`AppLogger`). <br> - **Infraestructura:** Seeders automáticos de base de datos (`AppInitializationListener`). <br> - **Notificaciones:** Envío de emails transaccionales a los usuarios e Histórico de Salidas. |
