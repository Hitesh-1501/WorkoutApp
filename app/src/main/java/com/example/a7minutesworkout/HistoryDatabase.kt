package com.example.a7minutesworkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {
    // Connects the database to the DAO.
    abstract fun historyDao () : HistoryDao

    companion object{
        /* *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
          */
        @Volatile
        private var INSTANCE : HistoryDatabase? = null

        fun getInstance(context: Context) : HistoryDatabase{
            synchronized(this){
                /* Multiple threads can ask for the database at the same time, ensure we only initialize
                   it once by using synchronized.
                */
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if(instance == null ){
                    instance = Room.databaseBuilder(context.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database",
                        ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}