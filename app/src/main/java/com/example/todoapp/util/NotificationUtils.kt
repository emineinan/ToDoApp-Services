package com.example.todoapp.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.receiver.ToDoReceiver

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        FLAG_UPDATE_CURRENT
    )

    val exitIntent = Intent(applicationContext, ToDoReceiver::class.java)
    exitIntent.putExtra("notificationId", NOTIFICATION_ID)
    val exitPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        exitIntent,
        FLAG_CANCEL_CURRENT
    )

    val builder = NotificationCompat.Builder(  // get an instance of NotificationCompat.Builder
        applicationContext,
        applicationContext.getString(R.string.todo_notification_channel_id)
    )
        //set title, text and icon to builder
        .setSmallIcon(R.drawable.ic_checklist)
        //.setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        .addAction(
            R.drawable.ic_check,
            applicationContext.getString(R.string.exit),
            exitPendingIntent
        )


    //call notify to send the notification
    notify(NOTIFICATION_ID, builder.build())
}