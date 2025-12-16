package es.fpsumma.dam2.api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import es.fpsumma.dam2.api.ui.navegation.AppNavHost
import es.fpsumma.dam2.api.ui.theme.ActividadconsumiendoapiTheme
import es.fpsumma.dam2.api.viewmodel.TareasViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val tareasViewModel: TareasViewModel =viewModel()
    ActividadconsumiendoapiTheme {
        AppNavHost(navController = navController,tareasViewModel)
    }
}

