package com.example.todoapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.R.drawable


class ToDoService : Service() {
    private val CHANNEL_ID = "ForegroundService Kotlin"
    lateinit var contentPendingIntent: PendingIntent
    lateinit var actionPendingIntent: PendingIntent

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            when (action) {
                ACTION_START_FOREGROUND_SERVICE -> {
                    startForegroundService()
                    Toast.makeText(
                        this,
                        "Foreground service is started.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                ACTION_STOP_FOREGROUND_SERVICE -> {
                    stopForegroundService()
                    Toast.makeText(
                        this,
                        "Foreground service is stopped.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                ACTION_EXIT -> {
                    stopForegroundService()
                }
                ACTION_PAUSE -> Toast.makeText(
                    this,
                    "You click Pause button.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun startForegroundService() {
        val contentIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.addTaskFragment)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tab to add todo note.")
            .setSmallIcon(drawable.ic_checklist)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)


        // Add Exit button intent in notification.
        val exitIntent = Intent(this, ToDoService::class.java)
        exitIntent.action = ACTION_EXIT
        val pendingExitIntent = PendingIntent.getService(this, 0, exitIntent, 0)
        val exitAction =
            NotificationCompat.Action(android.R.drawable.ic_media_play, "Exit", pendingExitIntent)
        builder.addAction(exitAction)

        // Build the notification.
        val notification = builder.build()
        // Start foreground service.
        startForeground(1, notification)
    }

    /* Used to build and start foreground service. */
    /*fun startForegroundService() {
        val contentIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tab to add todo note.")
            .setSmallIcon(drawable.ic_checklist)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)


        // Add Exit button intent in notification.
        val exitIntent = Intent(this, ToDoService::class.java)
        exitIntent.action = ACTION_EXIT
        val pendingExitIntent = PendingIntent.getService(this, 0, exitIntent, 0)
        val exitAction =
            NotificationCompat.Action(android.R.drawable.ic_media_play, "Exit", pendingExitIntent)
        builder.addAction(exitAction)

        // Build the notification.
        val notification = builder.build()
        // Start foreground service.
        startForeground(1, notification)
    }*/


    fun stopForegroundService() {
        // Stop foreground service and remove the notification.
        val stopIntent = Intent(this, ToDoService::class.java)
        this.stopService(stopIntent)
        // Stop the foreground service.
        stopSelf()
    }

    companion object {
        private const val TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE"
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_EXIT = "ACTION_EXIT"
    }
}