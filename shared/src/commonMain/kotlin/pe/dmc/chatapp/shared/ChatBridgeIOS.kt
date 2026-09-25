package pe.dmc.chatapp.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun escucharMensajesEnVivo(
    conversacionID: String,
    alRecibir: (Mensaje) -> Unit,
): Job {
    val scope = CoroutineScope(Dispatchers.Main)

    return scope.launch {
        mensajesEnVivo(conversacionID).collect { mensaje -> alRecibir(mensaje) }
    }
}