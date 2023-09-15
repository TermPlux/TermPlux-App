package io.ecosed.libecosed_example

import androidx.appcompat.app.AppCompatActivity
import io.ecosed.droid.EcosedDroidActivityImpl
import io.ecosed.droid.EcosedDroidActivityUtils

class MyActivity : AppCompatActivity(), EcosedDroidActivityImpl by EcosedDroidActivityUtils() {
}