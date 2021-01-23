package com.sabgil.bbuckkugi.service.channel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.Data

object CommunicationChannel {
    private const val RECEIVE_RESULT = "RECEIVE_RESULT"
    private const val RECEIVE_RESULT_INTENT_TAG = "RECEIVE_RESULT_INTENT_TAG"

    private const val RECEIVE_ACTION = "RECEIVE_ACTION"
    private const val RECEIVE_ACTION_INTENT_TAG = "RECEIVE_ACTION_INTENT_TAG"

    fun registerClient(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onReceive: (Result<Data>) -> Unit
    ) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val result = intent.getSerializableExtra(RECEIVE_RESULT_INTENT_TAG)
                    @Suppress("UNCHECKED_CAST")
                    onReceive(result as Result<Data>)
                }
            }

            LocalBroadcastManager.getInstance(context)
                .registerReceiver(
                    receiver,
                    IntentFilter().apply { addAction(RECEIVE_RESULT) }
                )

            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    LocalBroadcastManager.getInstance(context)
                        .unregisterReceiver(receiver)
                    super.onDestroy(owner)
                }
            })
        }
    }

    fun registerHost(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onReceive: (Data) -> Unit
    ) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val data = intent.getSerializableExtra(RECEIVE_ACTION_INTENT_TAG) as Data
                    onReceive(data)
                }
            }

            LocalBroadcastManager.getInstance(context)
                .registerReceiver(
                    receiver,
                    IntentFilter().apply { addAction(RECEIVE_ACTION) }
                )

            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    LocalBroadcastManager.getInstance(context)
                        .unregisterReceiver(receiver)
                    super.onDestroy(owner)
                }
            })
        }
    }

    fun sendData(context: Context, data: Data) {
        LocalBroadcastManager
            .getInstance(context)
            .sendBroadcast(Intent(RECEIVE_ACTION).apply {
                putExtra(RECEIVE_ACTION_INTENT_TAG, data)
            })
    }
}