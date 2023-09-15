package io.ecosed.libecosed_example

import androidx.appcompat.app.AppCompatActivity
import io.ecosed.libecosed.EcosedDroidActivityImpl
import io.ecosed.libecosed.EcosedDroidActivityUtils

class MyActivity : AppCompatActivity(), EcosedDroidActivityImpl by EcosedDroidActivityUtils() {
}