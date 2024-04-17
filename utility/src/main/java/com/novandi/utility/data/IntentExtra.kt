package com.novandi.utility.data

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

object IntentExtra {
    fun getExtra(context: Context, keyName: String): String? {
        val activity = context.findActivity()
        val intent = activity?.intent
        return intent?.getStringExtra(keyName)
    }

    fun deleteExtra(context: Context, keyName: String) {
        val activity = context.findActivity()
        val intent = activity?.intent
        intent?.removeExtra(keyName)
    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}