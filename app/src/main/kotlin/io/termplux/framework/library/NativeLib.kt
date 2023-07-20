package io.termplux.framework.library

class NativeLib {



    companion object {
        init {
            // 加载原生代码
            System.loadLibrary("termplux")
        }
    }
}