package com.example.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var viewType: Int,
    var headerTitle: String,
    var isActive: Boolean,
    var title: String,
    var description: String
)