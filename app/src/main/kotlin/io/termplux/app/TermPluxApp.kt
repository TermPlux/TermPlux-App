package io.termplux.app

import android.content.Context
import android.os.Build
import io.flutter.app.FlutterApplication
import org.lsposed.hiddenapibypass.HiddenApiBypass

class TermPluxApp : FlutterApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L")
        }
    }
}