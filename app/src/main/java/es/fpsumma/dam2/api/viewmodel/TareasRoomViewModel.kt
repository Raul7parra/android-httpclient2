package es.fpsumma.dam2.api.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import es.fpsumma.dam2.api.data.local.AppDatabase
import es.fpsumma.dam2.api.data.local.entity.TareaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TareasRoomViewModel(app: Application) : AndroidViewModel(app), TareasContract {

    private val db = Room.databaseBuilder(
        app, AppDatabase::class.java, "tareas.db"
    ).fallbackToDestructiveMigration(false).build()

    private val dao = db.tareaDao()

    val tareas: StateFlow<List<TareaEntity>> =
        dao.getAllTareas().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _state = MutableStateFlow(TareasUIState(loading = false))
    override val state: StateFlow<TareasUIState> = _state

    init {
        // Conectas el flow de Room con el state UI
        dao.getAllTareas().onEach { lista ->
            _state.value = _state.value.copy(
                tareas = lista.map { Tarea(it.id, it.titulo, it.descripcion) }
            )
        }.launchIn(viewModelScope)
    }

    override fun loadTareas() {
        // Room ya “empuja” cambios por Flow, aquí podría ser no-op
    }

    override fun addTarea(titulo: String, descripcion: String) = viewModelScope.launch {
        dao.insert(TareaEntity(titulo = titulo, descripcion = descripcion))
        _state.value = _state.value.copy(message = "Tarea creada (Room)")
    }

    override fun updateTarea(id: Int, titulo: String, descripcion: String) = viewModelScope.launch {
        dao.update(TareaEntity(id = id, titulo = titulo, descripcion = descripcion))
        _state.value = _state.value.copy(message = "Tarea actualizada (Room)")
    }

    override fun deleteTarea(id: Int) = viewModelScope.launch {
        val tarea = dao.getTareaOnce(id) // o crea un DAO deleteById(id)
        if (tarea != null) {
            dao.delete(tarea)
        }
        _state.value = _state.value.copy(message = "Tarea eliminada (Room)")
    }

    override fun limpiarMensaje() {
        _state.value = _state.value.copy(message = null)
    }
}