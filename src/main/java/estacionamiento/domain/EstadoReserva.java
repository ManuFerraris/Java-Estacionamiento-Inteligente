package estacionamiento.domain;

public enum EstadoReserva {
	PENDIENTE,         // Pagó/Señó, pero el auto aún no llegó (ocupa lugar)
    EN_CURSO,          // El auto está físicamente en la cochera (ocupa lugar)
    SALIDA_PARCIAL,    // Salió, pero tiene la estadía vigente (ocupa lugar)
    FINALIZADA,        // Terminó su tiempo o se fue definitivamente (libera lugar)
    CANCELADA,         // Anuló antes de ir (libera lugar)
}
