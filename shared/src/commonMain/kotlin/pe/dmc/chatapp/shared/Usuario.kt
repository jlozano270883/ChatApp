package pe.dmc.chatapp.shared

import kotlinx.datetime.Clock

class Usuario(
    val id: String,
    var nombre: String,
    var enLinea: Boolean,
    var fotoUrl: String? = null,        // ? = este valor PUEDE ser null
    var ultimaConexion: Long? = null,   // null = nunca se ha conectado
) {

    val iniciales: String
        get() = nombre.split(" ")
            .filter { it.isNotBlank() }
            .map { it.first().uppercaseChar() }
            .joinToString("")

    fun marcarConectado() { enLinea = true }
    fun marcarDesconectado() { enLinea = false }
}

// --- Null safety: funciones que operan sobre Usuario ------------------------
// El compilador obliga a decidir, en cada punto de uso, qué pasa si el valor
// nullable resulta ser null -- no hay forma de "olvidarse" de ese caso.
//
// Comentario línea por línea (coincide con la diapositiva "Código · Null
// safety en el modelo de datos" de la Sesión 20):

fun avatarODefault(u: Usuario): String =
    // ?. (safe call) evita el error si fotoUrl es null; ?: da un valor por defecto
    u.fotoUrl?.uppercase() ?: "https://dmc.pe/avatar-default.png"

fun formatearUltimaConexion(u: Usuario): String {
    val ultima = u.ultimaConexion            // tipo Long? — puede ser null
    // smart cast: dentro del if, 'ultima' ya se trata como Long (no Long?)
    //return if (ultima != null) "hace ${(System.currentTimeMillis() - ultima) / 1000}s"
    //else "Nunca se ha conectado"
    return if (ultima != null){
        val ahora = Clock.System.now().toEpochMilliseconds()
        "hace ${(ahora - ultima) / 1000}s"
    } else{
        "Nunca se ha conectado."
    }
}

// Declara una función que recibe un 'Usuario' que podría ser nulo (Usuario?) y asegura que devolverá un 'Usuario' que jamás será nulo (Usuario)
fun requerirUsuarioValido(u: Usuario?): Usuario =
    // 'requireNotNull' verifica el objeto: si es nulo detiene la app, si no es nulo extrae el valor de forma segura
    requireNotNull(u) {
        // Este bloque de código solo se ejecuta si 'u' es nulo, lanzando un error (IllegalArgumentException) con este mensaje exacto
        "Se esperaba un usuario autenticado"
    }

