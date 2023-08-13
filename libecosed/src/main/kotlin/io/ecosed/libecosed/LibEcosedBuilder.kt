package io.ecosed.libecosed

import io.ecosed.libecosed.plugin.LibEcosed
import io.ecosed.plugin.EcosedPlugin

object LibEcosedBuilder : LibEcosedImpl {

    override val libecosedPlugin: EcosedPlugin
        get() = LibEcosed()

    override val libecosedChannel: String
        get() = LibEcosed.channel
}