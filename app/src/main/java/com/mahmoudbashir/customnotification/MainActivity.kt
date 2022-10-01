package com.mahmoudbashir.customnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.mahmoudbashir.customnotification.services.actionServices


class MainActivity : AppCompatActivity() {

    lateinit var btn_notify:Button
    val CHANNEL_ID = "1"


    // Key for the string that's delivered in the action's intent.
    private val KEY_TEXT_REPLY = "key_text_reply"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_notify = findViewById(R.id.basic_Notify_btn)
        //createNotificationChannel()

        btn_notify.setOnClickListener {
            basicNotification()
    //          actionNotification()
     //       replyWithNotification()
            //imgWithNotification()
        }

    }
    private fun basicNotification(){
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Notify basic")
            .setContentText("Notify Title ")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line...")
            ).setPriority(NotificationManager.IMPORTANCE_HIGH)


        with(NotificationManagerCompat.from(this)){
            notify(1,builder.build())
        }
    }

    private fun actionNotification(){
        val snoozeIntent = Intent(this, actionServices::class.java).apply {
            action = AlarmClock.ACTION_SNOOZE_ALARM
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, 0)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(snoozePendingIntent)
            .addAction(R.drawable.notification_icon, "Snooze",
                snoozePendingIntent)

        with(NotificationManagerCompat.from(this)){
            notify(1,builder.build())
        }

    }

    private fun replyWithNotification(){
        var replyLabel: String = resources.getString(R.string.reply_label)
        var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }

        val sendMessageIntent = Intent(this, actionServices::class.java).apply {
            action = AlarmClock.ACTION_SNOOZE_ALARM
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, 0)
        }

        // Build a PendingIntent for the reply action to trigger.
        var replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(applicationContext,
                1,
                sendMessageIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        // Create the reply action and add the remote input.
        var action: NotificationCompat.Action =
            NotificationCompat.Action.Builder(R.drawable.notification_icon,
                "Reply", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build()

        // Build the notification and add the action.
        val newMessageNotification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Message")
            .setContentText("Hello")
            .addAction(action)
            .build()


// Issue the notification.
        with(NotificationManagerCompat.from(this)) {
            notify(1, newMessageNotification)
        }
    }

    private fun imgWithNotification(){
        val icon:Bitmap =BitmapFactory.decodeResource(resources,R.drawable.mpic)

        var notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Mahmoud chat")
            .setContentText("sent new image")
            .setLargeIcon(icon)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(icon)
                .bigLargeIcon(icon)
            ).build()

        with(NotificationManagerCompat.from(this)) {
            notify(1, notification)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MahmoudChannel"
            val descriptionText = "new Channel for basic notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}