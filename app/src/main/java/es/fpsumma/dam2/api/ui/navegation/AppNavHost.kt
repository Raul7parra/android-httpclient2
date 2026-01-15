package es.fpsumma.dam2.api.ui.navegation

import NuevaTareaRemoteRoute
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.fpsumma.dam2.api.ui.screen.tareas.DetalleTareaRemoteRoute
import es.fpsumma.dam2.api.ui.screen.tareas.DetalleTareaRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasRemoteRoute
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.NuevaTareaRoomRoute
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    tareasViewModel: TareasViewModel,
    tareasRemoteViewModel: TareasRemoteViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TAREA_LISTADO_API
    ) {
        composable(Routes.TAREA_LISTADO_API) {
            ListadoTareasRemoteRoute(navController, tareasRemoteViewModel)
        }

        composable(Routes.TAREA_ADD) {
            NuevaTareaRemoteRoute(navController, tareasRemoteViewModel)
        }

        composable(
            route = Routes.TAREA_VIEW,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            DetalleTareaRemoteRoute(
                id = it.arguments?.getInt("id") ?: 0,
                navController = navController,
                vm = tareasRemoteViewModel
            )
        }
    }
}