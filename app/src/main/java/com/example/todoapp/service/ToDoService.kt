package com.example.todoapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.todoapp.BuildConfig

class ToDoService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        createIntents()
        createNotification()
    }

    private fun createIntents() {
        TODO("Not yet implemented")
    }

    private fun createNotification() {
        TODO("Not yet implemented")
    }
}