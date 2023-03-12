package io.termplux.application

import io.flutter.app.FlutterApplication
import io.termplux.activity.MainActivity

class FlutterApp : FlutterApplication() {

    private fun currentActivity() {
        currentActivity = MainActivity()
    }

    companion object {
        fun newInstance() {
            return FlutterApp().currentActivity()
        }
    }
}