package io.termplux.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.termplux.hybrid.HybridFlutter

open class TPBaseActivity : AppCompatActivity(), TPBaseActivityWrapper {

    private lateinit var mHybridFlutter: HybridFlutter

    override val flutter: Fragment
        get() = mHybridFlutter.getFlutterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (application is TPBaseApplicationWrapper) {
            (application as TPBaseApplicationWrapper).apply {
                mHybridFlutter = hybrid
            }
        } else error(
            message = ""
        )
    }

    override fun onPostResume() {
        super.onPostResume()
        try {
            mHybridFlutter.onPostResume()
        } catch (e: Exception) {
            Log.e(tag, "onPostResume", e)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        try {
            mHybridFlutter.onNewIntent(intent = intent!!)
        } catch (e: Exception) {
            Log.e(tag, "onNewIntent", e)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            mHybridFlutter.onBackPressed()
        } catch (e: Exception) {
            Log.e(tag, "onBackPressed", e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            mHybridFlutter.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } catch (e: Exception) {
            Log.e(tag, "onRequestPermissionsResult", e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            mHybridFlutter.onActivityResult(requestCode, resultCode, data)
        } catch (e: Exception) {
            Log.e(tag, "onActivityResult", e)
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        try {
            mHybridFlutter.onUserLeaveHint()
        } catch (e: Exception) {
            Log.e(tag, "onUserLeaveHint", e)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        try {
            mHybridFlutter.onTrimMemory(level = level)
        } catch (e: Exception) {
            Log.e(tag, "onTrimMemory", e)
        }
    }

    companion object {
        private const val tag: String = "TPBaseActivity"
    }
}