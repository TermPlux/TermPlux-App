package io.ecosed.engine

import io.ecosed.common.FlutterPluginProxy

interface EngineWrapper: FlutterPluginProxy {
    fun attach()
}