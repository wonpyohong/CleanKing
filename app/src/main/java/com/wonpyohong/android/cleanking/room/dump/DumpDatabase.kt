package com.homedev.android.dietapp.room.exercise

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.wonpyohong.android.cleanking.CleanApplication.Companion.applicationContext
import com.wonpyohong.android.cleanking.room.category.Category
import com.wonpyohong.android.cleanking.room.dump.Dump
import java.util.concurrent.Executors

@Database(entities = [Category::class, Dump::class], version = 2)
abstract class DumpDatabase: RoomDatabase() {
    abstract fun getDumpDao(): DumpDao
    abstract fun getCategoryDao(): CategoryDao

    companion object {
        private var INSTANCE: DumpDatabase? = null

        fun getInstance(): DumpDatabase {
            if (INSTANCE == null) {
                synchronized(DumpDatabase::class) {
                    INSTANCE = Room.databaseBuilder(applicationContext, DumpDatabase::class.java, "dump.db")
                        .addCallback(RoomDbCallback)
                        .addMigrations(Migration1to2)
                        .build()
                }
            }

            return INSTANCE!!
        }
    }
}

object RoomDbCallback: RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        addBaseData()
    }
}

private fun addBaseData() {
    Executors.newSingleThreadScheduledExecutor().execute {
        val categoryDao = DumpDatabase.getInstance().getCategoryDao()
        categoryDao.insert(Category(0, "버리기"))
        categoryDao.insert(Category(0, "집안일"))
        categoryDao.insert(Category(0, "제자리 이동"))
        categoryDao.insert(Category(0, "쓰레기"))
        categoryDao.insert(Category(0, "청소"))
        categoryDao.insert(Category(0, "정리"))
        categoryDao.insert(Category(0, "보관"))
    }
}

object Migration1to2: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

    }
}