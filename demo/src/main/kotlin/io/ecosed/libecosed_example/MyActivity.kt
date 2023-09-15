package io.ecosed.libecosed_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ecosed.droid.EcosedActivityImpl
import io.ecosed.droid.EcosedActivityUtils

class MyActivity : AppCompatActivity(), EcosedActivityImpl by EcosedActivityUtils() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachUtils(
            activity = this@MyActivity,
            lifecycle = lifecycle
        )

        setLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        detachUtils(
            lifecycle = lifecycle
        )
    }
}