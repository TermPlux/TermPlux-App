package io.termplux.hybrid

import android.app.Application
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface HybridWrapper {

    fun withApplication(application: Application): HybridWrapper
    fun build(): HybridFlutter

    fun getFlutterFragment(): Fragment

    fun onPostResume()
    fun onNewIntent(intent: Intent)
    fun onBackPressed()
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun onUserLeaveHint()
    fun onTrimMemory(level: Int)



}