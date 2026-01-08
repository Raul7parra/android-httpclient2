package es.fpsumma.dam2.api.ui.screen.tareas

import es.fpsumma.dam2.api.model.Tarea

data class TareasUIState(
    val tareas: List<Tarea> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
