import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import es.fpsumma.dam2.api.ui.screen.tareas.NuevaTareaContent
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun NuevaTareaRemoteRoute(
    navController: NavController,
    vm: TareasRemoteViewModel,
){
    NuevaTareaContent(
        onBack = { navController.popBackStack() },
        onSave = {titulo, descripcion ->
            vm.postTarea(titulo, descripcion)
            navController.navigate("TAREAS_LISTADO")
        }
    )
}