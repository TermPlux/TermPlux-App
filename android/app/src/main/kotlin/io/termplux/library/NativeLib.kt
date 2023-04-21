package io.termplux.library

class NativeLib {

    companion object {

        init {
            System.loadLibrary("termplux")
        }
    }
}