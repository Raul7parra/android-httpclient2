package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import es.fpsumma.dam2.api.ui.navegation.Routes
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun NuevaTareaRoomRoute(
    navController: NavController,
    vm: TareasViewModel,
){
    NuevaTareaContent(
        onBack = { navController.popBackStack() },
        onSave = {titulo, description ->
            vm.addTarea(titulo, description)
            navController.navigate("TAREAS_LISTADO")
        }
    )
}