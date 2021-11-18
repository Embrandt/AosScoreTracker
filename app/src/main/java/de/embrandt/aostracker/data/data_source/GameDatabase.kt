package de.embrandt.aostracker.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.embrandt.aostracker.domain.model.GameData
import de.embrandt.aostracker.domain.util.Converters

@TypeConverters(Converters::class)
@Database(entities = [GameData::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract val gameDataDao : GameDataDao
    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null
        fun getInstance(context: Context): GameDatabase {
            synchronized(this) {}
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "aos_tracker_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }
            return instance
        }

    }
}