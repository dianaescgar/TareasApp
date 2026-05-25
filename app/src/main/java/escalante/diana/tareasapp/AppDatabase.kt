package escalante.diana.tareasapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(
                this
            ) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "tasks_db"
                    )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insertamos las tareas iniciales en un
                            // hilo separado. NUNCA en el main thread.
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).taskDao()
                                TAREAS_INICIALES.forEach { tarea ->
                                    dao.insert(tarea)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Tareas que se cargan la primera vez que se instala la app.
        private val TAREAS_INICIALES = listOf(
            TaskEntity(
                titulo = "Redesign Logo",
                completado = true
            ),
            TaskEntity(
                titulo = "Create sharing interface",
                completado = true
            ),
            TaskEntity(
                titulo = "Enable sharing functionality",
                completado = true
            ),
            TaskEntity(
                titulo = "Remove shared access",
                completado = true
            ),
            TaskEntity(
                titulo = "Add shared toast",
                completado = true
            ),
            TaskEntity(
                titulo = "Allow administrators to delete any visualization when necessary",
                completado = true
            ),
            TaskEntity(
                titulo = "Allow current users to hide any shared visualization",
                completado = true
            ),
            TaskEntity(
                titulo = "Create team list visualization layout",
                completado = true
            ),
            TaskEntity(
                titulo = "Create toolbar",
                completado = true
            ),
            TaskEntity(
                titulo = "Add Teams header",
                completado = true
            ),
            TaskEntity(
                titulo = "Add 'My teams' list",
                completado = true
            ),
            TaskEntity(
                titulo = "Add swipe to delete or edit to 'My teams' list",
                completado = true
            ),
            TaskEntity(
                titulo = "Add 'Teams I'm in' list",
                completado = true
            ),
            TaskEntity(
                titulo = "Add toggle action to show team members to 'My teams' list",
                completado = true
            ),
            TaskEntity(
                titulo = "Create edit your team sheet layout",
                completado = false
            ),
            TaskEntity(
                titulo = "Enable editing team functionality",
                completado = false
            )
        )
    }
}