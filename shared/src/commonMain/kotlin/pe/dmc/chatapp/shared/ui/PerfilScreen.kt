package pe.dmc.chatapp.shared.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.dmc.chatapp.shared.*

@Composable
fun PerfilScreen(usuario: Usuario, onVolver: () -> Unit){
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Surface(shape=CircleShape, modifier = Modifier.size(88.dp), color =
            MaterialTheme.colorScheme.primaryContainer){
            Box(contentAlignment = Alignment.Center){
                Text(usuario.iniciales, style = MaterialTheme.typography.headlineMedium)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(usuario.nombre, style = MaterialTheme.typography.titleLarge)
        Text(if (usuario.enLinea) "En línea"
        else "Desconocido",
        style = MaterialTheme.typography.bodyMedium)
        Text("Última conexión: ${formatearUltimaConexion(usuario)}",
            style = MaterialTheme.typography.bodySmall
            )
        Text("Avatar: ${avatarODefault(usuario)}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onVolver){
            Text("Volver al chat")
        }
    }
}