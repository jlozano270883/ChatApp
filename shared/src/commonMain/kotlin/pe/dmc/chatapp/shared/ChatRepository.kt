package pe.dmc.chatapp.shared// Taller · Parte B — Colecciones funcionales + funciones de orden superior
// =============================================================================
// Todo lo de aquí opera SOBRE Usuario y Mensaje tal como quedaron al final
// de la Parte A -- el modelo de dominio no cambia, se opera SOBRE él.
//
// Comentarios línea por línea (coinciden con las diapositivas "Datos y
// colecciones: filter y map", "Datos y colecciones: fold" y "Código ·
// Funciones de orden superior" de la Sesión 20).

// --- filter / map ------------------------------------------------------------
// filter conserva SOLO los elementos que cumplen la condición del lambda { }

fun mensajesNoLeidosDe(mensajes: List<Mensaje>, autorId: String) =
    mensajes.filter { !it.leido && it.autorId == autorId }   // "quédate con los que..."

// map transforma CADA elemento, uno a uno, en algo nuevo (misma cantidad de elementos)
fun textosEnMayusculas(mensajes: List<Mensaje>) =
    mensajes.map { it.texto.uppercase() }        // por cada mensaje, su texto en mayúsculas

// groupingBy{}.eachCount() agrupa por una clave (aquí, el autor) y cuenta cuántos hay
fun resumenPorAutor(mensajes: List<Mensaje>): Map<String, Int> =
    mensajes.groupingBy { it.autorId }.eachCount()   // Map<autorId, cantidadDeMensajes>

// --- fold ---------------------------------------------------------------------
// data class: un "acumulador" con los 3 números que queremos ir sumando
data class ResumenChat(val total: Int, val noLeidos: Int, val caracteres: Long)

fun resumirConversacion(mensajes: List<Mensaje>): ResumenChat =
    // fold recorre la lista UNA vez, empezando en ResumenChat(0,0,0) (el "acc" inicial)
    mensajes.fold(ResumenChat(0, 0, 0)) { acc, m ->
        acc.copy(                                       // copy() arma el siguiente acc sin mutar el anterior
            total = acc.total + 1,                       // +1 mensaje contado
            noLeidos = acc.noLeidos + if (!m.leido) 1 else 0,  // +1 solo si no está leído
            caracteres = acc.caracteres + m.texto.length,  // suma la longitud de este mensaje
        )
    }
// resultado, por ejemplo: ResumenChat(total=23, noLeidos=5, caracteres=1840)

// --- Funciones de orden superior -----------------------------------------------
// ChatRepository guarda mensajes y notifica a quien esté escuchando -- SIN
// saber qué hace cada listener con la notificación (esa decisión es de quien
// se suscribe, no de ChatRepository).

class ChatRepository {
    private val mensajes = mutableListOf<Mensaje>()              // el "historial" guardado en memoria
    private val listeners = mutableListOf<(Mensaje) -> Unit>()   // lista de "timbres" suscritos

    // onNuevoMensaje RECIBE una función como parámetro — por eso es de orden superior.
    fun onNuevoMensaje(listener: (Mensaje) -> Unit) {
        listeners.add(listener)                    // solo guarda el listener, no lo ejecuta todavía
    }

    fun recibir(mensaje: Mensaje) {
        mensajes.add(mensaje)                                    // 1) se guarda en el historial
        listeners.forEach { listener -> listener(mensaje) }      // 2) se avisa a TODOS los suscritos
    }

    fun historial(filtro: (Mensaje) -> Boolean = { true }): List<Mensaje> =
        mensajes.filter(filtro)     // filtro tiene un valor por defecto: "acepta todos"
}