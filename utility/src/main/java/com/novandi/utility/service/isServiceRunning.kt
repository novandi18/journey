package com.novandi.utility.service

import android.app.ActivityManager
import android.content.Context

fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningServices = manager.runningAppProcesses ?: return false
    for (processInfo in runningServices) {
        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            || processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
        ) {
            for (service in processInfo.pkgList) {
                if (service == serviceClass.name) {
                    return true
                }
            }
        }
    }

    return false
}