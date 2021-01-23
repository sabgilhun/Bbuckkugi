package com.sabgil.bbuckkugi.service.channel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager

abstract class BaseChannel(context: Context) {

    private val localBroadcastManager = LocalBroadcastManager.getInstance(context)

    protected fun lifeCycleSafetyRegister(
        lifecycleOwner: LifecycleOwner,
        filteringAction: String,
        onReceive: (Context, Intent) -> Unit
    ) {
        if (lifecycleOwner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    onReceive(context, intent)
                }
            }

            localBroadcastManager
                .registerReceiver(
                    receiver,
                    IntentFilter().apply { addAction(filteringAction) }
                )

            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    localBroadcastManager
                        .unregisterReceiver(receiver)
                    super.onDestroy(owner)
                }
            })
        }
    }

    protected fun sendBroadCast(action: String, intentApply: Intent.() -> Unit) =
        localBroadcastManager.sendBroadcast(Intent(action).apply(intentApply))
}