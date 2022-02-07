package com.example.todoapp.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.example.todoapp.PermissionActivity
import com.example.todoapp.service.ToDoService

fun Context.startToDoService(command: String = "") {
    val intent = Intent(this, ToDoService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.startForegroundService(intent)
        Toast.makeText(this, "Service started.", Toast.LENGTH_SHORT).show()
    } else {
        this.startService(intent)
        Toast.makeText(this, "Service stopped.", Toast.LENGTH_SHORT).show()
    }
}

fun Context.stopToDoService() {
    val intent = Intent(this, ToDoService::class.java)
    this.stopService(intent)
    Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show()
}

fun Context.drawOverOtherAppsEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        true
    } else {
        Settings.canDrawOverlays(this)
    }
}


fun Context.startPermissionActivity() {
    startActivity(
        Intent(this, PermissionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    )
}