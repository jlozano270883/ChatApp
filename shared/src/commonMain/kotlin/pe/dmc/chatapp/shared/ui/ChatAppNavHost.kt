package pe.dmc.chatapp.shared.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.dmc.chatapp.shared.Usuario

@Composable
fun ChatAppNavHost(usuarioActual: Usuario){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "chat"){
        composable("chat"){
            ChatScreenCompartido(
                usuarioActual = usuarioActual,
                onAbrirPerfil = { navController.navigate("perfil")}
            )
        }
        composable("perfil"){
            PerfilScreen(
                usuario = usuarioActual,
                onVolver = { navController.popBackStack()}
            )
        }
    }
}

