package pe.dmc.chatapp.shared// Taller · Parte B — Funciones de extensión
// =============================================
// Agregan un método a una clase que NO controlamos -- aquí, Long, de la
// librería estándar -- sin heredarla ni envolverla en un wrapper. Se llaman
// exactamente como un método normal de la clase: mensaje.timestamp.aHoraChat().
//
// Comentarios línea por línea (coinciden con la diapositiva "Funciones de
// extensión" de la Sesión 20).

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


// Función de extensión sobre Long: se llama como si fuera un método de Long
fun Long.aHoraChat(): String {
    val instante = Instant.fromEpochMilliseconds(this)

    val horaLocal = instante.toLocalDateTime(TimeZone.currentSystemDefault())

    val horaStr = horaLocal.hour.toString().padStart(2,'0')
    val minutoStr = horaLocal.minute.toString().padStart(2, '0')

    return "$horaStr:$minutoStr"      // ej: "14:05"
}

// Función de extensión sobre Mensaje: mensaje.esDe("u1") en vez de una función suelta
fun Mensaje.esDe(usuarioId: String): Boolean = this.autorId == usuarioId
