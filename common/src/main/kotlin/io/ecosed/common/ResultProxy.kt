package io.ecosed.common

interface ResultProxy {
    fun success(result: Any?)
    fun error(errorCode: String, errorMessage: String?, errorDetails: Any?): Nothing
    fun notImplemented()
}