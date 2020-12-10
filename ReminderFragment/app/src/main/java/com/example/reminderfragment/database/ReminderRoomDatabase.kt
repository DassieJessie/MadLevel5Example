package com.example.reminderfragment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reminderfragment.model.Reminder
import com.example.reminderfragment.dao.ReminderDao

@Database(entities = [Reminder::class], version = 1, exportSchema = false) // tells the class this is a room database
abstract class ReminderRoomDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object{
        private const val DATABASE_NAME = "REMINDER_DATABASE"

        @Volatile
        private var reminderRoomDatabaseInstance: ReminderRoomDatabase? = null

        fun getDatabase(context: Context): ReminderRoomDatabase? { //Because we want the database to be static we encapsulate the getDatabase function within a companion object.

            if (reminderRoomDatabaseInstance == null) {

                //build database
                synchronized(ReminderRoomDatabase::class.java) {
                    if (reminderRoomDatabaseInstance == null) {
                        reminderRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            ReminderRoomDatabase::class.java,
                            DATABASE_NAME
                        )
                            /**
                             * When a query is performed on the main thread then the user interface
                             * will stop working until the query is finished. In other words the screen
                             * will freeze for 3 seconds if the query takes 3 seconds. Enabling this
                             * was only meant for purposes of going through the basics of Room in the
                             * previous steps of this tutorial. Never allow this in a finished app.
                             */
                            //.allowMainThreadQueries()
                            .build()
                    }
                }

            }
            return reminderRoomDatabaseInstance //get database
        }

    }

}