package com.homedev.android.dietapp.room.exercise

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.wonpyohong.android.cleanking.CleanApplication.Companion.applicationContext
import com.wonpyohong.android.cleanking.room.category.Category
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

@Database(entities = arrayOf(Category::class), version = 1)
abstract class CategoryDatabase: RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao

    companion object {
        private var INSTANCE: CategoryDatabase? = null

        fun getInstance(): CategoryDatabase {
            if (INSTANCE == null) {
                synchronized(CategoryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(applicationContext, CategoryDatabase::class.java, "category.db")
                        .addCallback(RoomDbCallback)
                            .addMigrations()
                                .build()
                }
            }

            return INSTANCE!!
        }
    }
}

object RoomDbCallback: RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        Executors.newSingleThreadScheduledExecutor().execute {
            val categoryDao = CategoryDatabase.getInstance().getCategoryDao()
            categoryDao.insert(Category(0, "버리기"))
            categoryDao.insert(Category(0, "집안일"))
            categoryDao.insert(Category(0, "제자리 이동"))
            categoryDao.insert(Category(0, "쓰레기"))
            categoryDao.insert(Category(0, "청소"))
            categoryDao.insert(Category(0, "정리"))
            categoryDao.insert(Category(0, "보관"))
        }
    }
}

//object MIGRATION_1_2: Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("ALTER TABLE exercise_spec RENAME COLUMN id TO exerciseId")
//    }
//
//}