package escalante.diana.tareasapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("""
        SELECT * FROM tasks
        WHERE titulo LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN :order = 'RECIENTES' THEN creado_en END DESC,
            CASE WHEN :order = 'ANTIGUAS' THEN creado_en END ASC,
            CASE WHEN :order = 'ALFABETICO_AZ' THEN titulo END ASC,
            CASE WHEN :order = 'ALFABETICO_ZA' THEN titulo END DESC
    """)
    fun searchTasks(query: String, order: String): Flow<List<TaskEntity>>
}