package io.ecosed.libecosed_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ecosed.droid.app.EcosedActivityImpl
import io.ecosed.droid.app.EcosedActivityUtils
import io.ecosed.droid.app.EcosedDroidLauncher

@EcosedDroidLauncher
class MyActivity : AppCompatActivity(), EcosedActivityImpl by EcosedActivityUtils<MyActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachUtils(
            activity = this@MyActivity,
            lifecycle = lifecycle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        detachUtils(
            lifecycle = lifecycle
        )
    }
}