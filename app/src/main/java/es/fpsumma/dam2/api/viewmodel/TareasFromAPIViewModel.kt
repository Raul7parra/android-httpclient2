package es.fpsumma.dam2.api.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.fpsumma.dam2.api.data.remote.RetrofitClient
import es.fpsumma.dam2.api.data.remote.dto.TareaCreateRequest
import es.fpsumma.dam2.api.data.remote.dto.TareaDTO
import es.fpsumma.dam2.api.data.remote.dto.TareaUpdateRequest
import es.fpsumma.dam2.api.model.Tarea
import kotlinx.coroutines.launch

class TareasViewModel : ViewModel() {
    private val api = RetrofitClient.tareaApi

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    // (Opcional) mensaje “no-error” para snackbars/toasts
    var message by mutableStateOf<String?>(null)
        private set

    var tareas by mutableStateOf<List<TareaDTO>>(emptyList())
        private set

    fun loadTareas() = viewModelScope.launch {
        loading = true
        error = null
        runCatching {
            val res = api.listar()
            if (!res.isSuccessful) error("HTTP ${res.code()}")
            res.body() ?: emptyList()
        }.onSuccess {
            tareas = it
        }.onFailure {
            error = it.message
        }
        loading = false
    }

    fun addTarea(titulo: String, descripcion: String) {
        viewModelScope.launch {
            loading = true
            error = null
            message = null
            runCatching {
                val res = api.crear(TareaCreateRequest(titulo = titulo, descripcion = descripcion))
                if (!res.isSuccessful) error("HTTP ${res.code()}: ${res.errorBody()?.string()}")
            }.onSuccess {
                message = "Tarea creada"
                loadTareas() // refresca lista
            }.onFailure { e ->
                error = e.message ?: "Error creando tarea"
                loading = false
            }
        }
    }

    fun updateTarea(id: Int, titulo: String, descripcion: String) {
        viewModelScope.launch {
            loading = true
            error = null
            message = null

            runCatching {
                val res = api.actualizar(id, TareaUpdateRequest(titulo = titulo, descripcion = descripcion))
                if (!res.isSuccessful) error("HTTP ${res.code()}: ${res.errorBody()?.string()}")
            }.onSuccess {
                message = "Tarea actualizada"
                loadTareas()
            }.onFailure { e ->
                error = e.message ?: "Error actualizando tarea"
                loading = false
            }
        }
    }

    fun deleteTarea(id: Int) {
        viewModelScope.launch {
            loading = true
            error = null
            message = null

            runCatching {
                val res = api.eliminar(id)
                if (!res.isSuccessful) error("HTTP ${res.code()}: ${res.errorBody()?.string()}")
            }.onSuccess {
                message = "Tarea eliminada"
                loadTareas()
            }.onFailure { e ->
                error = e.message ?: "Error eliminando tarea"
                loading = false
            }
        }
    }

    fun limpiarMensaje() {
        message = null
    }




}