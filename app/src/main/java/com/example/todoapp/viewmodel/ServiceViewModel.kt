package com.example.todoapp.viewmodel

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.example.todoapp.R
import com.example.todoapp.util.sendNotification

class ServiceViewModel(private val app: Application) : AndroidViewModel(app) {

    //get an instance of NotificationManager and call sendNotification
    fun startToDoApp() {
        val notificationManager = ContextCompat.getSystemService(
            app,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(app.getString(R.string.todo_task_ready), app)
    }
}