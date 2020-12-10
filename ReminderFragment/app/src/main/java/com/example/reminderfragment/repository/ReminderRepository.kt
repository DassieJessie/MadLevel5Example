package com.example.reminderfragment.repository

import android.content.Context
import com.example.reminderfragment.dao.ReminderDao
import com.example.reminderfragment.database.ReminderRoomDatabase
import com.example.reminderfragment.model.Reminder

/**
 * create a repository class which is responsible for using the DAO to make operations
 * on the database. This prevents us from having to create and initialize the dao objects
 * in the activity classes using the getDatabase method all the time. We just need to
 * create a repository class now.
 */

public class ReminderRepository(context: Context) { //needs a context object because we need this to access the database

    private var reminderDao: ReminderDao

    init{
        val reminderRoomDatabase = ReminderRoomDatabase.getDatabase(context)
        reminderDao = reminderRoomDatabase!!.reminderDao() //constructed using the abstract method we added in the ReminderRoomDatabase class
    }

    suspend fun getAllReminders(): List<Reminder> {
        return reminderDao.getAllReminders()
    }

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }


    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }

}