package io.ecosed.libecosed.client

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder

internal class EcosedClient constructor(
    context: Context
): ServiceConnection, EcosedCallBack {

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

    }

    override fun onServiceDisconnected(name: ComponentName?) {

    }

    override fun onBindingDied(name: ComponentName?) {
        super.onBindingDied(name)
    }

    override fun onNullBinding(name: ComponentName?) {
        super.onNullBinding(name)
    }

    override fun onEcosedConnected() {

    }

    override fun onEcosedDisconnected() {

    }

    override fun onEcosedDead() {

    }

    override fun onEcosedUnbind() {

    }
}