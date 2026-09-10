package estacionamiento.domain;

//Usamos los enums para mantener la consistencia de estados
public enum EstadoPago {
	PENDIENTE,  // Cuando se emitió (fechaHoraEmision) pero aún no se abonó
	APROBADO,   // Momento en que MP lo aprueba.
    PAGADO,     // Cuando ya se registró el pago (fechaHoraPago)
    VENCIDO,    // Si se pasó la fecha límite sin pagar
    CANCELADO,   // Si se anuló la suscripción o el comprobante
    RECHAZADO
}
