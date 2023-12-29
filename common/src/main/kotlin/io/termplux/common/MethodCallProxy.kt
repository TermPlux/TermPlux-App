package io.termplux.common

import android.os.Bundle

interface MethodCallProxy {
    val methodProxy: String?
    val bundleProxy: Bundle?
}