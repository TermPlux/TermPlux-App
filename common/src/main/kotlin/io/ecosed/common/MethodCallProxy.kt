package io.ecosed.common

import android.os.Bundle

interface MethodCallProxy {
    val method: String?
    val bundle: Bundle?
}