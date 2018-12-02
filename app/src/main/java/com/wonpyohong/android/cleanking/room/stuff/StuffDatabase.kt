package com.wonpyohong.android.cleanking.room.stuff

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.wonpyohong.android.cleanking.CleanApplication.Companion.applicationContext
import java.util.concurrent.Executors

@Database(entities = [Category::class, Stuff::class, StuffHistory::class], version = 3)
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
                        .addMigrations(Migration1to2, Migration2to3)
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

object Migration2to3: Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'stuff' ADD frequency INTEGER DEFAULT 0 NOT NULL")

        val cursor = database.query("SELECT A.categoryId, B.id, B.stuffName, COUNT(*) frequency_alias\n" +
                "FROM 'category' A \n" +
                "INNER JOIN 'stuff' B \n" +
                "ON A.categoryId = B.categoryId \n" +
                "INNER JOIN 'stuff_history' C\n" +
                "ON A.categoryName = C.categoryName AND B.stuffName = C.stuffName\n" +
                "GROUP BY A.categoryName, B.stuffName")

        val stuffList = mutableListOf<Stuff>()
        while (cursor.moveToNext()) {
            val categoryId = cursor.getInt(0)
            val stuffId = cursor.getInt(1)
            val stuffName = cursor.getString(2)
            val frequency = cursor.getInt(3)

            stuffList.add(Stuff(stuffId, categoryId, stuffName, frequency))
        }

        Executors.newSingleThreadScheduledExecutor().execute {
            StuffDatabase.getInstance().getStuffDao().update(*stuffList.toTypedArray())
        }
    }
}