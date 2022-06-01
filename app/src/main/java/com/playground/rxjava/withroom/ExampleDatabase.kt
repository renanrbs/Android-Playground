package com.playground.rxjava.withroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExampleEntity::class], version = 1)
abstract class ExampleDatabase : RoomDatabase() {
    abstract fun exampleEntityDao(): ExampleEntityDao

    companion object {
        @Volatile
        private var instance: ExampleDatabase? = null

        fun getDatabase(context: Context): ExampleDatabase {
            return instance ?: synchronized(this) {
                val dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ExampleDatabase::class.java,
                    "rx_with_room_database"
                ).build()
                instance = dbInstance

                dbInstance
            }
        }
    }
}