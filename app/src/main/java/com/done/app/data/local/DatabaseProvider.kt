package com.done.app.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var INSTANCE: DoneDatabase? = null

    fun getDatabase(
        context: Context
    ): DoneDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    DoneDatabase::class.java,
                    "done_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()

            INSTANCE = instance

            instance

        }
    }


}
