package io.ecosed.libecosed

import io.ecosed.libecosed.plugin.LibEcosedPlugin
import io.ecosed.libecosed.plugin.LibEcosed

interface LibEcosedImpl {
    val mLibEcosed: LibEcosed
}

object LibEcosedBuilder : LibEcosedImpl {

    override val mLibEcosed: LibEcosed
        get() = LibEcosedPlugin()
}