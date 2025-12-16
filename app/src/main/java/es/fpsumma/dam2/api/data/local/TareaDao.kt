package es.fpsumma.dam2.api.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.fpsumma.dam2.api.data.local.entity.TareaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tarea: TareaEntity)

    @Update
    suspend fun update(tarea: TareaEntity)

    @Query("DELETE FROM tareas WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * from tareas WHERE id = :id")
    fun getTarea(id: Int): Flow<TareaEntity>

    @Query("SELECT * from tareas ORDER BY titulo ASC")
    fun getAllTareas(): Flow<List<TareaEntity>>
}