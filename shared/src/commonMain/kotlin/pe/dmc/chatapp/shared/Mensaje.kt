package pe.dmc.chatapp.shared// Taller · Parte A — Data Clases
// =================================
// data class genera automáticamente equals()/hashCode() (compara VALORES,
// no referencias), un toString() legible y copy(). copy() crea un mensaje
// nuevo cambiando solo lo que indiques -- el original nunca se muta.

data class Mensaje(
    val id: String,
    val autorId: String,
    val texto: String,
    val timestamp: Long,
    val leido: Boolean = false,
)