package pe.dmc.chatapp.shared// Taller · Parte B — Coroutines y Flow: mensajes en tiempo real
// ===================================================================


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.datetime.Clock

// suspend: esta función puede "pausarse" sin bloquear el hilo que la llama
suspend fun obtenerMensajesIniciales(conversacionId: String): List<Mensaje> {
    delay(400.milliseconds)

    val ahora = Clock.System.now().toEpochMilliseconds()

    return listOf(
        Mensaje("m-100", "u1", "¿Sigues ahí?", ahora - 60_000),
        Mensaje("m-101", "u2", "Sí, revisando el build de KMP", ahora - 30_000),
    )
}

// Flow<T>: un stream de valores que van llegando EN EL TIEMPO — ideal para chat en vivo
fun mensajesEnVivo(conversacionId: String): Flow<Mensaje> = flow {
    var contador = 0                    // cuenta cuántos mensajes van emitidos

    var ahoraEnVivo = Clock.System.now().toEpochMilliseconds()

    while (true) {                      // 'en vivo' = nunca termina por sí solo
        delay(2000.milliseconds)   // espera 2 segundos antes de emitir el siguiente mensaje
        contador++
        emit(Mensaje("live-$contador", "u2", "Mensaje en vivo #$contador", ahoraEnVivo))
    }
}
