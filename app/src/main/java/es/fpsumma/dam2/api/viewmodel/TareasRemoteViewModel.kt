package es.fpsumma.dam2.api.viewmodel

import android.R.attr.id
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.fpsumma.dam2.api.data.remote.RetrofitClient
import es.fpsumma.dam2.api.data.remote.dto.TareaCreateRequestDTO
import es.fpsumma.dam2.api.data.remote.dto.TareaUpdateRequestDTO
import es.fpsumma.dam2.api.model.Tarea
import es.fpsumma.dam2.api.ui.screen.tareas.TareasUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



/**
 * EL TareasRemoteViewModel obtiene datos desde una API que es el fichero RetrofitClient y
 * expone un estado que la UI puede ver
 */
class TareasRemoteViewModel : ViewModel() {

    //Obtenemos la interfaz con los endpoints como puede ser: GET/POST/PUT/DELETE...
    private val api = RetrofitClient.tareaAPI

    /**
     * Lo mantenemos privado para que SOLO el ViewModel pueda modificarlo.
     */
    private val _state = MutableStateFlow(TareasUIState())

    /**
     * Estado público (solo lectura). La UI se suscribe a este StateFlow:
     * - con collectAsState() en Compose
     * - o con collect en otras capas
     */
    val state: StateFlow<TareasUIState> = _state

    /**
     * Carga el listado de tareas desde la API.
     * - Pone loading=true
     * - Llama al endpoint
     * - Si va bien: convierte DTO -> modelo de app (Tarea) y lo guarda en el estado
     * - Si falla: guarda el error en el estado
     */
    private val _selected = MutableStateFlow<Tarea?>(null)
    val selected: StateFlow<Tarea?> = _selected

    fun loadTarea(id: Int) = viewModelScope.launch {
        runCatching {
            val res = api.listarid(id)
            if (!res.isSuccessful) error("HTTP ${res.code()}")
            res.body() ?: error("Sin body")
        }.onSuccess { dto ->
            _selected.value = Tarea(dto.id, dto.titulo, dto.descripcion)
        }.onFailure { e ->
            _state.update { it.copy(error = e.message ?: "Error cargando detalle") }
        }
    }

    fun deleteTareaById(id: Int) = viewModelScope.launch {
        _state.update { it.copy(loading = true) }

        runCatching {
            // Llamada directa, no devuele nada
            api.eliminar(id)
        }.onSuccess {
            // Si la excepción no ha saltado es que la tarea se ha borrado correctamente
            loadTarea(id)
        }.onFailure { e ->
            // Si falla, se guarda un mensaje y paramos el loading
            _state.update { current ->
                current.copy(
                    error = e.message ?: "No se pudo eliminar la tarea",
                    loading = false
                )
            }
        }
    }

    fun postTarea(titulo: String, descripcion: String) = viewModelScope.launch {
        _state.update { it.copy(loading = true) }

        runCatching {
            val body = TareaCreateRequestDTO(titulo, descripcion)
            api.crear(body)
        }.onSuccess {
            // Si la excepción no ha saltado es que la tarea se ha borrado correctamente
            loadTarea(id)
        }.onFailure { e ->
            // Si falla, se guarda un mensaje y paramos el loading
            _state.update { current ->
                current.copy(
                    error = e.message ?: "No se pudo eliminar la tarea",
                    loading = false
                )
            }
        }
    }

    fun putTareaById(id: Int, titulo: String, descripcion: String) = viewModelScope.launch {
        _state.update { it.copy(loading = true) }

        runCatching {
            val body = TareaUpdateRequestDTO(titulo, descripcion)
            api.actualizar(id, body)
        }.onSuccess {
            // Si la excepción no ha saltado es que la tarea se ha borrado correctamente
            loadTarea(id)
        }.onFailure { e ->
            // Si falla, se guarda un mensaje y paramos el loading
            _state.update { current ->
                current.copy(
                    error = e.message ?: "No se pudo eliminar la tarea",
                    loading = false
                )
            }
        }
    }
}