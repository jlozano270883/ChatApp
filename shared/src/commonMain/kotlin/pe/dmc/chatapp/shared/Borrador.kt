package pe.dmc.chatapp.shared
class Borrador(textoInicial: String) {
    var texto: String = textoInicial
        set(value) { field = value.take(500) }

    val caracteresRestantes: Int
        get() = 500 - texto.length

    val estaVacio: Boolean
        get() = texto.isBlank()
}
