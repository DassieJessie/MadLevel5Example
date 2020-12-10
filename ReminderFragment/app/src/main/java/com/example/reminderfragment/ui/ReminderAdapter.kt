package com.example.reminderfragment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderfragment.R
import com.example.reminderfragment.model.Reminder
import kotlinx.android.synthetic.main.item_reminder.view.*

class ReminderAdapter(private val reminders: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(reminder: Reminder) {
            //reminderText = var Reminder.kt
            //tvReminder = text view in item reminder xml
            itemView.tvReminder.text = reminder.reminderText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reminders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(reminders[position])
    }

}