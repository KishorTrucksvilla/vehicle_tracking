package com.example.buttraking.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RestartServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "STOP_TRACKING") {
            val serviceIntent = Intent(context, LocationTrackingService::class.java)
            serviceIntent.action = LocationTrackingService.ACTION_START
            context.startService(serviceIntent)
        }
    }
}
