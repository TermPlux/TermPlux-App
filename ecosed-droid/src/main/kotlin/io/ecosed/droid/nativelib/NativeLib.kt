package io.ecosed.droid.nativelib

internal class NativeLib {

    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("libecosed")
        }
    }
}