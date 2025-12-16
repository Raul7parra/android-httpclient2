package es.fpsumma.dam2.api.viewmodel

import kotlinx.coroutines.flow.StateFlow

data class TareasUIState(
    val loading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val tareas: List<Tarea> = emptyList()
)

data class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String
)

interface TareasContract {
    val state: StateFlow<TareasUIState>

    fun loadTareas()
    fun addTarea(titulo: String, descripcion: String)
    fun updateTarea(id: Int, titulo: String, descripcion: String)
    fun deleteTarea(id: Int)
    fun limpiarMensaje()
}