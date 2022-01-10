package com.example.todoapp.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todoapp.MainActivity
import com.example.todoapp.R

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        FLAG_UPDATE_CURRENT
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

    //call notify to send the notification
    notify(NOTIFICATION_ID, builder.build())
}