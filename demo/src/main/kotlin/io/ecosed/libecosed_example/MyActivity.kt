package io.ecosed.libecosed_example

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.ecosed.droid.app.EcosedActivityImpl
import io.ecosed.droid.app.EcosedActivityUtils
import io.ecosed.droid.app.EcosedLauncher

@EcosedLauncher
class MyActivity : AppCompatActivity(), EcosedActivityImpl by EcosedActivityUtils<MyActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachUtils(activity = this@MyActivity, lifecycle = lifecycle)




        Toast.makeText(
            this@MyActivity,
            execMethodCall<Boolean>(
                name = "libecosed",
                method = "is_debug",
                bundle = null
            ).toString(),
            Toast.LENGTH_SHORT
        ).show()



    }

    override fun onDestroy() {
        super.onDestroy()
        detachUtils(lifecycle = lifecycle)
    }
}