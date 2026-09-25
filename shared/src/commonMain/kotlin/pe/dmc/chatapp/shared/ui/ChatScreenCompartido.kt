package pe.dmc.chatapp.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.dmc.chatapp.shared.*

@Composable
fun ChatScreenCompartido(usuarioActual: Usuario, onAbrirPerfil:() -> Unit = {}){
    val mensajes = remember { mutableStateListOf<Mensaje>() }
    val repositorio = remember { ChatRepository() }
    val borrador = remember { Borrador("") }
    var textoInput by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        repositorio.onNuevoMensaje { mensaje -> mensajes.add(mensaje) }
        obtenerMensajesIniciales("conv-1").forEach { repositorio.recibir(it) }
    }

    LaunchedEffect(Unit){
        mensajesEnVivo("conv-1").collect { mensaje -> repositorio.recibir(mensaje) }
    }

    LaunchedEffect(mensajes.size){
        if (mensajes.isNotEmpty()){
            scope.launch { listState.animateScrollToItem(mensajes.size - 1) }
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Column {
                Text("Sesión desde: ${descripcionDeSesion()}", style = MaterialTheme.typography.labelMedium)
                Text(
                    "${usuarioActual.iniciales} - último acceso: ${formatearUltimaConexion(usuarioActual)}",
                    style = MaterialTheme.typography.labelSmall
                )
                TextButton(onClick = onAbrirPerfil) { Text("Perfil")}
            }
        }

        Spacer(Modifier.height(8.dp))
        LazyColumn(state=listState, modifier = Modifier.weight(1f), verticalArrangement =
            Arrangement.spacedBy(6.dp)){
            items(mensajes, key = {it.id}){ mensaje ->
                BurbujaMensaje(mensaje, esMio = mensaje.esDe(usuarioActual.id))
            }
        }

        error?.let { Text(it, color = MaterialTheme.colorScheme.error)}

        Row(Modifier.fillMaxWidth().padding(top=8.dp), verticalAlignment = Alignment.CenterVertically){
            OutlinedTextField(
                value = textoInput,
                onValueChange = { borrador.texto = it; textoInput = borrador.texto},
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...")},
                supportingText = { Text("${borrador.caracteresRestantes} caracteres restantes")},
            )

            Spacer(Modifier.width(8.dp))

            Button(
                enabled = !borrador.estaVacio,
                onClick = {
                    try {
                        val mensaje = enviarMensaje(usuarioActual, borrador.texto)
                        repositorio.recibir(mensaje)
                        borrador.texto=""; textoInput= ""; error=null
                    } catch (e: MensajeInvalidoException){
                        error = e.message
                    } catch (e: UsuarioNoAutenticadoException){
                        error = e.message
                    }
                },
            ){ Text("Enviar")}
        }
    }
}

@Composable
private fun BurbujaMensaje(mensaje: Mensaje, esMio: Boolean){
    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (esMio) Arrangement.End else Arrangement.Start) {
        Column(
            Modifier.clip(RoundedCornerShape(12.dp))
                .background(if (esMio) MaterialTheme.colorScheme.primaryContainer else
                    MaterialTheme.colorScheme.surfaceVariant)
                .padding(10.dp)
        ) {
            Text(mensaje.autorId, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Text(mensaje.texto)
            Text(mensaje.timestamp.aHoraChat(), style = MaterialTheme.typography.labelSmall)
        }
    }
}
