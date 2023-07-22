package io.termplux.app.framework.library

class NativeLib {


    external fun stringFromJNI(): String

    companion object {
        init {
            // 加载原生代码
            System.loadLibrary("termplux")
        }
    }
}