package com.example.myapplicationmad

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null) {
            createNotificationChannel(context)
        }


        val notificationBuilder = NotificationCompat.Builder(context!!, "med_channel")
            .setSmallIcon(R.drawable.icon_pills)
            .setContentTitle("Medication Reminder")
            .setContentText("It's time to take your medication!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()


        val notificationId = System.currentTimeMillis()
        val manager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        manager.notify(notificationId.toInt(), notificationBuilder)

        }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "med_channel",
                "Medication Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

}

}