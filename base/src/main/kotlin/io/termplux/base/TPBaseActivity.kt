package io.termplux.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.termplux.hybrid.HybridFlutter

abstract class TPBaseActivity : AppCompatActivity(), TPBaseActivityWrapper {

    private lateinit var mHybridFlutter: HybridFlutter

    override val hybrid: HybridFlutter
        get() = mHybridFlutter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (application is TPBaseApplicationWrapper) {
            (application as TPBaseApplicationWrapper).apply {
                mHybridFlutter = hybrid
            }
        } else error(
            message = "Application未实现TPBaseApplicationWrapper方法"
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
        try {
            mHybridFlutter.onNewIntent(intent = intent!!)
        } catch (e: Exception) {
            Log.e(tag, "onNewIntent", e)
        }
    }



    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
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

    @SuppressLint("MissingSuperCall")
    override fun onUserLeaveHint() {
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