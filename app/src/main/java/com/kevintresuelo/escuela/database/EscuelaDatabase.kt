package com.kevintresuelo.escuela.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kevintresuelo.escuela.database.daos.AcademicTermDao
import com.kevintresuelo.escuela.database.entities.AcademicTerm

@Database(entities = [
    AcademicTerm::class
],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class EscuelaDatabase : RoomDatabase() {

    abstract val academicTermDao: AcademicTermDao

    companion object {
        @Volatile
        private var INSTANCE: EscuelaDatabase? = null

        fun getInstance(context: Context): EscuelaDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EscuelaDatabase::class.java,
                        "escuela_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}