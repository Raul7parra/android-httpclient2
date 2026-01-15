package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@Composable
fun DetalleTareaRemoteRoute(
    id: Int,
    navController: NavController,
    vm: TareasRemoteViewModel
) {

    LaunchedEffect(id) {
        vm.loadTarea(id)
    }

    val tarea by vm.selected.collectAsStateWithLifecycle()

    DetalleTareaContent(
        tarea = tarea,
        onBack = { navController.popBackStack() },
        onSave = { titulo, descripcion ->
            vm.putTareaById(id, titulo, descripcion)
            navController.popBackStack()
        }
    )
}