package io.ecosed.droid

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.Lifecycle

interface EcosedActivityImpl {

    fun Activity.attachUtils(activity: Activity, lifecycle: Lifecycle)
    fun Activity.detachUtils(lifecycle: Lifecycle)

    fun Activity.setLayout()

    fun <T> Activity.execMethodCall(
        name: String,
        method: String,
        bundle: Bundle?
    ): T?
}