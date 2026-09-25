package pe.dmc.chatapp.shared// Taller · Parte A — Manejo de excepciones
// ============================================
// Excepciones PROPIAS en vez de "throw Exception(...)" genérica: quien
// hace catch puede distinguir exactamente qué fue lo que salió mal, en
// vez de recibir un mensaje de error genérico que no dice nada del negocio.

import kotlinx.datetime.Clock
import kotlin.random.Random

class MensajeInvalidoException(mensaje: String) : Exception(mensaje)
class UsuarioNoAutenticadoException : Exception("No hay una sesión activa")

@Throws(MensajeInvalidoException::class, UsuarioNoAutenticadoException::class)
fun enviarMensaje(usuario: Usuario?, texto: String): Mensaje {
    if (usuario == null) throw UsuarioNoAutenticadoException()        // regla 1: sesión activa
    if (texto.isBlank()) throw MensajeInvalidoException("El mensaje no puede estar vacío")   // regla 2
    if (texto.length > 2000) throw MensajeInvalidoException("Excede los 2000 caracteres")    // regla 3

    var currentTimeMillis = Clock.System.now().toEpochMilliseconds()

    var idUnico = "m-$currentTimeMillis-${Random.nextInt(1000,9999)}"

    //return Mensaje("m-${System.nanoTime()}", usuario.id, texto, System.currentTimeMillis())  // pasó todo: se crea
    return Mensaje(idUnico, usuario.id,texto,currentTimeMillis)
}

