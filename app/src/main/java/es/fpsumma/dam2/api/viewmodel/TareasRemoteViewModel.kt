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

    fun loadTareas() = viewModelScope.launch {
        // 1) Antes de llamar a la red, avisamos a la UI de que estamos cargando.
        //    También limpiamos errores previos.
        _state.update { current ->
            current.copy(loading = true, error = null)
        }

        /**
         * runCatching hace de "try/catch" funcional:
         * - Lo que devuelva el bloque irá a onSuccess(...)
         * - Si el bloque lanza una excepción, irá a onFailure(...)
         */
        runCatching {
            // 2) Llamada HTTP (suspend) al endpoint: GET /api/tareas
            val res = api.listar()

            // 3) Si el HTTP no es 2xx, lo convertimos en excepción para ir a onFailure(...)
            if (!res.isSuccessful) error("HTTP ${res.code()}")

            // 4) Si el body es null, devolvemos lista vacía (para evitar NPE)
            res.body() ?: emptyList()
        }.onSuccess { listaDto ->
            // 5) Si todo salió bien, aquí tenemos la lista de DTOs que viene de la API.
            //    Convertimos DTO -> Modelo de la app.
            //    (La UI trabaja con Tarea, no con TareaDTO)
            val tareas = listaDto.map { dto ->
                Tarea(
                    id = dto.id,
                    titulo = dto.titulo,
                    descripcion = dto.descripcion
                )
            }

            // 6) Actualizamos el estado:
            //    - tareas con la lista convertida
            //    - loading=false porque ya acabó la carga
            _state.update { current ->
                current.copy(tareas = tareas, loading = false)
            }
        }.onFailure { e ->
            // 7) Si ha fallado (sin internet, timeout, 500, etc.), guardamos un mensaje de error
            //    y paramos el loading.
            _state.update { current ->
                current.copy(
                    error = e.message ?: "Error cargando tareas",
                    loading = false
                )
            }
        }
    }

    fun loadTarea(id: Int) = viewModelScope.launch {
        runCatching {
            val res = api.listarid(id)

            // Verificamos que la respuesta sea exitosa
            if (!res.isSuccessful) error("HTTP ${res.code()}")

            // Extraemos el cuerpo o lanzamos error si es nulo
            res.body() ?: error("Sin body")
        }.onSuccess { dto ->
            // Convertimos el TareaDTO recibido en nuestro modelo Tarea y lo guardamos
            _selected.value = Tarea(
                id = dto.id,
                titulo = dto.titulo,
                descripcion = dto.descripcion
            )
        }.onFailure { e ->
            // Si falla, se guarda un mensaje y paramos el loading
            _state.update { it.copy(
                error = e.message ?: "Error cargando detalle",
                loading = false
            ) }
        }
    }

    fun deleteTareaById(id: Int) = viewModelScope.launch {
        _state.update { it.copy(loading = true) }

        runCatching {
            // Llamada directa, no devuele nada
            api.eliminar(id)
        }.onSuccess {
            // Si la excepción no ha saltado es que la tarea se ha borrado correctamente
            loadTareas()
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
            loadTareas()
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
            loadTareas()
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