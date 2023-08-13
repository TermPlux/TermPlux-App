package io.ecosed.libecosed

import io.ecosed.plugin.EcosedPlugin

interface LibEcosedImpl {
    val libecosedPlugin: EcosedPlugin
    val libecosedChannel: String
}