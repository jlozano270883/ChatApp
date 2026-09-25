package pe.dmc.chatapp.shared

expect fun idDeDispositivo(): String

expect fun nombrePlataforma(): String

fun descripcionDeSesion(): String =
    "Conectado desde ${nombrePlataforma()} (id: ${idDeDispositivo()})"