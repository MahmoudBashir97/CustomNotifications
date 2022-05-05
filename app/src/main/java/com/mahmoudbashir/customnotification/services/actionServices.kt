package com.mahmoudbashir.customnotification.services

import android.app.Notification
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.mahmoudbashir.customnotification.R

class actionServices() : BroadcastReceiver() {
    // Key for the string that's delivered in the action's intent.
    private val KEY_TEXT_REPLY = "key_text_reply"
    private val CHANNEL_ID = "1"
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1 != null) {
            getMessageText(intent = p1)
            val repliedNotification = Notification.Builder(p0, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentText("Replied")
                .build()

// Issue the new notification.
            if (p0 != null) {
                NotificationManagerCompat.from(p0).apply {
                    notify(1, repliedNotification)
                }
            }
        }
    }

    private fun getMessageText(intent: Intent): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_TEXT_REPLY)
    }
}