package com.example.reminderfragment.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Room creates an SQLite database using all objects annotated with @Entity

@Entity(tableName = "reminderTable") //this is an entity that needs to be stored in a database
data class Reminder (

    @ColumnInfo(name = "reminder") //if you only type @ColumnInfo it takes the variables name as default
    var reminderText: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long? = null
)
