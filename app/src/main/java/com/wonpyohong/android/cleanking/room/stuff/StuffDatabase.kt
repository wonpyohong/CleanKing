package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.wonpyohong.android.cleanking.CleanApplication.Companion.applicationContext
import java.util.concurrent.Executors

@Database(entities = [Category::class, Stuff::class, StuffHistory::class], version = 2)
abstract class StuffDatabase: RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getStuffDao(): StuffDao
    abstract fun getStuffHistoryDao(): StuffHistoryDao

    companion object {
        private var INSTANCE: StuffDatabase? = null

        fun getInstance(): StuffDatabase {
            if (INSTANCE == null) {
                synchronized(StuffDatabase::class) {
                    INSTANCE = Room.databaseBuilder(applicationContext, StuffDatabase::class.java, "stuff.db")
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
        with (StuffDatabase.getInstance().getCategoryDao()) {
            insert(Category(0, "버리기"))
            insert(Category(0, "집안일"))
            insert(Category(0, "제자리 이동"))
            insert(Category(0, "쓰레기"))
            insert(Category(0, "청소"))
            insert(Category(0, "정리"))
            insert(Category(0, "보관"))
        }
    }
}

object Migration1to2: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DELETE FROM category WHERE categoryId NOT IN (SELECT  MIN(categoryId) FROM category GROUP BY categoryName)")
        database.execSQL("CREATE UNIQUE INDEX index_category_categoryName ON category(categoryName)")

        database.execSQL("DELETE FROM stuff WHERE id NOT IN (SELECT  MIN(id) FROM stuff GROUP BY categoryId, StuffName)")
        database.execSQL("CREATE UNIQUE INDEX index_stuff_categoryId_stuffName ON stuff(categoryId, stuffName)")
    }
}