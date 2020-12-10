package com.example.reminderfragment.dao

import androidx.room.*
import com.example.reminderfragment.model.Reminder

//To get access to the Room database we are using a DAO(Data Access Object)
//A Dao can either be an interface or an abstract class

@Dao
interface ReminderDao {

    /**
     * By adding the suspend keyword to the method we have specified that this  method cannot
     * be called without using Coroutines.
     *
     * (to ensure that they are not executed on the main thread)
     *
     * The biggest difference is that coroutines are very cheap, almost free: we can create thousands
     * of them, and pay very little in terms of performance. True threads, on the other hand, are
     * expensive to start and keep around
     */
    @Query("SELECT * FROM reminderTable") //retrieve all reminders from the database
    suspend fun getAllReminders(): List<Reminder>

    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)
}