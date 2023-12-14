package io.ecosed.nativelib

internal class NativeLib private constructor() {

    private external fun stringFromJNI(): String

    internal fun main(){
        stringFromJNI()
    }

    companion object {
        init {
            System.loadLibrary("libecosed")
        }

        fun build(): NativeLib = NativeLib()
    }
}