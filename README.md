# **PROPUESTA: Estacionamiento Inteligente**

## **Grupo:**

### **Integrantes:**
- 51937 - Ferraris Manuel **.**
- 48952 - Franco Natalia
- 50430 - Munné María Lucía

## **Descripción**
 El estacionamiento cuenta con la posibilidad de brindar el servicio a clientes de forma mensual, diaria, o por hora. Dependiendo de este tiempo de estacionamiento sera la forma de administrar el estacionamiento, ya que si es por hora, el cliente es asignado a un estacionamiento para su vehículo y el mismo se va con la llave, a diferencia de si es media estadía o  estadía completa ya que el cliente debe dejar la llave porque el vehículo puede ser trasladado de estacionamiento.
Del vehículo se registra nro de patente, tipo de vehículo (moto, auto, utilitario, chata, camioneta); nombre, apellido, nro de telefono y direccion del propietario; y en el momento de registrar todos estos datos se calcula el monto de la estadía y se paga en el momento (efectivo, transferencia, debito o credito).
Puede ocurrir que el cliente en una estadía (de cualquier tipo menos la “x hora”) retire el vehículo y luego vuelva. El sistema debe contemplar esos casos con una fecha y hora de salida parcial y regreso dentro del transcurso de la estadía para no asignar más vehículos de lo que permite la capacidad física.

## **Entidades**
- **Usuario**: idUsuario, nombre, apellido, numeroTelefono, direccion, mail, nombreUsuario, contraseña, fechaBaja.
- **Vehiculo**: patente, descripcion.
- **TipoVehiculo**: idTipo, nombre.
- **TipoEstadia**: numero, descripcion, cupo.
- **Pago**: numero, fechaHora, monto, tipoPago, estado.
- **Pago_Suscripcion**: numero, fechaHora, monto, tipoPago, estado.
- **Lugar**: codigo, numeroPiso, descripcion.
- **TipoPlan**: codigo, nombre, detalle.
- **PrecioHistoricoTV**: fechaDesde, precio.
- **PrecioHistoricoTP**: fechaDesde, precio.
- **Beneficio**: numero, fechaCreacion, fechaBaja.
- **HistoricoSalidas**: fechaSalida, horaSalidaParcial, horaRegresoParcial, horaSalidaReal, horaRegresoReal.
- **lugar_tipoEstadia**: fechaDesde.
- **Suscripcion**: fechaD, fechaH.
- **Reserva**: fechaDesde, fechaHastaTentativa, FechaHastaREal, estado, seña.
 
## **Alcance Funcional**

### _Regularidad:_
|Requerimiento|Detalle/Listado de casos incluidos|
|---|---|
|ABMC Simple|Vehiculo. <br> Usuario. <br> TipoPlan.|
|ABMC dependiente|PrecioHistoricoTP. <br> PrecioHistoricoTV.|
|CU NO-ABMC|Realizar Reserva. <br> Realizar Suscripcion.|
|Listado simple|Cantidad de usuarios morosos <br> Listado de reservas el último mes. <br> Listado de tipos de vehículos que más concurren a los estacionamientos.|

### _Aprobación Directa_
|Requerimiento|Detalle/Listado de casos incluidos|
|---|---|
|ABMC|Todas las entidades mencionadas anteriormente|
|CU "Complejo"(nivel resumen)|Gestión de Reserva|
|Listado complejo|Listado mensual del histórico de salidas de las reservas cuyo tipo de estadía es “Mensual” en un período de tiempo determinado. <br> Listado de suscripciones impagas por usuario.|
|Nivel de acceso|user. <br> adminCochera. <br> superAdmin.|
|Requerimiento extra obligatorio|Envío de emails.|
