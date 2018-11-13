package com.homedev.android.dietapp.room.exercise

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.wonpyohong.android.cleanking.CleanApplication.Companion.applicationContext

@Database(entities = arrayOf(Dump::class), version = 1)
abstract class DumpDatabase: RoomDatabase() {
    abstract fun getDumpDao(): DumpDao

    companion object {
        private var INSTANCE: DumpDatabase? = null

        fun getInstance(): DumpDatabase {
            if (INSTANCE == null) {
                synchronized(DumpDatabase::class) {
                    INSTANCE = Room.databaseBuilder(applicationContext, DumpDatabase::class.java, "dump.db")
                            .addMigrations()
                                .build()
                }
            }

            return INSTANCE!!
        }
    }
}

//object MIGRATION_1_2: Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("ALTER TABLE exercise_spec RENAME COLUMN id TO exerciseId")
//    }
//
//}