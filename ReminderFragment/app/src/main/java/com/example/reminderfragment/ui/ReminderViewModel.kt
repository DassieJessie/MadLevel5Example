package com.example.reminderfragment.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.reminderfragment.model.Reminder
import com.example.reminderfragment.repository.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * separates the concerns of ui logic and business logic from each other
 *
 * We also don’t need the Dispatcher.Main scope anymore because in the ViewModel we don’t have any
 * user interface logic.
 */
class ReminderViewModel(application: Application) :
AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val reminderRepository = ReminderRepository(application.applicationContext)

    val reminders: LiveData<List<Reminder>> = reminderRepository.getAllReminders()

    fun insertReminder(reminder: Reminder){
        ioScope.launch{
            reminderRepository.insertReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder){
        ioScope.launch {
            reminderRepository.deleteReminder(reminder)
        }
    }
}