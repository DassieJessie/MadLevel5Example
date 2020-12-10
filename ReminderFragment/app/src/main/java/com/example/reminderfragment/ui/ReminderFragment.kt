package com.example.reminderfragment.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderfragment.R
import com.example.reminderfragment.model.Reminder
import com.example.reminderfragment.repository.ReminderRepository
import kotlinx.android.synthetic.main.fragment_reminder.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReminderFragment : Fragment() {

    //database repo
    private lateinit var reminderRepository: ReminderRepository

    private val reminders = arrayListOf<Reminder>()
    private val reminderAdapter =
        ReminderAdapter(reminders)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeAddReminderResult()


        reminderRepository =
            ReminderRepository(
                requireContext()
            )
        getRemindersFromDatabase()

    }

    private fun getRemindersFromDatabase() {
        /**
         * In Kotlin, all coroutines must run in a dispatcher — even when they’re running on the main thread
         *
         * Kotlin provides three Dispatchers you can use. The dispatchers being:
         * Dispatchers.Main: Main thread on Android, interact with the UI and perform light work.
         * Dispatchers.IO: Optimized for disk and network IO.
         * Dispatchers.Default: Optimized for CPU intensive work.
         *
         * - For updating the user interface we will be using the Main dispatcher.
         * - doing database operations we are going to be needing the IO dispatcher
         *
         * The reason why we have to start all the Coroutines inside a Main dispatcher is because it’s not
         * possible to modify the user interface within an IO thread.
         */
        CoroutineScope(Dispatchers.Main).launch {
            //get all the reminders from the database
            val reminders = withContext(Dispatchers.IO) {
                reminderRepository.getAllReminders()
            }
            //clears current reminders list
            this@ReminderFragment.reminders.clear()
            //adds all the reminders from the database to the list
            this@ReminderFragment.reminders.addAll(reminders)


            reminderAdapter.notifyDataSetChanged()
        }

//        //get all the reminders from the database
//        val reminders = reminderRepository.getAllReminders()
//        //clears current reminders list
//        this.reminders.clear()
//        //adds all the reminders from the database to the list
//        this.reminders.addAll(reminders)
//
//
//        reminderAdapter.notifyDataSetChanged()
    }


    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvReminders.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReminders.adapter = reminderAdapter
        rvReminders.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))

        createItemTouchHelper().attachToRecyclerView(rvReminders)
    }

    private fun observeAddReminderResult() {
        setFragmentResultListener(REQ_REMINDER_KEY) { key, bundle ->
            bundle.getString(BUNDLE_REMINDER_KEY)?.let {
                val reminder = Reminder(it)

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        reminderRepository.insertReminder(reminder)
                    }
                    //Refreshes/ updates rv from database
                    getRemindersFromDatabase()
                }

//                reminderRepository.insertReminder(reminder)
//                Refreshes rv from database
//                getRemindersFromDatabase()

            } ?: Log.e("ReminderFragment", "Request triggered, but empty reminder text!")

        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val reminderToDelete = reminders[position]

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        reminderRepository.deleteReminder(reminderToDelete)
                    }
                    //Refreshes/update rv from database
                    getRemindersFromDatabase()
                }


//                val reminderToDelete = reminders[position]
//                reminderRepository.deleteReminder(reminderToDelete)
//                //Refreshes/update rv from database
//                getRemindersFromDatabase()

            }
        }
        
        return ItemTouchHelper(callback)
    }




}