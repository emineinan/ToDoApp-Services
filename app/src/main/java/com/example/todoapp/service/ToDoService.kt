package com.example.todoapp.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.todoapp.Constants.Companion.CODE_EXIT_INTENT
import com.example.todoapp.Constants.Companion.CODE_FOREGROUND_SERVICE
import com.example.todoapp.Constants.Companion.CODE_NOTE_INTENT
import com.example.todoapp.Constants.Companion.INTENT_COMMAND_EXIT
import com.example.todoapp.Constants.Companion.INTENT_COMMAND_NOTE
import com.example.todoapp.Constants.Companion.NOTIFICATION_CHANNEL_GENERAL
import com.example.todoapp.R
import com.example.todoapp.util.drawOverOtherAppsEnabled
import com.example.todoapp.util.startPermissionActivity
import com.example.todoapp.view.ToDoOverlayView

class ToDoService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    //Remove the foreground notification and stop the service.
    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    //Create and show the foreground notification.
    @SuppressLint("LaunchActivityFromNotification")
    private fun showNotification() {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val exitIntent = Intent(this, ToDoService::class.java)
        exitIntent.action = INTENT_COMMAND_EXIT

        val noteIntent = Intent(this, ToDoService::class.java)
        noteIntent.action = INTENT_COMMAND_NOTE

        val exitPendingIntent = PendingIntent.getService(
            this, CODE_EXIT_INTENT, exitIntent, 0
        )

        val notePendingIntent = PendingIntent.getService(
            this, CODE_NOTE_INTENT, noteIntent, 0
        )

        // From Android O, it's necessary to create a notification channel first.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                with(
                    NotificationChannel(
                        NOTIFICATION_CHANNEL_GENERAL,
                        getString(R.string.notification_channel_general),
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                ) {
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    manager.createNotificationChannel(this)
                }
        }

        with(
            NotificationCompat.Builder(
                this,
                NOTIFICATION_CHANNEL_GENERAL
            )
        ) {
            setContentTitle(getString(R.string.app_name))
            setContentText(getString(R.string.todo_app_notification_text))
            setAutoCancel(false)
            setOngoing(true)
            setWhen(System.currentTimeMillis())
            setSmallIcon(R.drawable.ic_checklist)
            priority = Notification.PRIORITY_HIGH
            setContentIntent(notePendingIntent)
            addAction(
                NotificationCompat.Action(
                    0,
                    getString(R.string.exit),
                    exitPendingIntent
                )
            )
            startForeground(CODE_FOREGROUND_SERVICE, build())
        }
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val command = intent.action

        // Exit the service if we receive the EXIT command.
        // START_NOT_STICKY is important here, we don't want
        // the service to be relaunched.
        if (command == INTENT_COMMAND_EXIT) {
            stopService()
            return START_NOT_STICKY
        }

        // Be sure to show the notification first for all commands.
        // Don't worry, repeated calls have no effects.
        showNotification()

        // Show the floating window for adding a new note.
        if (command == INTENT_COMMAND_NOTE) {
            if (!drawOverOtherAppsEnabled()) {
                startPermissionActivity()
            } else {
                val toDoOverlayView = ToDoOverlayView(applicationContext)
                toDoOverlayView.openOverlay()
            }
        }
        return START_STICKY
    }
}
