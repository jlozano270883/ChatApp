package pe.dmc.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pe.dmc.chatapp.shared.Usuario
import pe.dmc.chatapp.shared.ui.ChatScreenCompartido
import pe.dmc.chatapp.shared.ui.ChatAppNavHost

private val usuarioActual = Usuario(id = "yo", nombre = "Jorge Lozano",
    enLinea = true, ultimaConexion = System.currentTimeMillis())
class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            MaterialTheme{
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //ChatScreen()
                    //ChatScreenCompartido(usuarioActual)
                    ChatAppNavHost(usuarioActual = usuarioActual)
                }
            }
        }
    }
}