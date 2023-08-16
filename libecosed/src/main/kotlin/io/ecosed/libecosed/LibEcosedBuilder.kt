package io.ecosed.libecosed

import io.ecosed.libecosed.plugin.LibEcosed
import io.ecosed.plugin.EcosedPlugin

object LibEcosedBuilder : LibEcosedImpl {

    override val mLibEcosed: EcosedPlugin
        get() = LibEcosed()
}