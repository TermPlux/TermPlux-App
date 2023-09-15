package io.ecosed.droid

import io.ecosed.droid.plugin.LibEcosedPlugin
import io.ecosed.droid.plugin.LibEcosed

interface LibEcosedImpl {
    val mLibEcosed: LibEcosed
}

object LibEcosedBuilder : LibEcosedImpl {

    override val mLibEcosed: LibEcosed
        get() = LibEcosedPlugin()
}