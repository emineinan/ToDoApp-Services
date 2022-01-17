package com.example.todoapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todoapp.Constants.Companion.ACTION_STOP_LISTEN
import com.example.todoapp.Constants.Companion.OPEN_ADD_TODO_TASK_VIEW
import com.example.todoapp.service.ToDoService
import com.example.todoapp.view.ToDoOverlayView

class ToDoReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (OPEN_ADD_TODO_TASK_VIEW == action) {
            val popupWindow = Intent(context, ToDoOverlayView::class.java)
            popupWindow.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(popupWindow)
        } else if (ACTION_STOP_LISTEN == action) {
            context.stopService(Intent(context, ToDoService::class.java))
        }
    }
}