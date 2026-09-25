package pe.dmc.chatapp.notificaciones

expect class ProgramadorDeRecordatorios(){
    fun programar(mensaje: String, segundosDesdeAhora: Int)
}