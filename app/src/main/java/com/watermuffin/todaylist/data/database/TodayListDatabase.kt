package com.watermuffin.todaylist.data.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.watermuffin.todaylist.data.database.dao.TextNoteDao
import com.watermuffin.todaylist.data.database.dao.UserDao
import com.watermuffin.todaylist.data.database.entities.TextNoteEntity
import com.watermuffin.todaylist.data.database.entities.UserEntity

@Database(
    entities = [
        TextNoteEntity::class,
        UserEntity::class,
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TodayListDatabase : RoomDatabase() {
    abstract fun textNoteDao(): TextNoteDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TodayListDatabase? = null

        fun getDatabase(context: Context): TodayListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodayListDatabase::class.java,
                    "todaylist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}