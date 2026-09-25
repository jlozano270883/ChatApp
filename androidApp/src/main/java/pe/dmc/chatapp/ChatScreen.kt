package pe.dmc.chatapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import pe.dmc.chatapp.shared.*

class Borrador(var texto: String){
    val caracteresRestantes: Int get() = (2000-texto.length).coerceAtLeast(0)
    val estaVacio:Boolean get() = texto.isBlank()
}

@Composable
fun ChatScreen(){
    val scope = rememberCoroutineScope()
    var logs by remember { mutableStateOf(listOf("Presiona un caso de prueba para comenzar..."))}
    var ultimoMensajeLive by remember { mutableStateOf("Conectando flujo en vivo...")}

    LaunchedEffect(Unit) {
        mensajesEnVivo("conv-1").collect { mensaje ->
            ultimoMensajeLive = "[${mensaje.timestamp.aHoraChat()}]${mensaje.autorId}: ${mensaje.texto}"
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ){
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Sesión: ${descripcionDeSesion()}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Live: $ultimoMensajeLive", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Text("Casos de Prueba Disponibles:", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val jorge = Usuario(id = "u1", nombre = "Jorge Lozano", enLinea = false)
                    val antes = jorge.enLinea
                    jorge.marcarConectado()
                    logs = listOf(
                        "Iniciales: ${jorge.iniciales}",
                        "¿En línea antes?: $antes",
                        "¿En línea después?: ${jorge.enLinea}"
                    )
                }
            ){
                Text("1. Usuario", style= MaterialTheme.typography.labelSmall)
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val timestampSimulado = 1710000000000L
                    val conFoto = Usuario(
                        id="u3", nombre = "Marta Rios", enLinea = true,
                        fotoUrl = "https://dmc.pe",
                        ultimaConexion = timestampSimulado - 45_000
                    )
                    logs = listOf(
                        "== Demo NullSafety & Excepciones ==",
                        "Avatar: ${avatarODefault(conFoto)}",
                        "Conexión: ${formatearUltimaConexion(conFoto)}",
                        "",
                        "Intentando enviar borrador vacío..."
                    )
                    try {
                        enviarMensaje(conFoto,"")
                    } catch (e: Exception){
                        logs = logs + "Capturado: ${e.message}"
                    }
                }
            ){
                Text("2. Seguridad", style= MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    val tiempoBaseFijo = 1710000000000L
                    val listaMensajes = (0 until 6).map { i ->
                        Mensaje("m-$i",if(i%2==0)"u1" else "u2","Mensaje $i",tiempoBaseFijo, leido = i % 3 != 0)
                    }
                    val resumen = resumirConversacion(listaMensajes)
                    val noLeidosU2 = mensajesNoLeidosDe(listaMensajes,"u2").size

                    logs = listOf(
                        "== Demo Colecciones y fold ==",
                        "Total analizados: ${listaMensajes.size}",
                        "No leídos de u2: $noLeidosU2",
                        "Resultado Fold -> Total: ${resumen.total}, No leídos: ${resumen.noLeidos}"
                    )
                }
            ){
                Text("3. Filtros/Fold", style= MaterialTheme.typography.labelSmall)
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    scope.launch {
                        logs = listOf("== Demo Coroutines ==", "Cargando del repo...")
                        val iniciales = obtenerMensajesIniciales("conv-1")
                        var resultado = listOf("Mensajes iniciales en caché:")
                        iniciales.forEach { resultado = resultado + " - ${it.texto}" }

                        resultado = resultado + "" + "Tomando 2 ráfagas del Flow ..."
                        val rafaga = mensajesEnVivo("conv-1").take(2).toList()
                        rafaga.forEach { resultado = resultado + " En vivo -> ${it.texto}" }

                        logs = logs + resultado
                    }
                }
            ){
                Text("4. Coroutines", style= MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Resultados de la ejecución (Terminal UI):", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            color= MaterialTheme.colorScheme.inverseOnSurface,
            shape = MaterialTheme.shapes.medium
        ) {
            LazyColumn(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(logs){ log ->
                    Text(
                        text= log,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

}